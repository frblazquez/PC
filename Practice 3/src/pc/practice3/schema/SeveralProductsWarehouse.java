package pc.practice3.schema;

import java.util.concurrent.Semaphore;

/**
 * This class implements an common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class SeveralProductsWarehouse implements Warehouse {

    private static final int BUS_SIZE = 100;

    private int produceIdx = 0;
    private int consumeIdx = 0;
    private Product[] products = new Product[BUS_SIZE];

    // These semaphores doesn't allow to access more prod. and consumers than the
    // space we have for them
    private Semaphore toProduct = new Semaphore(BUS_SIZE);
    private Semaphore toConsume = new Semaphore(0);

    // These semaphores protect products bus to be accesed in mutual exclusion
    private Semaphore mtxProduce = new Semaphore(1);
    private Semaphore mtxConsume = new Semaphore(1);

    @Override
    public void store(Product product) {
	try {
	    toProduct.acquire();
	    mtxProduce.acquire();
	    products[produceIdx] = product;
	    produceIdx = (produceIdx + 1) % BUS_SIZE;
	    mtxProduce.release();
	    toConsume.release();
	} catch (InterruptedException e) { // Interruptions are not considered
	    e.printStackTrace();
	}
    }

    @Override
    public Product extract() {
	Product paux = null;

	try {
	    toConsume.acquire();
	    mtxConsume.acquire();
	    paux = products[consumeIdx];
	    products[consumeIdx] = null;
	    consumeIdx = (consumeIdx + 1) % BUS_SIZE;
	    mtxConsume.release();
	    toProduct.release();
	} catch (InterruptedException e) { // Interruptions are not considered
	    e.printStackTrace();
	}

	return paux;
    }

}
