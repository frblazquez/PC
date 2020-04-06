package pc.practice4.part2.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class implements an common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Warehouse2_2 implements Warehouse {

    // Useful link:
    // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
    private static final int BUS_SIZE = 12;
    private static final Logger logger = LogManager.getLogger();

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private int produceIdx = 0;
    private int consumeIdx = 0;
    private int nInBus = 0;
    private Product[] products = new Product[BUS_SIZE];

    /*
     * Depending on where we place synchronized block (inside or outside for) we
     * have several posibilites. It's remaining to analyze the correctness of these
     * other options.
     */

    @Override
    public void store(List<Product> prs) {
	lock.lock();

	for(Product p : prs) {
	    while(nInBus == BUS_SIZE) {
		try {
		    notEmpty.signal();
		    notFull.await();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    products[produceIdx] = p;
	    produceIdx = (produceIdx + 1) % BUS_SIZE;
	    nInBus++;
	    logger.info("Produced " + p);
	    logger.debug("In the buffer: " + nInBus);

	}
	notEmpty.signal();
	lock.unlock();
    }

    @Override
    public synchronized List<Product> extract(int n) {
	List<Product> paux = new ArrayList<>();
	
	lock.lock();

	for(int i = 0; i < n; i++) {
	    while(nInBus == 0) {
		try {
		    notFull.signal();
		    notEmpty.await();
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
	}

	notFull.signal();
	lock.unlock();

	return paux;
    }

}
