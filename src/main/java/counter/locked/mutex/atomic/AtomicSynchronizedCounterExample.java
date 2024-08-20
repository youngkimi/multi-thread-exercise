package counter.locked.mutex.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicSynchronizedCounterExample {
	protected static final int threadNum = 6;
	protected static AtomicInteger lock = new AtomicInteger(0);
	protected static AtomicInteger methodCalled = new AtomicInteger(0);

	public static void main(String[] args) {

		int countLimit = 1000000;

		System.out.println("number of threads - "  + countLimit + ", count limit - " + countLimit);

		Thread[] threads = new Thread[threadNum];

		for (int i = 0; i < threadNum; i++) {
			threads[i] = new Thread(new AtomicNumberCounter("Thread"+i, countLimit, i));
			threads[i].start();
		}

		for (int i = 0; i < threadNum; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Final lock Value: " + lock.get());
		System.out.println("Final methodCalled Value: " + methodCalled.get());
	}
}
