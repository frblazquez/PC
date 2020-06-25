package pc.practice3.schema;

import java.util.concurrent.Semaphore;

/**
 * This class implements an common interface for the communication of producers
 * and consumers in the Producers-Consumers problem with only a space available.
 * 
 * @author Francisco Javier Blázquez Martínez, Miguel Franqueira Varela
 */
public class OneProductWarehouse implements Warehouse {

    private Product product;
    private Semaphore toProduct = new Semaphore(1);
    private Semaphore toConsume = new Semaphore(0);

    @Override
    public void store(Product product) {
	try {
	    toProduct.acquire();
	} catch (InterruptedException e) { // Interruptions are not considered
	    e.printStackTrace();
	}
	this.product = product;
	toConsume.release();
    }

    @Override
    public Product extract() {
	try {
	    toConsume.acquire();
	} catch (InterruptedException e) { // Interruptions are not considered
	    e.printStackTrace();
	}
	Product paux = product;
	product = null;
	toProduct.release();

	return paux;
    }

}
