package counter.six_atomic;

import static counter.six_atomic.SynchronizedCounterExample.lock;
import static counter.six_atomic.SynchronizedCounterExample.threadCalled;

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
			criticalSection();
		}
	}

	private void criticalSection() {
		synchronized (lock) {
			threadCalled++;
		}
	}
}
