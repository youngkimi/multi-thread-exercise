## Multithreading Exercise

Our goal of this exercise is to create a real-time chat program using TCP(or UDP).  

### 1. Simple Counter

Let's start by making a simple counter with `Runnable`.

The `Runnable` interface represents a task that can be executed by a thread. By implementing this interface, you can define the process to be handled and pass it to a thread for concurrent execution. 

Here is basic example to illustrate how to use `Runnable` and `Thread` :

`SimpleNumberCounter` counts `counterLimit` times, starting from `stt`.

```java
package counter.one_simiple;

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

        for (int i = 0; i < countLimit; i++) {
            System.out.println(counterName + " - Count: " + (stt + i));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(counterName + " interrupted.");
            }
        }
    }
}
```

```java
package counter.one_simiple;

public class SimpleCounterExample {

	public static void main(String[] args) {

		int countLimit = 5;

		try {
			Runnable counter1 = new SimpleNumberCounter("Thread 1", 101, countLimit);
			Runnable counter2 = new SimpleNumberCounter("Thread 2", 201, countLimit);
			Runnable counter3 = new SimpleNumberCounter("Thread 3", 301, countLimit);

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
}
```

Here is the result. 

```
Thread 2 - Count: 201
Thread 1 - Count: 101
Thread 3 - Count: 301
Thread 1 - Count: 102
Thread 2 - Count: 202
Thread 3 - Count: 302
Thread 1 - Count: 103
Thread 2 - Count: 203
Thread 3 - Count: 303
Thread 1 - Count: 104
Thread 2 - Count: 204
Thread 3 - Count: 304
Thread 1 - Count: 105
Thread 2 - Count: 205
Thread 3 - Count: 305
```

As you can check, threads seem to work concurrently.
But sometimes `Thread2` finishes its job faster than `Thread1`. The message that `Thread1` has finished its job might show up either after or before `Thread2` finished counting.

Let's raise the bar. Due to certain business conditions, `Thread1` has priority over `Thread2` for each count. `Thread2` can only count after `Thread1` has completed its count. How can we achieve that? The Key is `Lock`. ~~sounds like a punchline~~

### 2. Counter Using Lock (spinlock)

Imagine a `Group Therapy` session. 

Take a ball, talk about what's on your mind, pass the ball, and your turn is over. Now listen to what the next person has to say. You can talk when you get the ball again.

This is what we're going to do.  

Take a lock, do what you're supposed to do, pass the lock, and your turn is over. Now wait till someone hand the lock back to you again. 

We added a `turn` and a `trigger`. The `trigger` is a specific number assigned when the thread is created. The thread compares the `trigger` to the `turn`.
After the task is finished, we increment the `turn`. If the `turn` equals the threadSize, we reset it to zero.

```java
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

```

```java
package counter.one_simiple;

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

It seems fine, right? But... is it? 

### 3. volatile

```java
package counter.one_volatile;

import static counter.one_volatile.VolatileCounterExample.*;

public class VolatileNumberCounter implements Runnable {
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
		methodCalled++;
	}

	private void passTheBall() {
		turn ++;
		if (turn == threadSize) {
			turn = 0;
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
Thread size: 3, counterLimit: 1000
Thread called: 3000
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
    };
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

```
Thread size: 3, counterLimit: 100
Thread called: 300
```

But increasing `countLimit` can lead to the same problems: infinite loops.
Or when the job is completed, but the results may not be as expected.
Let's move on to the next chapter.

### 5. synchronized

The reason the code isn't working is that the `lock` value might be lost or overwritten during the task.
To address this, we added `volatile` to the shared variables so that each thread can see the up-to-date values.
However, if the `lock` value is greater than 0, multiple threads might mistakenly believe they can acquire it, resulting in more threads acquiring locks than are actually available.
Additionally, when threads update the `lock` value, it might be overwritten due to simultaneous access.
Therefore, we need to ensure that access to the shared variables is restricted to only one thread at a time. In java, we can use `synchronized`.

Then, can we solve every `synchronization` problem with `volatile` and `synchronized`? Unfortunately, we're not.

```java
package counter.five_synchronized;

import static counter.five_synchronized.SynchronizedCounterExample.lockCount;
import static counter.five_synchronized.SynchronizedCounterExample.threadCalled;

public class SynchronizedNumberCounter implements Runnable {
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
            acquireLock();
            criticalSection();
            releaseLock();
        }
    }

    private synchronized void acquireLock() {
        while (--lockCount < 0) {
            lockCount++;
        }
    }

    private synchronized void criticalSection() {
        threadCalled++;
    }

    private synchronized void releaseLock() {
        lockCount ++;
    }
}
```

I refactored like this. All shared variables are declared as `volatile`, and methods modifying the shared variables are declared as `synchronized`.

```
Thread size: 8, counterLimit: 100
Atomic Thread called: 800
```

It works fine with 3 digits, but if you increase the digit, same problem we've seen before occurs. 

### 6. monitor lock




### AtomicType




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

Now all threads are executed in sequence, with each thread counting exactly once before the next thread begins its count. It's time to raise the hurdle. 

What if I change the task from simply printing out to something else?  

```java

```

It seems that every result of each equation should be true because `lock` and `methodCalled` are incremented consecutively. But, as you might observe, the results don't match the expectations.

```
number of threads - 6, count limit - 1000000
Final lock Value: 6000000
Final methodCalled Value: 5994124
```


```
number of threads - 1000000, count limit - 1000000
Final lock Value: 6000000
Final methodCalled Value: 6000000
```

The occurrence of these errors indicates that the value of `lock` was different from the value of `methodCalled` at the comparison. Even though `volatile` ensures visibility of the variable among threads, such errors can occur because multiple thread may try to modify the shared variable simultaneously.
We need to separate the section modifying shared variables, and ensure that only one thread the section can access to this section at a time. This section is called the `Critical Section`  

# Atomic


# wait() and modify()


# 할일 목록.

# We will use `wait()` and `notify()` method to coordinate the execution.

# 1. wait(), notify() 를 이용해서. synchronized 를 활용하지 않고. 활용하고.
#   - mutex의 문제. 락을 누가 점유하는 문제. 

# 2. 행렬 계산기 구현. 싱글 스레드와 멀티 스레드를 활용.  
# 3. 성능 비교. multi threading 이 진짜 빠른가? 빠른가의 의미는? 

# 소켓 프로그래밍. 


# multi-thread-exercise-
