package pc.practice5.part2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pc.practice5.part2.client.User;
import pc.practice5.part2.messages.MessageType;

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
		MessageType type = (MessageType) channel_object_in.readObject();
		switch (type) 
		{
		case CONNECT: // TODO: This shouldn't be inside switch but before
		    System.out.println("Establishing the connection...");
		    User user = (User) channel_object_in.readObject();
		    Server.addUser(user);
		    System.out.println("Connection established");
		    break;
		case DISCONNECT:
		    System.out.println("Closing connection with user...");
		    String userId = (String) channel_object_in.readObject();
		    Server.removeUser(userId);
		    System.out.println("User " + userId + " disconnected");
		    break;
		case GET_FILE:
		    System.out.println("Getting file requested...");
		    break;
		case GET_USERS:
		    System.out.println("Getting the users connected...");
		    // TODO: It is a bad idea sending the whole array!
		    channel_object_out.writeObject(Server.getAllUsers());
		    channel_object_out.flush();
		    System.out.println("Users connected sent to client");
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
