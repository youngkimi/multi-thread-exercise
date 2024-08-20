package counter.one_volatile;

import static counter.one_volatile.VolatileCounterExample.*;

public class VolatileNumberCounter implements Runnable {
	private final String counterName;
	private final int countLimit;
	private final int trigger;

	public VolatileNumberCounter(String counterName, int countLimit, int trigger) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.countLimit = countLimit;
		this.trigger = trigger;
	}

	@Override
	public void run() {
		for (int i = 0; i < countLimit; i++) {

			isWaiting();

			criticalSection();

			passTheBall();
		}
	}

	private void isWaiting() {
		while (turn % threadSize != trigger) {

		};
	}

	private void criticalSection() {
		threadCalled++;
	}

	private void passTheBall() {
		turn ++;
		if (turn == threadSize) {
			turn = 0;
		}
	}
}
