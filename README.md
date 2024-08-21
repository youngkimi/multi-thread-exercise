## Multithreading Exercise

Our goal of this exercise is to create a matrix multiplication program using multithreading.

### 1. Simple Counter

Let's start by making a simple counter with `Runnable`.

The `Runnable` interface represents a task that can be executed by a thread. By implementing this interface, you can define the process to be handled and pass it to a thread for concurrent execution. 

Here is basic example to illustrate how to use `Runnable` and `Thread` :

`SimpleNumberCounter` counts `counterLimit` times, starting from `stt`.

```java
package counter.a_simiple;

public class SimpleCounterExample {

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new SimpleNumberCounter("Thread 1", countLimit);
			Runnable counter2 = new SimpleNumberCounter("Thread 2", countLimit);
			Runnable counter3 = new SimpleNumberCounter("Thread 3", countLimit);

			Thread thread1 = new Thread(counter1);
			Thread thread2 = new Thread(counter2);
			Thread thread3 = new Thread(counter3);

			thread1.start();
			thread2.start();
			thread3.start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class SimpleNumberCounter implements Runnable {
		private final String counterName;
		private final int countLimit;

		public SimpleNumberCounter(String counterName, int countLimit) {

			if (countLimit < 0) {
				throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
			}

			this.counterName = counterName;
			this.countLimit = countLimit;
		}

		@Override
		public void run() {
			for (int i = 1; i <= countLimit; i++) {
				System.out.println(counterName + " - Counted: " + i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(counterName + " interrupted.");
				}
			}
		}
	}
}
```

Here is the result. 

```
Thread 1 - Counted: 1
Thread 3 - Counted: 1
Thread 2 - Counted: 1
Thread 1 - Counted: 2
Thread 3 - Counted: 2
Thread 2 - Counted: 2
Thread 1 - Counted: 3
Thread 3 - Counted: 3
Thread 2 - Counted: 3
Thread 1 - Counted: 4
Thread 2 - Counted: 4
Thread 3 - Counted: 4
Thread 1 - Counted: 5
Thread 2 - Counted: 5
Thread 3 - Counted: 5
```

As you can check, threads seem to work concurrently.
But the message that `Thread 3` has finished its job might show up either after or before `Thread 2` finished counting.

Let's raise the bar. Due to certain business conditions, `Thread1` has priority over `Thread2` for each count. `Thread2` can only count after `Thread1` has completed its count. How can we achieve that? The Key is `Lock`. ~~sounds like a punchline~~

### 2. Counter Using Lock (spinlock)

Imagine a `Group Therapy` session. 

Take a ball, talk about what's on your mind, pass the ball, and your turn is over. Now listen to what the next person has to say. You can talk when you get the ball again.

This is what we're going to do.  

Take a lock, do what you're supposed to do, pass the lock, and your turn is over. Now wait till someone hand the lock back to you again. 

We added a `turn` and a `trigger`. The `trigger` is a specific number assigned when the thread is created. The thread compares the `trigger` to the `turn`.
After the task is finished, we increment the `turn`. If the `turn` equals the threadSize, we reset it to zero.

```java
package counter.b_spinlock;

public class LockedCounterExample {

	protected static int threadSize = 3;
	protected static int turn = 0;

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			for (int i = 0; i < threadSize; i++) {
				Runnable counter = new LockedNumberCounter("Thread " + i, countLimit, i);
				Thread thread = new Thread(counter);
				thread.start();
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class LockedNumberCounter implements Runnable {
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
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println(counterName + " interrupted.");
				}
			}
		}

		private void isWaiting() {
			while (turn % threadSize != trigger) {

			}
		}
		private void passTheBall() {
			turn ++;
			if (turn == threadSize) {
				turn = 0;
			}
		}
	}
}
```

```
Thread 0 called.
Thread 1 called.
Thread 2 called.
Thread 0 called.
Thread 1 called.
Thread 2 called.
Thread 0 called.
Thread 1 called.
Thread 2 called.
Thread 0 called.
Thread 1 called.
Thread 2 called.
Thread 0 called.
Thread 1 called.
Thread 2 called.
```

The process where only one thread can access shared variables is called `mutual exclusion`, or simply `mutex`.
It seems fine, right? But... is it? 

### 3. volatile

```java
package counter.c_volatile;

public class VolatileCounterExample {

	protected static int threadSize = 3;
	protected static Thread[] threads = new Thread[threadSize];

	// set volatile to these variables.
	protected static int threadCalled = 0;
	protected static int turn = 0;

	public static void main(String[] args) {

		int countLimit = 1000;

		for (int i = 0; i < threadSize; i++) {
			Runnable counter = new VolatileNumberCounter("Thread " + i, countLimit, i);
			threads[i] = new Thread(counter);
			threads[i].start();
		}

		for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
				System.out.println("Thread Interrupted: " + t.getName());
            }
        }

		System.out.println("Thread size: " + threadSize + ", counterLimit: " + countLimit);
		System.out.println("Thread called: " + threadCalled);
	}

	private static class VolatileNumberCounter implements Runnable {
		private final String counterName;
		private final int countLimit;
		private final int trigger;

		public VolatileNumberCounter(String counterName, int countLimit, int trigger) {

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

				criticalSection();

				passTheBall();
			}
		}

		private void isWaiting() {
			while (turn % threadSize != trigger) {

			};
		}

		private void criticalSection() {
			threadCalled++;
		}

		private void passTheBall() {
			turn ++;
			if (turn == threadSize) {
				turn = 0;
			}
		}
	}

}
```

This is a counter counts how much every thread called. Obviously, it must be `threadSize` multiplied with `counterLimit`. 

```
Thread size: 3, counterLimit: 100
Thread called: 300
```

It works fine, but if you increase the `counterLimit`, something weird happens.
When I set `counterLimit` to 1000, I waited almost ten minutes, but it ended up looping infinitely.

It's because of thread cache. We need to know which `turn` each thread is watching. In fact, the value of `turn` that each thread watches can differ sometimes because each thread has its own cache.
Even if one thread changes the value of `turn`, the others might still see the old value of `turn` from their cache.

We can fix this problem by declaring lock as `volatile`. This way, each thread watches the value of `turn` from memory, not from its cache.

```
Thread size: 3, counterLimit: 10000000
Thread called: 30000000
```

Now it’s perfect, right? Unfortunately, it’s not.

Let's take a moment to think before we move on to the next chapter.

### 4. Semaphore

You might have thought we were multithreading, but to be precise, we’re not.

We've only used one thread at a time. Why don't we increase the `lock` size so that multiple tasks can be handled simultaneously?

```java
private void acquireLock() {
    while (--lock <= 0) {
        lock++;
    }
}

private void criticalSection() {
    threadCalled ++;
}

private void releaseLock() {
    lock ++;
}
```
When a thread needs to work, it first acquires a lock. If all locks are occupied by other threads, it cancels the acquisition of the lock.
If a lock is available, the thread acquires it by decrementing the lock count.
Once the task is finished, the thread releases the lock by incrementing the lock count.

But unfortunately, you need to make sure that shared variables are accessed by only one thread at a time.
If multiple threads access them simultaneously, it can cause concurrency problems.
That's why you should keep the `lockCount` at 1, which is what we call a `binary semaphore`.

You might think there is no difference between `mutex` and `binary semaphore`, but there are some distinctions.
First, since a `mutex` can lock a target object, it cannot be accessed by another thread as long as one thread is holding the lock.
On the other hand, `binary semaphore` doesn't enforce ownership, meaning that any thread can release the semaphore, not just the one that acquired it.

waiting thread implemented by `mutex` cannot be wakened up by another thread.

```
Thread size: 3, counterLimit: 100
Thread called: 299
```

But when the job is completed, but the results may not be as expected. 
Or increasing `countLimit` can lead to the same problems: infinite loops.
Why do these things happen?

### 5. synchronized

The reason the code isn't working is that the `lock` value might be lost or overwritten during the task.
To address this, we added `volatile` to the shared variables so that each thread can see the up-to-date values.
However, if the `lock` value is greater than 0, multiple threads might mistakenly believe they can acquire it, resulting in more threads acquiring locks than are actually available.
Additionally, when threads update the `lock` value, it might be overwritten due to simultaneous access.
Therefore, we need to ensure that access to the shared variables is restricted to only one thread at a time. In java, we can use `synchronized`.

[//]: # ( 자바 객체의 Lock 이야기와, &#40;this&#41;를 사용하면 안되는 이유. thread Local 에 대해서. )

```java
package counter.e_synchronized;

public class SynchronizedCounterExample {

    protected static int threadSize = 8;
    protected static Thread[] threads = new Thread[threadSize];
    // set volatile to these variables.
    protected static volatile int threadCalled = 0;
    // set lock final not to be reassigned.
    protected static final Object lock = new Object();

    public static void main(String[] args) {

        int countLimit = 1000000;

        for (int i = 0; i < threadSize; i++) {
            Runnable counter = new SynchronizedNumberCounter(countLimit);
            threads[i] = new Thread(counter);
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread Interrupted: " + t.getName());
            }
        }

        System.out.println("Thread size: " + threadSize + ", counterLimit: " + countLimit);
        System.out.println("Atomic Thread called: " + threadCalled);
    }

    private static class SynchronizedNumberCounter implements Runnable {
        private final int countLimit;

        public SynchronizedNumberCounter(int countLimit) {

            if (countLimit < 0) {
                throw new IllegalArgumentException("Count Limit Should Be 0 Or Greater.");
            }

            this.countLimit = countLimit;
        }

        @Override
        public void run() {
            for (int i = 0; i < countLimit; i++) {
                criticalSection();
            }
        }

        private synchronized void criticalSection() {
            synchronized (lock) {
                threadCalled++;
            }
        }
    }
}
```

I refactored the code as follows. 
All shared variables are declared as `volatile`, and method modifying the shared variables are declared as `synchronized`.

```
Thread size: 8, counterLimit: 100000000
Atomic Thread called: 800000000
```

Impressive!

[//]: # (# synchronized 는 성능 저하의 문제가 발생할 수 있으므로, 최대한 좁은 스코프에서 사용해야 한다. )



### 6. atomic lock




But, although you add `synchronized` to your method accessing to shared variables, error may occur during 



Simply adding synchronized to the methods that use the shared variables makes it work perfectly.

But in fact, simply adding `synchronized` is not enough. 

```
Thread0 - Count: 1
Thread1 - Count: 1
Thread2 - Count: 1
Thread0 - Count: 2
Thread1 - Count: 2
Thread2 - Count: 2
Thread0 - Count: 3
Thread1 - Count: 3
Thread2 - Count: 3
Thread0 - Count: 4
Thread1 - Count: 4
Thread2 - Count: 4
Thread0 - Count: 5
Thread1 - Count: 5
Thread2 - Count: 5
```



[//]: # (# Todo )

[//]: # ()
[//]: # (# We will use `wait&#40;&#41;` and `notify&#40;&#41;` method to coordinate the execution.)

[//]: # ()
[//]: # (# 2. 행렬 계산기 구현. 싱글 스레드와 멀티 스레드를 활용.  )

[//]: # ()
[//]: # (# 3. 성능 비교. multi threading 이 진짜 빠른가? 빠른가의 의미는? )

[//]: # ()
[//]: # (# 소켓 프로그래밍. )

