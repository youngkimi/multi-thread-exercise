package counter.locked.spinlock.nonatomic;

public class CounterExample {

	protected static final int threadSize = 6; // make threadNum even
	protected static volatile int lock = threadSize / 2;
 	protected static volatile int methodCalled = 0;

	public static void main(String[] args) throws InterruptedException {

		int countLimit = 1000;

		System.out.println("number of threads - "  + threadSize + ", count limit - " + countLimit);

		Thread[] threads = new Thread[threadSize];

		for (int i = 0; i < threadSize; i++) {
			threads[i] = new Thread(new SynchronizedCounter("Thread" + i, countLimit));
			threads[i].start();
		}

		for (Thread thread : threads) {
			thread.join();
		}

		// This should be thread size * countLimit. but ...
		System.out.println("method Called: " + methodCalled);
	}
}
