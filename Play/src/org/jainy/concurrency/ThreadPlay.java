package org.jainy.concurrency;

/**
 * 
 * This class is using the relay-race algorithm, where every runner (thread)
 * passes the baton (control) to next runner (thread) in line and then waits for
 * the baton (control) to come back to it.
 * 
 * This has been achieved by creating a mutex per thread and then every thread
 * makes sure to take control of it's own mutex and the next in lines thread
 * mutex before processing. Once processing is done, it notifies on the mutex of
 * next in line thread so that it can go ahead and then notifies on it's own
 * mutex so that the thread which has to notify current thread can go ahead and
 * then goes into wait state
 * 
 * Flag <code>started</code> has been used to make sure that all thread get a
 * correct kick-off initially
 * 
 * @author jainy
 * 
 */
public class ThreadPlay {

	static int numbers = 150;
	static int threadCount = 12;
	static volatile int n = 0;
	static volatile boolean started;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		for (int i = 1; i <= threadCount; i++) {
			started = false;
			Thread t = new Thread(new MyThread(i));
			t.start();
			while (!started) {
				// Wait
			}
		}

	}

	static class MyThread implements Runnable {

		private int threadId;
		private Object myMutex;
		private Object notifyMutex;

		MyThread(int threadId) {
			this.threadId = threadId;
			myMutex = MutexFactory.getMyMutex(this.threadId);
			notifyMutex = MutexFactory.getNotifyMutex(this.threadId);
		}

		@Override
		public void run() {
			while (n < numbers) {

				synchronized (myMutex) {
					synchronized (notifyMutex) {

						System.out.print("Thread" + threadId + ": " + (++n));

						if (threadId == threadCount) {
							System.out.println();
						} else {
							System.out.print("\t");
						}

						notifyMutex.notify();
					}
					myMutex.notify();
					started = true;
					try {
						myMutex.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}

			synchronized (notifyMutex) {

				notifyMutex.notify();
			}

		}

	}

	static class MutexFactory {

		static Object[] mutexArr = new Object[threadCount];

		static {
			for (int i = 0; i < mutexArr.length; i++) {
				mutexArr[i] = new Object();
			}
		}

		static Object getMyMutex(int threadId) {
			return mutexArr[threadId - 1];
		}

		static Object getNotifyMutex(int threadId) {
			if (threadId == threadCount) {
				return mutexArr[0];
			}
			return mutexArr[threadId];
		}
	}


}
