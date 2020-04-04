package pc.practice4.part2.schema;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Consumer implements Runnable {

    private final int MAX_TO_CONSUME;
    private int pid;
    private Warehouse wh;

    public Consumer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
	this.MAX_TO_CONSUME = 10;
    }

    public Consumer(int pid, Warehouse wh, int max_consumption) {
	this.pid = pid;
	this.wh = wh;
	this.MAX_TO_CONSUME = max_consumption;
    }

    @Override
    public void run() {
	List<Product> consumed;
	int nConsumed;
	
	while(true) {
	    nConsumed = 1 + ThreadLocalRandom.current().nextInt(0, MAX_TO_CONSUME);
	    consumed = wh.extract(nConsumed);

	    //@formatter:off
	    try { Thread.sleep(200); }
	    catch (InterruptedException e) { e.printStackTrace(); }
	}
    }

}
