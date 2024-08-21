package counter.b_spinlock;

public class LockedCounterExample {

	protected static int threadSize = 3;
	protected static int turn = 0;

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			for (int i = 0; i < threadSize; i++) {
				Runnable counter = new LockedNumberCounter("Thread " + i, countLimit, i);
				Thread thread = new Thread(counter);
				thread.start();
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class LockedNumberCounter implements Runnable {
		private final String counterName;
		private final int countLimit;
		private final int trigger;

		public LockedNumberCounter(String counterName, int countLimit, int trigger) {

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

				System.out.println(counterName + " called.");

				passTheBall();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(counterName + " interrupted.");
				}
			}
		}

		private void isWaiting() {
			while (turn % threadSize != trigger) {

			}
		}
		private void passTheBall() {
			turn ++;
			if (turn == threadSize) {
				turn = 0;
			}
		}
	}
}
