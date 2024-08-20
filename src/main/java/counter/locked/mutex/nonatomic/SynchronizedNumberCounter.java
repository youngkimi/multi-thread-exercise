package counter.locked.mutex.nonatomic;

import static counter.locked.mutex.nonatomic.CounterExample.*;

public class SynchronizedNumberCounter implements Runnable {
	private final String counterName;
	private final int countLimit;
	private final int turn;

	public SynchronizedNumberCounter(String counterName, int countLimit, int turn) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.countLimit = countLimit;
		this.turn = turn;
	}

	@Override
	public void run() {

		int count = 0;

		while (count++ < countLimit) {
			lockOn(turn);

			criticalSection(counterName);

			lockOff();
		}
	}

	private static void lockOn(int turn) {
		while (turn != lock % threadNum) {
			// busy waiting
		}
	}

	private static void criticalSection(String counterName) {
		accountMap.put(accountName, accountMap.get(accountName)+100) ;
	}

	private static synchronized void lockOff() {
		lock++;
	}
}
