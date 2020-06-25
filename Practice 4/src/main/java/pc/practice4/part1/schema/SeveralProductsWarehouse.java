package pc.practice4.part1.schema;

/**
 * This class implements an common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class SeveralProductsWarehouse implements Warehouse {

    private static final int BUS_SIZE = 10;

    private int produceIdx = 0;
    private int consumeIdx = 0;
    private int nInBus = 0;
    private volatile Product[] products = new Product[BUS_SIZE];


    @Override
    public synchronized void store(Product product) {
	while(nInBus == BUS_SIZE)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }
	
	products[produceIdx] = product;
	produceIdx = (produceIdx + 1) % BUS_SIZE;
	nInBus++;

	notifyAll();
    }

    @Override
    public synchronized Product extract() {
	while(nInBus == 0)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }

	Product paux = null;
	paux = products[consumeIdx];
	products[consumeIdx] = null;
	products = products;
	consumeIdx = (consumeIdx + 1) % BUS_SIZE;
	nInBus--;

	notifyAll();
	return paux;
    }

}
