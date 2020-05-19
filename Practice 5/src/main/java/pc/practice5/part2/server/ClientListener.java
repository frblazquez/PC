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

    /**
     * Connection protocol from server side.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public synchronized void setConnectionProtocol() throws ClassNotFoundException, IOException {
	logger.info("Establishing a new connection...");
	MessageType request = (MessageType) channel_object_in.readObject();
	if (request != MessageType.CONNECT)
	    throw new ConnectException("User " + client_id + "is not following connection protocol properly");
	User user = (User) channel_object_in.readObject();
	client_id = user.getId();
	Server.addUser(user, this);
	channel_object_out.writeObject(MessageType.CONNECTED);
	channel_object_out.flush();
	logger.debug("Connection established with user " + client_id);
    }

    /**
     * Disconnection protocol from server side.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public synchronized void disconnectProtocol() throws ClassNotFoundException, IOException {
	logger.info("Closing connection with client " + client_id);
	Server.removeUser(client_id);
	channel_object_out.writeObject(MessageType.DISCONNECTED);
	channel_object_out.flush();
	channel_object_in.close();
	channel_object_out.close();
	channel.close();
	logger.debug("Client " + client_id + " disconnected");
    }

    /**
     * Users connected retrieving protocol from server side.
     * 
     * @throws IOException
     */
    public synchronized void getUsersProtocol() throws IOException {
	logger.info("Getting the clients connected for client " + client_id);
	channel_object_out.writeObject(MessageType.RECEIVE_USERS);
	channel_object_out.writeObject(Server.getAllUsers());
	channel_object_out.flush();
	logger.debug("Users connected sent to client " + client_id);
    }
    
    /**
     * File request protocol from server side.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public synchronized void getFileProtocol() throws ClassNotFoundException, IOException {
	logger.info("Getting file request from client " + client_id);
	String fileName = (String) channel_object_in.readObject();
	String ownerUser = Server.getUserWithFile(fileName);

	if (ownerUser == null) {
	    logger.debug("No client connected has the file " + fileName);
	    channel_object_out.writeObject(MessageType.NON_EXISTENT_FILE);
	    channel_object_out.writeObject(fileName);
	    channel_object_out.flush();
	} else {
	    logger.debug("Notifying the file \"" + fileName + "\"request to the file owner " + ownerUser);
	    Server.notifyFileRequest(client_id, ownerUser, fileName);
	}
    }
    
    /**
     * This method is invoked as part of the file transfer protocol between clients.
     * It notifies the file owner to transfer a file to a concrete user.
     * 
     * @param fileName
     * @throws IOException
     */
    public synchronized void sendFileRequest(String fileName, String requestUserId) throws IOException {
	logger.info("Sending file \"" + fileName + "\" request to client " + client_id);
	channel_object_out.writeObject(MessageType.FILE_REQUEST_OWNER);
	channel_object_out.writeObject(fileName);
	channel_object_out.writeObject(requestUserId);
	channel_object_out.flush();
    }
    
    /**
     * This method is invoked as part of the file transfer protocol between clients.
     * It notifies the file requester user to take it from a certain IP and port.
     * 
     * @param fileName
     * @throws IOException
     */
    public synchronized void receiveFileRequest(String fileName, String ownerUserIP, int portNumber) throws IOException {
	logger.info("Sending file \"" + fileName + "\" receive request to client " + client_id);
	channel_object_out.writeObject(MessageType.FILE_RECEIVE);
	channel_object_out.writeObject(fileName);
	channel_object_out.writeObject(ownerUserIP);
	channel_object_out.writeObject(portNumber);
	channel_object_out.flush();
    }

    /**
     * 
     */
    public synchronized void notifyFileReceiver() throws IOException, ClassNotFoundException {
	String fileName = (String) channel_object_in.readObject();
	String requesterId = (String) channel_object_in.readObject();
	String ownerIp = (String) channel_object_in.readObject();
	int port = (int) channel_object_in.readObject();
	logger.info("Notifying the user " + requesterId + " the file \"" + fileName + "\" is ready to be taken");
	Server.notifyFileRequestAttended(requesterId, fileName, ownerIp, port);
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
		case FILE_REQUEST_TO_SERVER:	getFileProtocol();	break;
		case FILE_SENDER_READY:		notifyFileReceiver();	break;
		case ASK_USERS:			getUsersProtocol();   	break;
		default:			logger.error("Invalid request to the server");
		}
		type = (MessageType) channel_object_in.readObject();
	    }
	    
	    disconnectProtocol();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Listener for client " + client_id + " ended abruptly", e);
	}
    }

}
