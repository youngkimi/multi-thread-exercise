package counter.one_simiple;

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
}
