package pc.practice3;

import pc.practice3.schema.Consumer;
import pc.practice3.schema.OneProductWarehouse;
import pc.practice3.schema.Producer;
import pc.practice3.schema.Warehouse;

/**
 * Producers-Consumers problem with semaphores with bus size of a single
 * element.
 * 
 * @author Francisco Javier Blázquez Martínez, Miguel Franqueira Varela
 */
public class Part2 {

    private static final int N_PROD = 10;
    private static final int N_CONS = 10;
    private static Warehouse wh = new OneProductWarehouse();

    public static void main(String[] args) {

	// Threads creation and initialization
	Thread[] producers = new Thread[N_PROD];
	Thread[] consumers = new Thread[N_CONS];

	for(int i = 0; i < N_PROD; i++)
	    producers[i] = new Thread(new Producer(i, wh));

	for(int i = 0; i < N_CONS; i++)
	    consumers[i] = new Thread(new Consumer(i, wh));

	// Threads execution start
	for(int i = 0; i < N_PROD; i++)
	    producers[i].start();

	for(int i = 0; i < N_CONS; i++)
	    consumers[i].start();

    }
}
