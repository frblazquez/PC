package pc.practice4.part2.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Producer implements Runnable {

    private final int MAX_TO_PRODUCE;
    private int pid;
    private Warehouse wh;
    private int idx = 0;

    public Producer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
	this.MAX_TO_PRODUCE = 10;
    }

    public Producer(int pid, Warehouse wh, int max_production) {
	this.pid = pid;
	this.wh = wh;
	this.MAX_TO_PRODUCE = max_production;
    }

    @Override
    public void run() {
	List<Product> produced = new ArrayList<Product>();
	int nProduced;

	while(true) {
	    produced.clear();
	    nProduced = 1 + ThreadLocalRandom.current().nextInt(0, MAX_TO_PRODUCE);

	    for(int i = 0; i < nProduced; i++) {
		produced.add(new IntProduct(pid * 10000 + idx));
		idx++;
	    }

	    wh.store(produced);
	    
	    //@formatter:off
	    try {Thread.sleep(200);}
	    catch (InterruptedException e) { e.printStackTrace();}
	}
    }

}
