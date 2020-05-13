package pc.practice5.part2.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class is responsible of listening and managing all the messages received
 * from the server.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ServerListener implements Runnable {

    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;

    public ServerListener(ObjectInputStream i, ObjectOutputStream o) {
	this.channel_object_in = i;
	this.channel_object_out = o;
    }

    @Override
    public void run() {

	while(true) {
	    try {
		System.out.println("ServerListener goes to sleep!");
		Thread.sleep(10000);
		System.out.println("ServerListener wakes up!");
		Thread.sleep(10000);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
