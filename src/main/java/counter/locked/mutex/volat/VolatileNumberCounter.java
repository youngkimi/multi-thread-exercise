package counter.locked.mutex.volat;

import static counter.locked.mutex.volat.VolatileSynchronizedCounterExample.*;

public class VolatileNumberCounter implements Runnable {

	/*
		Lock might not be incremented properly.
		Due to synchronization.
	 */

	private final String counterName;
	private final int stt;
	private final int countLimit;
	private final int turn;

	public VolatileNumberCounter(String counterName, int stt, int countLimit, int turn) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.stt = stt;
		this.countLimit = countLimit;
		this.turn = turn;
	}

	@Override
	public void run() {

		int count = 0;

		while (count < countLimit) {

			lockOn(turn);

			count = criticalTask(count);

			lockOff();
		}
	}

	private int criticalTask(int count) {
		System.out.println(counterName + " - Count: " + (stt + count++));

		try {
			Thread.sleep(1000); // 1 second pause
		} catch (InterruptedException e) {
			System.out.println(counterName + " interrupted.");
		}
		return count;
	}

	private static void lockOn(int turn) {
		while (turn != lock % threadNum) {
			// busy waiting ...
		}
	}

	private static void lockOff() {
		lock ++;
	}
}
