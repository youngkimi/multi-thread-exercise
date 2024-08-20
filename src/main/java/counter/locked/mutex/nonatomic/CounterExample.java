package counter.locked.mutex.nonatomic;

import java.util.HashMap;
import java.util.Map;

public class CounterExample {
	protected static final String accountName = "family account";
	protected static final int threadNum = 8;
	protected static volatile int lock = 0;
	protected static volatile Map<String, Integer> accountMap = new HashMap<>();

	public static void main(String[] args) {

		int countLimit = 10000;

		System.out.println("number of threads - "  + threadNum + ", count limit - " + countLimit);

		accountMap.put(accountName, 0);

		Thread[] threads = new Thread[threadNum];

		for (int i = 0; i < threadNum; i++) {

			if (i % 2 == 0) {
				threads[i] = new Thread(new NonSynchronizedPlusCounter("Plus Thread"+i, countLimit, i));
			} else {
				threads[i] = new Thread(new NonSynchronizedMinusCounter("Minus Thread"+i, countLimit, i));
			}

			threads[i].start();
		}

		for (int i = 0; i < threadNum; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.out.println("Error Occurs: " + e.getMessage());
			}
		}

		System.out.println("Final lock value: " + lock);

		System.out.println("Final account money: " + accountMap.get(accountName));
	}
}
