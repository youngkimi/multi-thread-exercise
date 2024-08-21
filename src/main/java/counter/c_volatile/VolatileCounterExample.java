package counter.c_volatile;

public class VolatileCounterExample {

	protected static int threadSize = 3;
	protected static Thread[] threads = new Thread[threadSize];

	// set volatile to these variables.
	protected static volatile int threadCalled = 0;
	protected static volatile int turn = 0;

	public static void main(String[] args) {

		int countLimit = 10000000;

		for (int i = 0; i < threadSize; i++) {
			Runnable counter = new VolatileNumberCounter("Thread " + i, countLimit, i);
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

	private static class VolatileNumberCounter implements Runnable {
		private final String counterName;
		private final int countLimit;
		private final int trigger;

		public VolatileNumberCounter(String counterName, int countLimit, int trigger) {

			if (countLimit < 0) {
				throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
			}

			this.counterName = counterName;
			this.countLimit = countLimit;
			this.trigger = trigger;
		}

		@Override
		public void run() {
			for (int i = 0; i < countLimit; i++) {

				isWaiting();

				criticalSection();

				passTheBall();
			}
		}

		private void isWaiting() {
			while (turn % threadSize != trigger) {

			};
		}

		private void criticalSection() {
			threadCalled++;
		}

		private void passTheBall() {
			turn ++;
			if (turn == threadSize) {
				turn = 0;
			}
		}
	}

}
