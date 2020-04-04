package pc.practice4.part2;

import pc.practice4.part2.schema.Consumer;
import pc.practice4.part2.schema.Producer;
import pc.practice4.part2.schema.Warehouse2_1;
import pc.practice4.part2.schema.Warehouse;

/**
 * Practice 4 first part, Producers-Consumers problem using monitors with bus
 * size space for several elements.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part2_1 {

    private static final int N_PROD = 10;
    private static final int N_CONS = 10;
    private static final int MAX_TO_PRODUCE = 15;
    private static final int MAX_TO_CONSUME = 10;
    private static Warehouse wh = new Warehouse2_1();

    public static void main(String[] args) {

	// Threads creation and initialization
	Thread[] producers = new Thread[N_PROD];
	Thread[] consumers = new Thread[N_CONS];

	for(int i = 0; i < N_PROD; i++)
	    producers[i] = new Thread(new Producer(i, wh, MAX_TO_PRODUCE));

	for(int i = 0; i < N_CONS; i++)
	    consumers[i] = new Thread(new Consumer(i, wh, MAX_TO_CONSUME));

	// Threads execution start
	for(int i = 0; i < N_PROD; i++)
	    producers[i].start();

	for(int i = 0; i < N_CONS; i++)
	    consumers[i].start();

    }
}
