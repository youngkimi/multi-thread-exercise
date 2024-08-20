package counter.locked.mutex.atomic;

import static counter.locked.mutex.atomic.AtomicSynchronizedCounterExample.*;

public class AtomicNumberCounter implements Runnable {
	private final String counterName;
	private final int countLimit;
	private final int trigger;

	public AtomicNumberCounter(String counterName, int countLimit, int trigger) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.countLimit = countLimit;
		this.trigger = trigger;
	}

	@Override
	public void run() {

		int count = 0;

		while (count++ < countLimit) {

			while (trigger != lock.get() % threadNum) {
				// busy waiting
			}

			lock.incrementAndGet();
			methodCalled.incrementAndGet();
		}
	}
}
