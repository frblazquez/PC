package pc.practice3;

import java.util.concurrent.Semaphore;

/**
 * Practice 3 first part, mutual exclusion with a semaphore.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1 {

    private static final int M = 10000;
    private static int a = 0;
    private static Semaphore mtx = new Semaphore(1);

    public static void main(String[] args) {

	// Threads creation and initialization
	Part1 part1 = new Part1();
	Decrementer dc = part1.new Decrementer();
	Incrementer ic = part1.new Incrementer();

	Thread incrementer = new Thread(dc);
	Thread decrementer = new Thread(ic);

	// Threads execution start
	incrementer.start();
	decrementer.start();

	// Threads ending execution
	try {
	    decrementer.join();
	    incrementer.join();
	} catch (InterruptedException e) { // Interruptions are not considered
	    e.printStackTrace();
	}

	// The output should be 0 (same increments and decrements)
	System.out.println(a);
    }


    /**
     * Decrements M units the global variable a.
     */
    class Decrementer implements Runnable {

	@Override
	public void run() {
	    for(int i = 0; i < M; i++) {
		try {
		    mtx.acquire();
		    a--;
		    mtx.release();
		} catch (InterruptedException e) { // Interruptions are not considered
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Increments M units the global variable a.
     */
    class Incrementer implements Runnable {

	@Override
	public void run() {
	    for(int i = 0; i < M; i++) {
		try {
		    mtx.acquire();
		    a++;
		    mtx.release();
		} catch (InterruptedException e) { // Interruptions are not considered
		    e.printStackTrace();
		}
	    }
	}
    }

}
