package pc.practice5.part2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.client.User;

/**
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

    // TODO: port number as an argument?
    public static void main(String args[]) {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    while(true) {
		Socket socket = serverSocket.accept();
		Thread th = new Thread(new ClientListener(socket));
		th.start();
	    }
	} catch (IOException e) {
	    // TODO: Exceptions management
	    e.printStackTrace();
	}
    }
}
