package counter.locked.monitor;

import static counter.locked.monitor.LockedSynchronizedCounterExample.*;

public class LockedNumberCounter implements Runnable {
	private final String counterName;
	private final int stt;
	private final int countLimit;

	public LockedNumberCounter(String counterName, int stt, int countLimit) {

		if (countLimit < 0) {
			throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
		}

		this.counterName = counterName;
		this.stt = stt;
		this.countLimit = countLimit;
	}

	@Override
	public void run() {

		int count = 0;

		while (count < countLimit) {

			synchronized (lock) {
				System.out.println(counterName + " - Count: " + (stt + count++));
				lock.notify();

				try {
					if (count < countLimit) {
						lock.wait();
					}
				} catch (InterruptedException e) {
					System.out.println(counterName + " interrupted.");
				}
			}

			try {
				Thread.sleep(1000); // 1 second pause
			} catch (InterruptedException e) {
				System.out.println(counterName + " interrupted.");
			}
			System.out.println(counterName + " finished counting.");
		}
	}
}
