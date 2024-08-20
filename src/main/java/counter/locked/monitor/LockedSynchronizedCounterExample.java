package counter.locked.monitor;

public class LockedSynchronizedCounterExample {

	protected static final Object lock = new Object();

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new LockedNumberCounter("Thread 1", 101, countLimit);
			Runnable counter2 = new LockedNumberCounter("Thread 2", 201, countLimit);

			Thread thread1 = new Thread(counter1);
			Thread thread2 = new Thread(counter2);

			thread1.start();
			thread2.start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
