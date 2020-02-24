package pc.practice2.locks;

/**
 * Ticket lock algorithm for several threads implementation.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class LockTicket implements MyLock {

    private final int N;
    private volatile int nextTicket;
    private volatile int actualTicket;
    private volatile int[] turn;

    public LockTicket(int nprocesses) {
	N = nprocesses;
	turn = new int[N];
	nextTicket = 1;
	actualTicket = 1;
    }

    /**
     * Each process takes an unique number of turn and it waits until it's turn
     * arrives.
     * 
     * @param pid It's important to have a unique id per thread in [0,nthreads).
     */
    @Override
    public void lock(int pid) {
	turn[pid] = fetch_and_add_ticket();
	while(turn[pid] != actualTicket)
	    /* Active wait */;
    }

    @Override
    public void unlock(int pid) {
	fetch_and_add_number();
    }

    // These methods have to be synchronized for being executed in mutual exclusion
    private synchronized int fetch_and_add_ticket() {
	nextTicket++;
	return nextTicket - 1;
    }

    private synchronized int fetch_and_add_number() {
	actualTicket++;
	return actualTicket - 1;
    }
}
