package pc.practice4.part1.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements an common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class SeveralProductsWarehouse implements Warehouse {

    // El bus ahora es infinito
    private List<Product> products = new ArrayList<>();
    private MonitorAlmacen controller = new MonitorAlmacen();
    
    @Override
    public void store(Product product) {
	controller.request_produce();
	products.add(product);
	controller.release_produce();
    }

    @Override
    public Product extract(int idx) {
	controller.request_consume();
	Product paux = products.get(idx);
	controller.release_consume();
	return paux;
    }

}
