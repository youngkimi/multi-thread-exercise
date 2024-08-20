package counter.simple;

public class SimpleCounterExample {
	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new SimpleNumberCounter("Thread 1", 101, countLimit);
			Runnable counter2 = new SimpleNumberCounter("Thread 2", 201, countLimit);

			Thread thread1 = new Thread(counter1);
			Thread thread2 = new Thread(counter2);

			thread1.start();
			thread2.start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
