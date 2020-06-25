package pc.practice2.locks;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicketMax implements MyLock {
    private final int MAX;
    private volatile AtomicInteger nextTicket;
    private volatile AtomicInteger actualTicket;
    private volatile int[] turn;

    public LockTicketMax(int MAX) {
	this.MAX = MAX;
	turn = new int[MAX];
	nextTicket = new AtomicInteger(0);
	actualTicket = new AtomicInteger(0);

	for(int i = 0; i < MAX; i++)
	    turn[i] = -1;
    }


    /**
     * Each process takes an unique number of turn and it waits until it's turn
     * arrives. If the number gets greater than max it's set to the fist value.
     * 
     * @param pid It's important to have a unique id per thread in [0,nthreads).
     */
    @Override
    public void lock(int pid) {
	// LO MEJOR SERÍA USAR GET AND UPDATE CON UNA FUNCIÓN DE UPDATE DADA QUE FUERA EL INCREMENTO MÓDULO MAX
	nextTicket.compareAndSet(MAX, 0);
	turn[pid] = nextTicket.getAndAdd(1);
	nextTicket.compareAndSet(MAX, 0);

	while(turn[pid] != actualTicket.get())
	    /* Active wait */;
    }

    @Override
    public void unlock(int pid) {
	turn[pid] = -1;

	// LO MEJOR SERÍA USAR GET AND UPDATE CON UNA FUNCIÓN DE UPDATE DADA QUE FUERA EL INCREMENTO MÓDULO MAX
	actualTicket.compareAndSet(MAX, 0);
	actualTicket.getAndAdd(1);
	actualTicket.compareAndSet(MAX, 0);
    }

}
