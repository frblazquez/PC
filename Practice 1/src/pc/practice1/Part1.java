package pc.practice1;

/**
 * Practice 1 first part, thread creation and basic synchronization.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1 {

	/**
	 * @param args First number of threads. Second threads delay.
	 */
	public static void main(String args[]) {

		int nThreads = Integer.parseInt(args[0]);
		long sleepTime = Long.parseLong(args[1]);

		Thread[] threads = new Thread[nThreads];
		DummyWorker dmth = new DummyWorker(sleepTime);

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new Thread(dmth);
			threads[i].start();
		}

		for (Thread th : threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				// Interruptions are not considered
				e.printStackTrace();
			}
		}

		System.out.println("Execution finished");
	}
}

/**
 * Class for creating a thread which prints its ID, sleeps and prints it again.
 */
class DummyWorker implements Runnable {

	private long sleepingTime;

	public DummyWorker(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId());

		try {
			Thread.sleep(sleepingTime);
		} catch (InterruptedException e) {
			// Interruptions are not considered
			e.printStackTrace();
		}

		System.out.println(Thread.currentThread().getId());
	}
}
