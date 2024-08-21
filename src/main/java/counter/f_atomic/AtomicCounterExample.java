package counter.f_atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounterExample {

	protected static int threadSize = 8;
	protected static Thread[] threads = new Thread[threadSize];
	// int value updated atomically. lock-free thread-safe programming on single variables.
	protected static AtomicInteger threadCalled = new AtomicInteger(0);
	protected static final Object lock = new Object();

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
				criticalSection();
			}
		}

		private synchronized void criticalSection() {
			threadCalled.getAndIncrement();
		}
    }
}
