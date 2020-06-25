package pc.practice3.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * This class implements a common interface for both producers and consumers in
 * Producer-Consumers problem with a bus for several products.
 * 
 * @author Francisco Javier Blázquez Martínez, Miguel Franqueira Varela
 */
public class SeveralProductsWarehouse implements Warehouse {

    // El bus ahora es infinito
    private List<Product> products = new ArrayList<>();

    boolean produciendo;
    int nProductoresEsperando = 0;
    int nConsumidoresEsperando = 0;
    int nConsumidoresConsumiendo = 0;

    // Colas para productores y consumidores
    private Semaphore consumersQueue = new Semaphore(0);
    private Semaphore producersQueue = new Semaphore(0);

    // Mutex, controla el acceso a las variables:
    // + produciendo
    // + nProductoresEsperando
    // + nConsumidoresEsperando
    // + nConsumidoresConsumiendo
    private Semaphore mtx = new Semaphore(1);

    @Override
    public void store(Product product) {

	try {
	    mtx.acquire();
	    // No es necesario comprobar si alguón productor está produciendo porque de ser
	    // el caso este tendría la propiedad del mutex y no puede haber dos dentro de mtx.aquire()!
	    while(nConsumidoresConsumiendo > 0) {
		nProductoresEsperando++;
		mtx.release();
		producersQueue.acquire();
		// No necesario por paso de testigo!
		// mtx.acquire();
		nProductoresEsperando--;
	    }

	    produciendo = true;
	    products.add(product);

	    if (nProductoresEsperando > 0)	// Quedan productores
		producersQueue.release();	// PASO DE TESTIGO!
	    else {				// No quedan productores, despertamos a los consumidores
		produciendo = false;

		for(int i = 0; i < nConsumidoresEsperando; i++)
		    consumersQueue.release();

		mtx.release();			// Importante liberarlo después de despertar a  los consumidores en espera
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public Product extract(int idx) {
	
	Product paux = null;

	try {
	    mtx.acquire();			// Para consultar/modificar las variables compartidas
	    while(produciendo) {
		nConsumidoresEsperando++;
		mtx.release();
		consumersQueue.acquire();
		mtx.acquire();			// Para actualizar el estado antes de consumir
		nConsumidoresEsperando--;
	    }

	    nConsumidoresConsumiendo++;		// Bloqueo a los productores
	    mtx.release();

	    // Fuera de la sección crítica
	    paux = products.get(idx);

	    mtx.acquire();
	    nConsumidoresConsumiendo--; 	// Actualización del estado (posible desbloqueo a productores)

	    if (nConsumidoresConsumiendo == 0) {
		for(int i = 0; i < nProductoresEsperando; i++)
		    producersQueue.release();
	    }

	    mtx.release();

	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	}

	return paux;
    }

}
