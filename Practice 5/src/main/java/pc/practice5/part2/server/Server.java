package pc.practice5.part2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.User;

/**
 * This class implements a server for managing a share-files application among
 * users. Clients can check who is connected and request files to other
 * connected clients.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Server {

    private static final int PORT = 4444;
    private static final Logger logger = LogManager.getLogger();

    private static HashMap<String, String>         user_ip       = new HashMap<>();
    private static HashMap<String, List<String>>   user_files    = new HashMap<>();
    private static HashMap<String, String> 	   file_owner    = new HashMap<>();
    private static HashMap<String, ClientListener> user_listener = new HashMap<>();

    public static synchronized void addUser(User user, ClientListener listener) {
	logger.info("Adding user " + user.getId());

	// User IDs are supposed to be unique, we continue execution even though
	// TODO: Exception? Message to client to select another user ID?
	if (user_ip.containsKey(user.getId()))
	    logger.error("There is already a user registered with ID " + user.getId());

	user_ip.put(user.getId(), user.getIp_address());
	user_files.put(user.getId(), user.getFile_names());
	user_listener.put(user.getId(), listener);
	
	for(String fileName : user.getFile_names()) {
	    // Several clients might have the same file
	    if (file_owner.containsKey(fileName))
		logger.warn("File " + fileName + " is owned by more than one user");

	    file_owner.put(fileName, user.getId());
	}

	logger.debug("User " + user.getId() + " successfully added");
    }

    public static synchronized void removeUser(String userId) {
	logger.info("Removing user " + userId);
	user_ip.remove(userId);
	user_files.remove(userId);
	user_listener.remove(userId);
	file_owner.values().removeIf(val -> userId.equals(val));
	logger.debug("User " + userId + " successfully removed");
    }

    public static synchronized List<String> getAllUsers() {
	logger.debug("Attending get-all-users request");
	return new ArrayList<String>(user_ip.keySet());
    }

    /**
     * @return User ID of the owner of the file {@code fileName} or null if there is
     *         no such user connected
     */
    public static synchronized String getUserWithFile(String fileName) {
	logger.debug("Attending get-owner-for-file request");
	return file_owner.get(fileName);
    }

    public static synchronized void notifyFileRequest(String requestUserId, String ownerUserId, String fileName) {
	try {
	    logger.info("Sending file \""+ fileName +"\" request from user "+requestUserId+" to user "+ownerUserId);
	    user_listener.get(ownerUserId).sendFileRequest(fileName, requestUserId);
	} catch (IOException e) {
	    logger.error("Sending file \"" + fileName + "\" request to client " + ownerUserId + " failed", e);
	}
    }
    
    public static synchronized void notifyFileRequestAttended(String requestUserId, String fileName, String ownerUserIp, int portNumber) {
	try {
	    logger.info("Notifying user " + requestUserId + " to take the file \"" + fileName + "\"requested");
	    user_listener.get(requestUserId).receiveFileRequest(fileName, ownerUserIp, portNumber);
	} catch (IOException e) {
	    logger.error("Receiving file \"" + fileName + "\" by user " + requestUserId + " failed", e);
	}
    }

    /**
     * Starts a server and waits indefinitely for client connections. Port number is
     * fixed to be {@link #PORT}.
     * 
     * @param args unused
     */
    public static void main(String args[]) {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    while(true) {
		Socket socket = serverSocket.accept();
		logger.debug("New connection established, creating listener for the client");
		(new Thread(new ClientListener(socket))).start();
	    }
	} catch (IOException e) {
	    logger.fatal("Unable to start the server", e);
	}
    }
}
