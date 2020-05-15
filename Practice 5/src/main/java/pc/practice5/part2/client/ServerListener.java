package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.MessageType;

/**
 * This class is responsible of listening and managing all the messages received
 * from the server.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ServerListener implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Client client;
    private Socket channel;
    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;

    // TODO: Remove when possible
    public ServerListener(Client client, Socket channel) throws IOException {
	this.client = client;
	this.channel = channel;
	this.channel_object_in = new ObjectInputStream(channel.getInputStream());
	this.channel_object_out = new ObjectOutputStream(channel.getOutputStream());
    }

    public ServerListener(Client client, Socket channel, ObjectInputStream i, ObjectOutputStream o) throws IOException {
	this.client = client;
	this.channel = channel;
	this.channel_object_in = i;
	this.channel_object_out = o;
    }

    private void disconnectAnswer() throws IOException {
	channel.close();
	channel_object_in.close();
	channel_object_out.close();
	logger.info("Client " + client.getId() + " disconnected");
    }

    private void getUsersAnswer() throws ClassNotFoundException, IOException {
	@SuppressWarnings("unchecked")
	List<String> users = (ArrayList<String>) channel_object_in.readObject();
	for(String usr : users)
	    System.out.println(usr);
	logger.debug("Users successfully retrieved...");
    }


    @Override
    public void run() {

	try {
	    MessageType type = (MessageType) channel_object_in.readObject();
	    while(type != MessageType.DISCONNECTED) {
		switch (type) {
		case RECEIVE_USERS: getUsersAnswer(); break;
		default: logger.error("Unknown option");
		}
		type = (MessageType) channel_object_in.readObject();
	    }
	    disconnectAnswer();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Listener for client " + client.getId() + " finished abruptly");
	}
    }

}
