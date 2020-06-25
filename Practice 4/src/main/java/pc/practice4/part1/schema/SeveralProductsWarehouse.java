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

    // Variables de control de estado
    boolean produciendo;
    int nConsumidoresConsumiendo = 0;


    @Override
    public void store(Product product) {
	request_produce();
	products.add(product);
	release_produce();
    }

    @Override
    public Product extract(int idx) {
	request_consume();
	Product paux = products.get(idx);
	release_consume();
	return paux;
    }

    private synchronized void request_produce() {
	// A diferencia del ejercicio anterior, donde nosotros controlábamos el mutex y
	// hacíamos paso de testigo, aquí si que tenemos que controlar si hay algún
	// productorproduciendo (segunda condición while)
	while(nConsumidoresConsumiendo > 0 || produciendo)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }
	produciendo = true;
    }

    private synchronized void release_produce() {
	produciendo = false;
	
	// Los productores notifican una vez han terminado a consumidores y productores en espera.
	// (En el ejercicio anterior se daba preferencia a los productores en este punto)
	notifyAll();
    }

    private synchronized void request_consume() {
	while(produciendo)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }

	nConsumidoresConsumiendo++;
    }

    private synchronized void release_consume() {
	nConsumidoresConsumiendo--;
	
	// Los consumidores no notifican mientras haya más consumidores
	if(nConsumidoresConsumiendo == 0)
	    notifyAll();
    }
}
