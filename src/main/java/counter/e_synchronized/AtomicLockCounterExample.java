package counter.e_synchronized;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicLockCounterExample {

	protected static int threadSize = 6;
	protected static Thread[] threads = new Thread[threadSize];
	// set volatile to these variables.
	protected static volatile int threadCalled = 0;
	// set lock final not to be reassigned.
	protected static AtomicInteger lockCount = new AtomicInteger(1);

	public static void main(String[] args) {

		/*
			Slow, but works!
			Thread size: 6, counterLimit: 10000000
			Atomic Thread called: 60000000
		 */

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
				waitLock();
				criticalSection();
				releaseLock();
			}
		}

		private void waitLock() {
			while (lockCount.decrementAndGet() < 0) {
				lockCount.incrementAndGet();
			}
		}

		private void criticalSection() {
			threadCalled++;
		}

		private void releaseLock() {
			lockCount.incrementAndGet();
		}
    }
}
