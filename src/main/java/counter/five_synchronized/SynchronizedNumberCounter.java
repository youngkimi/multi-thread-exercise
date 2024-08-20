package counter.five_synchronized;

import static counter.five_synchronized.SynchronizedCounterExample.lock;
import static counter.five_synchronized.SynchronizedCounterExample.threadCalled;

public class SynchronizedNumberCounter implements Runnable {
	private final int countLimit;

	public SynchronizedNumberCounter(int countLimit) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.countLimit = countLimit;
	}

	@Override
	public void run() {

		for (int i = 0; i < countLimit; i++) {

			takeLock();

			criticalSection();

			releaseLock();
		}
	}

	private synchronized void takeLock() {
		while (lock <= 0) {
		};
		lock --;
	}

	private synchronized void criticalSection() {
		threadCalled ++;
	}

	private synchronized void releaseLock() {
		lock ++;
	}
}
