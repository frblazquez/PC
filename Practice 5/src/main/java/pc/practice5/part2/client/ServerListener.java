package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	System.out.println("El usuario " + client.getId() + " se ha desconectado correctamente");
	logger.info("User " + client.getId() + " disconnect confirmation received");
    }

    private void getUsersAnswer() throws ClassNotFoundException, IOException {
	@SuppressWarnings("unchecked")
	List<String> users = (ArrayList<String>) channel_object_in.readObject();
	System.out.println("User " + client.getId() + " get-users answer:");
	for(String usr : users)
	    System.out.println(usr);
	logger.debug("Users connected successfully retrieved by user " + client.getId());
    }

    private void nonExistentFileAnswer() throws ClassNotFoundException, IOException {
	String fileName = (String) channel_object_in.readObject();
	System.out.println("Ningún usuario tiene el fichero \"" + fileName + "\" solicitado");
	logger.warn("No client connected has the file \"" + fileName + "\" requested by user " + client.getId());
    }

    private void shareFileAnswer() throws ClassNotFoundException, IOException {
	String fileName = (String) channel_object_in.readObject();
	String destinationUser = (String) channel_object_in.readObject();
	
	// Get an available port (can be improved)
	Random rand = new Random();
	int portNumber = 4747;
	boolean portAvailable = false;

	while(!portAvailable) {
	    portNumber = 1000 + rand.nextInt(9000);
	    try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
		portAvailable = true;
	    } catch (Exception e) {}
	}

	// Create a file sender
	(new Thread(new FileSender(fileName, client.getId(), portNumber))).start();

	// IMPORTANT! Notify the server that the sender is ready
	// Communication from client side to server side is done via Client, not via
	// ServerListener in order to protect the data streams from concurrency!
	client.notifySenderReady(fileName, destinationUser, portNumber);
    }
    
    private void receiveFileAnswer() throws ClassNotFoundException, IOException {
	logger.debug("User " + client.getId() + " attending receive file request");
	String fileName    = (String) channel_object_in.readObject();
	String ownerUserIp = (String) channel_object_in.readObject();
	int portNumber     = (int)    channel_object_in.readObject();

	(new Thread(new FileReceiver(client, fileName, portNumber, ownerUserIp))).start();
    }

    /**
     * Listen and attend for server requests until it orders disconnection.
     */
    @Override
    public void run() {

	try {
	    MessageType type = (MessageType) channel_object_in.readObject();
	    while(type != MessageType.DISCONNECTED) {
		switch (type) {
		case RECEIVE_USERS: 	  getUsersAnswer(); 	   break;
		case NON_EXISTENT_FILE:   nonExistentFileAnswer(); break;
		case FILE_REQUEST_OWNER:  shareFileAnswer();	   break;
		case FILE_RECEIVE:	  receiveFileAnswer();     break;
		default: logger.error("User "+client.getId()+" received an invalid request from the server");
		}
		type = (MessageType) channel_object_in.readObject();
	    }
	    disconnectAnswer();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Listener for client " + client.getId() + " finished abruptly");
	}
    }

}
