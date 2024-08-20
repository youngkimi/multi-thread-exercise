package counter.locked.spinlock.nonatomic;

import static counter.locked.spinlock.nonatomic.CounterExample.*;

public class NonSynchronizedCounter implements Runnable {
	private final String counterName;
	private final int countLimit;

	public NonSynchronizedCounter(String counterName, int countLimit) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.countLimit = countLimit;
	}

	@Override
	public void run() {

		int count = 0;

		while (count++ < countLimit) {
			lockOn();

			criticalSection(counterName);

			lockOff();
		}
	}

	private static void lockOn() {
		while (lock <= 0) {
			lock--;
		}
	}

	private static void criticalSection(String counterName) {
		try {
			methodCalled++;
			Thread.sleep(1);
		} catch (InterruptedException e) {
			System.out.println("Interruption Error Occurs: " + counterName);
		}
	}

	private static void lockOff() {
		lock++;
	}
}
