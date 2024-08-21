package counter.six_atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedCounterExample {

	protected static int threadSize = 6;
	protected static Thread[] threads = new Thread[threadSize];
	protected static int threadCalled = 0;
	// final not to be re-assigned
	protected static final Object lock = new Object();

	public static void main(String[] args) {

		int countLimit = 10000000;

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
		System.out.println("Atomic Thread called: " + threadCalled);
	}
}
