package pc.practice4.part1;

/**
 * Practice 4 first part, mutual exclusion mechanisms among two processes using
 * synchronized methods (monitors).
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1_1 {

    private static final int M = 100000;
    private static int a = 0;

    public static void main(String[] args) {

	// Threads creation and initialization
	Part1_1 part1 = new Part1_1();
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

    public static synchronized void increaseA() {
	a++;
    }

    public static synchronized void decreaseA() {
	a--;
    }


    /**
     * Decrements M units the global variable a.
     */
    class Decrementer implements Runnable {

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) 
		decreaseA();
	}
    }

    /**
     * Increments M units the global variable a.
     */
    class Incrementer implements Runnable {

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) 
		increaseA();
	}
    }

}
