package pc.practice4.part2.schema;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Consumer implements Runnable {

    // TODO: Replace with a public constant in a proper place
    private static final int MAX_TO_CONSUME = 10;
    private static final Logger logger = LogManager.getLogger();

    private int pid;
    private Warehouse wh;

    public Consumer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
    }

    @Override
    public void run() {
	List<Product> consumed;
	int nConsumed;
	
	while(true) {
	    nConsumed = 1 + ThreadLocalRandom.current().nextInt(0, MAX_TO_CONSUME);
	    consumed = wh.extract(nConsumed);

	    try {
		Thread.sleep(200);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
