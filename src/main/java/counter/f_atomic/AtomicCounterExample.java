package counter.f_atomic;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class AtomicCounterExample {

	protected static int threadSize = 8;
	protected static Thread[] threads = new Thread[threadSize];
	protected static int threadCalled = 0;
	protected static int lock = 1;
	protected static Queue<Thread> readyQueue = new LinkedBlockingQueue<>();

	public static void main(String[] args) {

		int countLimit = 10000000;

		for (int i = 0; i < threadSize; i++) {
			Runnable counter = new SynchronizedNumberCounter(countLimit);
			threads[i] = new Thread(counter);
			threads[i].start();
		}

		for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
				System.out.println("Thread Interrupted: " + t.getName());
            }
        }

		System.out.println("Thread size: " + threadSize + ", counterLimit: " + countLimit);
		System.out.println("Atomic Thread called: " + threadCalled);
	}

	private static class SynchronizedNumberCounter implements Runnable {
		private final int countLimit;

		public SynchronizedNumberCounter(int countLimit) {

			if (countLimit < 0) {
				throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
			}

			this.countLimit = countLimit;
		}

		@Override
		public void run() {
			for (int i = 0; i < countLimit; i++) {
                try {
                    acquireLock();
					criticalSection();
					releaseLock();
                } catch (InterruptedException e) {
					Thread.currentThread().interrupt();
                }
			}
		}

		private synchronized void acquireLock() throws InterruptedException {
			while (lock < 1) {
				readyQueue.add(Thread.currentThread());
				Thread.currentThread().wait();
			}
			lock --;
		}

		private synchronized void criticalSection() {
			threadCalled ++;
		}

		private synchronized void releaseLock() {
			lock++;
			if (!readyQueue.isEmpty()) {
				Thread nextThread = readyQueue.poll();
				nextThread.interrupt();
			}
		}
    }
}
