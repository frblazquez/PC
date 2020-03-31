package pc.practice4.part2.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Producer implements Runnable {

    // TODO: Replace with a public constant in a proper place
    private static final int MAX_TO_PRODUCE = 10;
    private static final Logger logger = LogManager.getLogger();

    private int pid;
    private Warehouse wh;
    private int idx = 0;

    public Producer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
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

	    try {
		Thread.sleep(200);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
