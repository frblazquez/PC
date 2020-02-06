package pc.practice2;

/**
 * Practice 2 first part, Peterson algorithm, mutual exclusion mechanisms among
 * two processes.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1 {

    private static final int M = 10000;

    private static int a = 0;
    private volatile static int turn = 1;
    private volatile static boolean[] ctrl = {false,false};

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
     * Peterson mutual exclusion algorithm among two processes.
     * 
     * @param id 0 for decrementer, 1 for incrementer process.
     */
    public static void lock(int id) {
	ctrl[id] = true;
	turn = 1 - id;
	while (ctrl[1 - id] && turn == 1 - id)
	    /* Active wait */;
    }

    public static void unlock(int id) {
	ctrl[id] = false;
    }

    /**
     * Decrements M units the global variable a.
     */
    class Decrementer implements Runnable {

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) {
		lock(0);
		a--;
		unlock(0);
	    }
	}
    }

    /**
     * Increments M units the global variable a.
     */
    class Incrementer implements Runnable {

	@Override
	public void run() {
	    for (int i = 0; i < M; i++) {
		lock(1);
		a++;
		unlock(1);
	    }
	}
    }

}
