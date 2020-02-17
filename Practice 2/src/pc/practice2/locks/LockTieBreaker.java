package pc.practice2.locks;

public class LockTieBreaker implements MyLock {

    private volatile int[] level;
    private volatile int[] last;
    private final int N;

    public LockTieBreaker(int nprocesses) {
	N = nprocesses;
	level = new int[N]; // Default initialization to 0
	last = new int[N - 1];// Default initialization to 0
    }


    /**
     * Thread has to go through N-1 stages. At each stage it can only advance to the
     * next one if it's sure there is at least one thread in an earlier stage.
     * 
     * @param pid It's important to have a unique id per thread in [0,nthreads).
     */
    @Override
    public void lock(int pid) {
	for(int i = 0; i < N - 1; i++) {
	    level[pid] = i;
	    last[i] = pid;

	    for(int j = 0; j < N; j++) {
		if (j == pid)
		    continue;
		while(last[i] == pid && level[j] >= i)
		    /* Active wait */;
	    }
	}
    }

    @Override
    public void unlock(int pid) {
	level[pid] = -1;
    }

}
