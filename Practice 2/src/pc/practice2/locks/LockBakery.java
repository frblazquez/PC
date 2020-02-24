package pc.practice2.locks;

/**
 * Bakery algorithm for several threads implementation.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class LockBakery implements MyLock {

    private final int N;
    private volatile int[] tickets;
    private volatile boolean[] entering;

    public LockBakery(int nprocesses) {
	N = nprocesses;
	tickets = new int[N];
	entering = new boolean[N];
    }

    /**
     * Each process takes a number of turn and it waits until it's turn arrives.
     * Several threads can take the same turn but at the end in the critical section
     * only one will be at the same time.
     * 
     * @param pid It's important to have a unique id per thread in [0,nthreads).
     */
    @Override
    public void lock(int pid) {
	entering[pid] = true;
	entering = entering;

	int max = 0;
	for(int ticket : tickets)
	    max = Math.max(max, ticket);
	tickets[pid] = 1 + max;

	entering[pid] = false;
	entering = entering;

	for(int i = 0; i < N; i++) {
	    if (i == pid)
		continue;
	    while(entering[i])
		Thread.yield(); // /* Active wait */; // Thread.yield();
	    while(tickets[i] != 0 && (tickets[pid] > tickets[i] || (tickets[pid] == tickets[i] && pid > i)))
		Thread.yield(); // /* Active wait */; // Thread.yield();
	}
    }

    @Override
    public void unlock(int pid) {
	tickets[pid] = 0;
	tickets = tickets;
    }

}
