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

    private static HashMap<String, String>       user_ip    = new HashMap<>();
    private static HashMap<String, List<String>> user_files = new HashMap<>();
    private static HashMap<String, String>       file_user  = new HashMap<>(); // TODO: Unused for the moment

    public static synchronized void addUser(User user) {
	logger.info("Adding user " + user.getId());
	user_ip.put(user.getId(), user.getIp_address());
	user_files.put(user.getId(), user.getFile_names());
    }

    public static synchronized void removeUser(String userId) {
	logger.info("Removing user " + userId);
	user_ip.remove(userId);
	user_files.remove(userId);
    }

    public static synchronized List<String> getAllUsers() {
	return new ArrayList<String>(user_ip.keySet());
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
		(new Thread(new ClientListener(socket))).start();
	    }
	} catch (IOException e) {
	    logger.fatal("Unable to start the server", e);
	}
    }
}
