package pc.practice5.part2.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.User;

/**
 * This class implements a server for managing a share files application between
 * users. Clients can check who is connected and request files to other
 * connected clients. All the communications with clients are done concurrently
 * via {@link ClientListener}.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Server implements Runnable {

    private static final int PORT = 4444;
    private static final Logger logger = LogManager.getLogger();

    private HashMap<String, String> 	    user_ip 	  = new HashMap<>();
    private HashMap<String, List<String>>   user_files    = new HashMap<>();
    private HashMap<String, String> 	    file_owner 	  = new HashMap<>();
    private HashMap<String, ClientListener> user_listener = new HashMap<>();

    public void addUser(User user, ClientListener listener) {

	// TODO: Explicar por qué lo has hecho así en vez de monitores explícitos, no
	// queremos exclusión mutua sino coherencia y poder manejar el concepto de
	// transacción o ejecución atómica de acciones en varias estructuras de datos.
	synchronized (user_ip)       {
	synchronized (user_files)    {
	synchronized (user_listener) {
	synchronized (file_owner)    {
	    
	logger.info("Adding user " + user.getId() + " to server");

	// User IDs are supposed to be unique, we continue execution even though
	if (user_ip.containsKey(user.getId()))
	    logger.error("There is already a user registered with ID \"" + user.getId() + "\"");

	user_ip.put(user.getId(), user.getIp_address());
	user_files.put(user.getId(), user.getFile_names());
	user_listener.put(user.getId(), listener);
	
	for(String fileName : user.getFile_names()) {
	    // Several clients might have the same file
	    if (file_owner.containsKey(fileName))
		logger.warn("File \"" + fileName + "\" is owned by more than one user");

	    file_owner.put(fileName, user.getId());
	}

	logger.debug("User " + user.getId() + " successfully added to server");}}}}
    }

    public void removeUser(String userId) {
	// TODO: Explicar la importancia de tomarlos en el mismo orden que en addUser
	// para evitar un interbloqueo
	synchronized (user_ip)       {
	synchronized (user_files)    {
	synchronized (user_listener) {
	synchronized (file_owner)    {
	   logger.info("Removing user " + userId + " from server");
	   user_ip.remove(userId);                                 
	   user_files.remove(userId);                              
	   user_listener.remove(userId);                  
	   file_owner.values().removeIf(val -> userId.equals(val));
	   logger.debug("User " + userId + " successfully removed from server");
	}}}}
    }

    public List<String> getAllUsers() {
	synchronized (user_ip) {
	logger.debug("Server attending get-all-users request");
	return new ArrayList<String>(user_ip.keySet());
	}
    }

    /**
     * @return User ID of the owner of the file {@code fileName} or null if there is
     *         no such user connected
     */
    public String getUserWithFile(String fileName) {
	synchronized (file_owner) {
	logger.debug("Server attending get-owner-for-file request");
	return file_owner.get(fileName);
	}
    }

    /**
     * Notifies the user owner of a file requested that a certain user requests it.
     */
    public void notifyFileRequestToOwner(String requestUserId, String ownerUserId, String fileName) {
	synchronized (user_listener) {
	try {
	    logger.info("Server sending \""+ fileName +"\" request from user "+requestUserId+" to owner user "+ownerUserId);
	    user_listener.get(ownerUserId).sendFileRequest(fileName, requestUserId);
	} catch (IOException e) {
	    logger.error("Sending file \"" + fileName + "\" request to client " + ownerUserId + " failed", e);
	}
	}
    }
    
    /**
     * Notifies the user that requested a file that it's ready to be transferred.
     */
    public void notifyFileReadyToRequester(String requestUserId, String fileName, String ownerUserIp, int portNumber) {
	synchronized (user_listener) {
	try {
	    logger.info("Server notifying user " + requestUserId + " to take the file \"" + fileName + "\"requested");
	    user_listener.get(requestUserId).receiveFileRequest(fileName, ownerUserIp, portNumber);
	} catch (IOException e) {
	    logger.error("Server failed to communicate user "+ requestUserId +" to take the file \"" + fileName + "\" requested", e);
	}
	}
    }

    /**
     * Starts a server and waits indefinitely for client connections. Port number is
     * fixed to be {@link #PORT}.
     * 
     * @param args unused
     */
    @Override
    public void run() {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    logger.debug("Server created at " + InetAddress.getLocalHost().getHostName() + ", port " + PORT);
	    while(true) {
		Socket socket = serverSocket.accept();
		logger.debug("New connection established, creating listener for the client");
		(new Thread(new ClientListener(this, socket))).start();
	    }
	} catch (IOException e) {
	    logger.fatal("Unable to start the server", e);
	}
    }

    /**
     * Runs a file-sharing server
     */
    public static void main(String args[]) {
	(new Thread(new Server())).start();
    }

}
