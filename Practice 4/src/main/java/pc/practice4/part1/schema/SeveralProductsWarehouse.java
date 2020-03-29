package pc.practice4.part1.schema;

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


    @Override
    public synchronized void store(Product product) {
	products[produceIdx] = product;
	produceIdx = (produceIdx + 1) % BUS_SIZE;
    }

    @Override
    public synchronized Product extract() {
	Product paux = null;
	paux = products[consumeIdx];
	products[consumeIdx] = null;
	consumeIdx = (consumeIdx + 1) % BUS_SIZE;

	return paux;
    }

}
