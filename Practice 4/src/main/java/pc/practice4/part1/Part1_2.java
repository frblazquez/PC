package pc.practice4.part1;


/**
 * Practice 4 first part, mutual exclusion mechanisms among several processes
 * using synchronized methods (monitors).
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1_2 {

    private static final int M = 5000; // Number increments/decrements
    private static final int N = 100; // Number of incrementers/decrementers
    private static int a = 0;

    public static void main(String[] args) {

	// Threads creation and initialization
	Thread[] incrementers = new Thread[N];
	Thread[] decrementers = new Thread[N];

	Part1_2 part2 = new Part1_2();

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

	private int code;

	public Decrementer(int code) {
	    this.code = code;
	}

	@Override
	public void run() {
	    for(int i = 0; i < M; i++)
		decreaseA();
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
	    for(int i = 0; i < M; i++)
		increaseA();
	}
    }

}
