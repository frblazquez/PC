package pc.practice5.part2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.MessageType;
import pc.practice5.part2.common.User;

/**
 * This class is responsible of listening and managing all the requests received
 * from a client.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ClientListener implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Socket channel;
    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;
    private String client_id;

    public ClientListener(Socket socket) throws IOException {
	channel = socket;
	channel_object_in = new ObjectInputStream(channel.getInputStream());
	channel_object_out = new ObjectOutputStream(channel.getOutputStream());
    }

    public void setConnectionProtocol() throws ClassNotFoundException, IOException {
	logger.info("Establishing the connection...");
	MessageType request = (MessageType) channel_object_in.readObject();
	if (request != MessageType.CONNECT)
	    throw new ConnectException();
	User user = (User) channel_object_in.readObject();
	client_id = user.getId();
	Server.addUser(user);
	channel_object_out.writeObject(MessageType.CONNECTED);
	logger.debug("Connection established with client " + client_id);
    }

    public void disconnectProtocol() throws ClassNotFoundException, IOException {
	logger.info("Closing connection with user...");
	Server.removeUser(client_id);
	channel_object_out.writeObject(MessageType.DISCONNECTED);
	channel_object_out.flush();
	channel_object_in.close();
	channel_object_out.close();
	channel.close();
	logger.debug("User " + client_id + " disconnected");
    }

    public void getUsersProtocol() throws IOException {
	logger.info("Getting the users connected...");
	channel_object_out.writeObject(MessageType.RECEIVE_USERS);
	channel_object_out.writeObject(Server.getAllUsers());
	channel_object_out.flush();
	logger.debug("Users connected sent to client");
    }
    
    public void getFileProtocol() {
	logger.info("Getting file requested...");
    }
    
    /**
     * Sets the connection, listens to requests and finally (when the client wants)
     * close the connection safely.
     */
    @Override
    public void run() {
	
	try {
	    setConnectionProtocol();
	    
	    MessageType type = (MessageType) channel_object_in.readObject();
	    while(type != MessageType.DISCONNECT) {
		switch (type) 
		{
		case GET_FILE:	getFileProtocol();	break;
		case ASK_USERS:	getUsersProtocol();   	break;
		default:	logger.error("Invalid request to the server");
		}
		type = (MessageType) channel_object_in.readObject();
	    }
	    
	    disconnectProtocol();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Listener for client " + client_id + " ended abruptly", e);
	}
    }

}
