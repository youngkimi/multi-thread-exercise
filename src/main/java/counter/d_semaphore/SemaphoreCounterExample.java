package counter.d_semaphore;

public class SemaphoreCounterExample {

	protected static int threadSize = 3;
	protected static Thread[] threads = new Thread[threadSize];

	// set volatile to these variables.
	protected static volatile int threadCalled = 0;
	protected static volatile int lockCount = 1;

	public static void main(String[] args) {

		int countLimit = 100;

		for (int i = 0; i < threadSize; i++) {
			Runnable counter = new SemaphoreNumberCounter(countLimit);
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
		System.out.println("Thread called: " + threadCalled);
	}

	private static class SemaphoreNumberCounter implements Runnable {
		private final int countLimit;

		public SemaphoreNumberCounter(int countLimit) {

			if (countLimit < 0) {
				throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
			}

			this.countLimit = countLimit;
		}

		@Override
		public void run() {
			for (int i = 0; i < countLimit; i++) {

				acquireLock();

				criticalSection();

				releaseLock();
			}
		}

		private void acquireLock() {
			while (--lockCount <= 0) {
				lockCount++;
			}
		}

		private void criticalSection() {
			threadCalled ++;
		}

		private void releaseLock() {
			lockCount ++;
		}
	}

}
