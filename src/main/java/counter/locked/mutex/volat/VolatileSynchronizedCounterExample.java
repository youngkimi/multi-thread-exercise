package counter.locked.mutex.volat;

public class VolatileSynchronizedCounterExample {

	protected static volatile int lock = 0;
	protected static final int threadNum = 3;

	public static void main(String[] args) {

		int countLimit = 5;

		Runnable[] counters = new Runnable[threadNum];
		Thread[] threads = new Thread[threadNum];

		try {

			for (int i = 0; i < threadNum; i++) {
				counters[i] = new VolatileNumberCounter("Thread"+i, 1, countLimit, i);
				threads[i] = new Thread(counters[i]);
			}

			for (int i = 0; i < threadNum; i++) {
				threads[i].start();
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
