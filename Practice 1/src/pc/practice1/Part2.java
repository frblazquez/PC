package pc.practice1;

/**
 * Practice 1 second part, check inconsistencies of unprotected multithreading.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part2 {

	private static final int M = 100;
	private static final int N = 10;
	private static int a = 0;

	public static void main(String[] args) {
		Thread[] incrementers = new Thread[N];
		Thread[] decrementers = new Thread[N];
		
		Decrementer dc = new Part2().new Decrementer();
		Incrementer ic = new Part2().new Incrementer();

		for(int i = 0; i<N; i++) {
			decrementers[i] = new Thread(dc);
			incrementers[i] = new Thread(ic);
		}

		for (int i = 0; i < N; i++) {
			decrementers[i].start();
			incrementers[i].start();
		}

		for (int i = 0; i < N; i++) {
			try {
				decrementers[i].join();
				incrementers[i].join();
			} catch (InterruptedException e) {
				// Interruptions are not considered
				e.printStackTrace();
			}
		}

		System.out.println(a);
	}

	/**
	 * Decrements M units the global variable a.
	 */
	class Decrementer implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < M; i++)
				a--;
		}
	}

	/**
	 * Increments M units the global variable a.
	 */
	class Incrementer implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < M; i++)
				a++;
		}
	}

}
