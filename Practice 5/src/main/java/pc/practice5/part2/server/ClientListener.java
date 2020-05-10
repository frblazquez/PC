package pc.practice5.part2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pc.practice5.part2.messages.Message;

/**
 * This class is responsible of listening and managing all the requests received
 * from a client.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ClientListener implements Runnable {

    // TODO: Remove unnecessary attributes
    private Socket channel;
    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;

    public ClientListener(Socket socket) throws IOException {
	channel = socket;
	channel_object_in = new ObjectInputStream(channel.getInputStream());
	channel_object_out = new ObjectOutputStream(channel.getOutputStream());
    }

    @Override
    public void run() {

	while(true) {
	    try {
		Message msg = (Message) channel_object_in.readObject();
		switch (msg.getType()) 
		{
		case CONNECT:
		    System.out.println("Establishing the connection...");
		    break;
		case DISCONNECT:
		    System.out.println("Disconnecting...");
		    break;
		case GET_FILE:
		    System.out.println("Getting file requested...");
		    break;
		case GET_USERS:
		    System.out.println("Getting the users connected...");
		    break;
		default:
		    System.err.println("ERROR: Invalid request to server");
		    break;
		}
	    } catch (ClassNotFoundException | IOException e) {
		// TODO: Exceptions management
		e.printStackTrace();
	    }
	}
    }

}
