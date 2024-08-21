package counter.a_simiple;

public class SimpleCounterExample {

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new SimpleNumberCounter("Thread 1", countLimit);
			Runnable counter2 = new SimpleNumberCounter("Thread 2", countLimit);
			Runnable counter3 = new SimpleNumberCounter("Thread 3", countLimit);

			Thread thread1 = new Thread(counter1);
			Thread thread2 = new Thread(counter2);
			Thread thread3 = new Thread(counter3);

			thread1.start();
			thread2.start();
			thread3.start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class SimpleNumberCounter implements Runnable {
		private final String counterName;
		private final int countLimit;

		public SimpleNumberCounter(String counterName, int countLimit) {

			if (countLimit < 0) {
				throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
			}

			this.counterName = counterName;
			this.countLimit = countLimit;
		}

		@Override
		public void run() {
			for (int i = 1; i <= countLimit; i++) {
				System.out.println(counterName + " - Counted: " + i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(counterName + " interrupted.");
				}
			}
		}
	}
}
