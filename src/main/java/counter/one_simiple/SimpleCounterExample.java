package counter.one_simiple;

public class SimpleCounterExample {

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new SimpleNumberCounter("Thread 1", 101, countLimit);
			Runnable counter2 = new SimpleNumberCounter("Thread 2", 201, countLimit);
			Runnable counter3 = new SimpleNumberCounter("Thread 3", 301, countLimit);

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
}
