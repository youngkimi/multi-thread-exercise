package counter.four_semaphore;

import static counter.four_semaphore.SemaphoreCounterExample.*;

public class SemaphoreNumberCounter implements Runnable {
	private final int countLimit;

	public SemaphoreNumberCounter(int countLimit) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.countLimit = countLimit;
	}

	@Override
	public void run() {
		for (int i = 0; i < countLimit; i++) {

			acquireLock();

			criticalSection();

			releaseLock();
		}
	}

	private void acquireLock() {
		while (lock <= 0) {

		};
		lock --;
	}

	private void criticalSection() {
		threadCalled ++;
	}

	private void releaseLock() {
		lock ++;
	}
}
