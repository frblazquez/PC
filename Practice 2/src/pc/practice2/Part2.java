package pc.practice2;

/**
 * Practice 2 second part, own implemented mutual exclusion mechanisms.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part2 {

    private static final int M = 1000;
    private static final int N = 100;
    private static int a = 0;

    private static volatile int[] tickets = new int[2 * N];
    private static volatile boolean[] entering = new boolean[2 * N];

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

	// Protection arrays initialization
	for (int i = 0; i < 2 * N; i++) {
	    tickets[i] = 0;
	    entering[i] = false;
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
	    } catch (InterruptedException e) {
		// Interruptions are not considered
		e.printStackTrace();
	    }
	}

	// The output should be 0 (same increments and decrements)
	System.out.println(a);
    }

    public static void lock(int pid) {

	// System.out.println("Locking process " + pid);

	entering[pid] = true;
	entering = entering;

	int max = 0;
	for (int ticket : tickets)
	    max = Math.max(max, ticket);
	tickets[pid] = 1 + max;
	tickets = tickets;

	entering[pid] = false;
	entering = entering;

	for (int i = 0; i < 2 * N; i++) {
	    if (i != pid) {
		while (entering[i])
		    Thread.yield(); /* Active wait? */
		while (tickets[i] != 0 && (tickets[pid] > tickets[i] || (tickets[pid] == tickets[i] && pid > i)))
		    Thread.yield(); /* Active wait */
	    }
	}
    }

    public static void unlock(int pid) {
	// System.out.println("Unlocking process " + pid);
	tickets[pid] = 0;
	tickets = tickets;
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
		lock(code);
		a--;
		unlock(code);
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
		lock(code);
		a++;
		unlock(code);
	    }
	}
    }

}
