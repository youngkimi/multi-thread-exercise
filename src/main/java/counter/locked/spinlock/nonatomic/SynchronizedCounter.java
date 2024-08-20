package counter.locked.spinlock.nonatomic;

import static counter.locked.spinlock.nonatomic.CounterExample.lock;
import static counter.locked.spinlock.nonatomic.CounterExample.methodCalled;

public class SynchronizedCounter implements Runnable {
	private final String counterName;
	private final int countLimit;

	public SynchronizedCounter(String counterName, int countLimit) {

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

	private static synchronized void lockOn() {
		while (lock <= 0) {
		}
		lock--;
	}

	private static synchronized void criticalSection(String counterName) {
		try {
			methodCalled++;
			Thread.sleep(1);
		} catch (InterruptedException e) {
			System.out.println("Interruption Error Occurs: " + counterName);
		}
	}

	private static synchronized void lockOff() {
		lock++;
	}
}
