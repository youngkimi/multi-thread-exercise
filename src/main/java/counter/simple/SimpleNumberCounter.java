package counter.simple;

public class SimpleNumberCounter implements Runnable {
	private final String counterName;
	private final int stt;
	private final int countLimit;

	public SimpleNumberCounter(String counterName, int stt, int countLimit) {

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
			System.out.println(counterName + " - Count: " + (stt + count++));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(counterName + " interrupted.");
			}
		}
	}
}
