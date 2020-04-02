package pc.practice4.part2.schema;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class implements an common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class SeveralProductsWarehouse implements Warehouse {

    private static final int BUS_SIZE = 20;
    private static final Logger logger = LogManager.getLogger();

    private int produceIdx = 0;
    private int consumeIdx = 0;
    private int nInBus = 0;
    private Product[] products = new Product[BUS_SIZE];


    @Override
    public void store(List<Product> prs) {
	synchronized (this) {
	    for(Product p : prs) {
		while(nInBus == BUS_SIZE) {
		    try {
			this.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		products[produceIdx] = p;
		produceIdx = (produceIdx + 1) % BUS_SIZE;
		nInBus++;
		logger.info("Produced " + p);
		logger.debug("In the buffer: " + nInBus);
		notifyAll();
	    }
	}
    }

    @Override
    public synchronized List<Product> extract(int n) {
	List<Product> paux = new ArrayList<>();

	synchronized (this) {
	    for(int i = 0; i < n; i++) {
		while(nInBus == 0) {
		    try {
			wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		paux.add(products[consumeIdx]);
		logger.info("Consumed " + products[consumeIdx]);
		products[consumeIdx] = null;
		consumeIdx = (consumeIdx + 1) % BUS_SIZE;
		nInBus--;
		logger.debug("In the buffer: " + nInBus);
		notifyAll();
	    }
	}

	return paux;
    }

}
