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
 * from a client at the server side.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ClientListener implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Server server;
    private Socket channel;
    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;
    private String client_id;

    public ClientListener(Server srv, Socket socket) throws IOException {
	server = srv;
	channel = socket;
	channel_object_in = new ObjectInputStream(channel.getInputStream());
	channel_object_out = new ObjectOutputStream(channel.getOutputStream());

	// client_id should be initialized while the connection is established
    }

    // IMPORTANT
    // Methods are synchronized because they can be invoked from the own listener
    // after a request of a client or from the Server when attending another user
    // file request. We have to protect the access to the streams.

    /**
     * Connection protocol implementation from server side.
     */
    public synchronized void setConnectionProtocol() throws ClassNotFoundException, IOException {
	logger.info("Establishing a new connection...");
	MessageType request = (MessageType) channel_object_in.readObject();
	if (request != MessageType.CONNECT)
	    throw new ConnectException("User " + client_id + "is not following connection protocol properly");
	User user = (User) channel_object_in.readObject();
	client_id = user.getId();
	server.addUser(user, this);
	channel_object_out.writeObject(MessageType.CONNECTED);
	channel_object_out.flush();
	logger.debug("Connection established with user " + client_id);
    }

    /**
     * Disconnection protocol from server side.
     */
    public synchronized void disconnectProtocol() throws ClassNotFoundException, IOException {
	logger.info("Attending disconnect request from client " + client_id);
	server.removeUser(client_id);
	channel_object_out.writeObject(MessageType.DISCONNECTED);
	channel_object_out.flush();
	channel_object_in.close();
	channel_object_out.close();
	channel.close();
	logger.debug("User " + client_id + " disconnect request ended successfully");
    }

    /**
     * Users connected retrieving protocol from server side.
     */
    public synchronized void getUsersProtocol() throws IOException {
	logger.info("Attending get-users request from user " + client_id);
	channel_object_out.writeObject(MessageType.RECEIVE_USERS);
	channel_object_out.writeObject(server.getAllUsers());
	channel_object_out.flush();
	logger.debug("Users connected sent to user " + client_id);
    }
    
    /**
     * File request protocol from server side.
     */
    public synchronized void getFileProtocol() throws ClassNotFoundException, IOException {
	logger.info("Attending file request from user " + client_id);
	String fileName = (String) channel_object_in.readObject();
	String ownerUser = server.getUserWithFile(fileName);

	if (ownerUser == null) {
	    logger.debug("Notifying user " + client_id + " that no user connected has the file \"" + fileName + "\"");
	    channel_object_out.writeObject(MessageType.NON_EXISTENT_FILE);
	    channel_object_out.writeObject(fileName);
	    channel_object_out.flush();
	} else {
	    logger.debug("Asking server to notify the user " + ownerUser + " that his file \"" + fileName + "\" is being requested");
	    server.notifyFileRequestToOwner(client_id, ownerUser, fileName);
	}
    }
    
    /**
     * Method invoked as part of the file transfer protocol between clients. It
     * notifies the file owner to transfer a file to a concrete user.
     */
    public synchronized void sendFileRequest(String fileName, String requestUserId) throws IOException {
	logger.info("Sending user "+client_id+" a request to send his file \""+fileName+"\""+" to user "+requestUserId);
	channel_object_out.writeObject(MessageType.FILE_REQUEST_OWNER);
	channel_object_out.writeObject(fileName);
	channel_object_out.writeObject(requestUserId);
	channel_object_out.flush();
    }
    
    /**
     * This method is invoked as part of the file transfer protocol between clients.
     * It notifies the file requester user to take it from a certain IP and port.
     */
    public synchronized void receiveFileRequest(String fileName, String ownerUserIP, int portNumber) throws IOException {
	logger.info("Sending user " + client_id + " a request to read the file \"" + fileName + "\"");
	channel_object_out.writeObject(MessageType.FILE_RECEIVE);
	channel_object_out.writeObject(fileName);
	channel_object_out.writeObject(ownerUserIP);
	channel_object_out.writeObject(portNumber);
	channel_object_out.flush();
    }

    /**
     * This method is invoked as part of the file transfer protocol between clients.
     * It notifies the server that the file owner has enabled a connection for
     * sharing the file requested.
     */
    public synchronized void notifyFileReceiverRequest() throws IOException, ClassNotFoundException {
	String fileName    = (String) channel_object_in.readObject();
	String requesterId = (String) channel_object_in.readObject();
	String ownerIp     = (String) channel_object_in.readObject();
	int port = (int) channel_object_in.readObject();
	logger.info("Asking server to notify the user " + requesterId + " that the file \"" + fileName + "\" is ready to be taken");
	server.notifyFileReadyToRequester(requesterId, fileName, ownerIp, port);
    }

    /**
     * Executes the connection protocol with server, listens indefinitely to user
     * requests and finally (when the user wants) closes the connection safely.
     */
    @Override
    public void run() {
	
	try {
	    setConnectionProtocol();
	    
	    MessageType type = (MessageType) channel_object_in.readObject();
	    while(type != MessageType.DISCONNECT) {
		switch (type) 
		{
		case FILE_REQUEST_TO_SERVER:	getFileProtocol();		break;
		case FILE_SENDER_READY:		notifyFileReceiverRequest();	break;
		case ASK_USERS:			getUsersProtocol();   		break;
		default:			logger.error("User "+client_id+" did an invalid request to the server");
		}
		type = (MessageType) channel_object_in.readObject();
	    }
	    
	    disconnectProtocol();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Listener for client " + client_id + " ended abruptly", e);
	}
    }

}
