package counter.five_synchronized;

public class SynchronizedCounterExample {

	protected static int threadSize = 3;
	protected static Thread[] threads = new Thread[threadSize];

	// set volatile to these variables.
	protected static volatile int threadCalled = 0;
	protected static volatile int lock = 3;

	public static void main(String[] args) {

		int countLimit = 1000;

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
		System.out.println("Thread called: " + threadCalled);
	}
}
