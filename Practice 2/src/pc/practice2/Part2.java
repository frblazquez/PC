package pc.practice2;

import pc.practice2.locks.LockBakery;
import pc.practice2.locks.MyLock;

/**
 * Practice 2 second part, own implemented mutual exclusion mechanisms.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part2 {

    private static final int M = 500; // Number increments/decrements
    private static final int N = 10; // Number of incrementers/decrementers

    private static MyLock mtx = new LockBakery(2 * N);
    private static volatile int a = 0;

    public static void main(String[] args) {

	// Threads creation and initialization
	Thread[] incrementers = new Thread[N];
	Thread[] decrementers = new Thread[N];

	Part2 part2 = new Part2();

	for (int i = 0; i < N; i++) {
	    Decrementer dc = part2.new Decrementer(i);
	    Incrementer ic = part2.new Incrementer(N + i);

	    decrementers[i] = new Thread(dc);
	    incrementers[i] = new Thread(ic);
	}

	// Threads execution start
	for (int i = 0; i < N; i++) {
	    decrementers[i].start();
	    incrementers[i].start();
	}

	// Threads ending execution
	for (int i = 0; i < N; i++) {
	    try {
		decrementers[i].join();
		incrementers[i].join();
	    } catch (InterruptedException e) { // Interruptions are not considered
		e.printStackTrace();
	    }
	}

	// The output should be 0 (same increments and decrements)
	System.out.println(a);
    }

    /**
     * Decrements M units the global variable a.
     */
    class Decrementer implements Runnable {

	private int code;

	public Decrementer(int code) {
	    this.code = code;
	}

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) {
		mtx.lock(code);
		a--;
		mtx.unlock(code);
	    }
	}
    }

    /**
     * Increments M units the global variable a.
     */
    class Incrementer implements Runnable {

	private int code;

	public Incrementer(int code) {
	    this.code = code;
	}

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) {
		mtx.lock(code);
		a++;
		mtx.unlock(code);
	    }
	}
    }

}
