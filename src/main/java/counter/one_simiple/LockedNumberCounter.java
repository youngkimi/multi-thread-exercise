package counter.one_simiple;

import static counter.one_simiple.LockedCounterExample.threadSize;
import static counter.one_simiple.LockedCounterExample.turn;

public class LockedNumberCounter implements Runnable {
	private final String counterName;
	private final int countLimit;
	private final int trigger;

	public LockedNumberCounter(String counterName, int countLimit, int trigger) {

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

			System.out.println(counterName + " called.");

			passTheBall();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(counterName + " interrupted.");
			}
		}
	}

	private void isWaiting() {
		while (turn % threadSize != trigger) {

		};
	}
	private void passTheBall() {
		turn ++;
		if (turn == threadSize) {
			turn = 0;
		}
	}
}
