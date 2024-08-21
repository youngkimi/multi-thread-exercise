package counter.one;

public class SynchronizedVolatileCounter {

    static volatile int count = 0;
    static final int countLimitPerThread = 100000;

    public static void main(String[] args) throws InterruptedException {


        int threadSize = 6;
        Thread[] threads = new Thread[threadSize];

        for (int i = 0; i < threadSize; i++) {
            threads[i] = new Thread(new CountTask());
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Counted " + count + " times.");
    }

    static class CountTask implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < countLimitPerThread; i++) {
                count();
            }
        }

        private synchronized void count() {
            count++;
        }
    }
}
