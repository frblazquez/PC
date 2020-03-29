package pc.practice4.part1;

import pc.practice4.part1.schema.Consumer;
import pc.practice4.part1.schema.Producer;
import pc.practice4.part1.schema.SeveralProductsWarehouse;
import pc.practice4.part1.schema.Warehouse;

/**
 * Practice 4 first part, Producers-Consumers problem using monitors with bus
 * size space for several elements.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part1_3 {

    private static final int N_PROD = 10;
    private static final int N_CONS = 10;
    private static Warehouse wh = new SeveralProductsWarehouse();

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
