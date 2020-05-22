package pc.practice5.part2.client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.User;

/**
 * When a user is requested to share a file he runs this class properly
 * configured. This implements a server in file owner machine which waits until
 * requester user connects and the file is copied to him.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileSender implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private int port;
    private String file_name;
    private String owner_id;

    public FileSender(String fileName, String ownerId, int portNumber) {
	file_name = fileName;
	owner_id = ownerId;
	port = portNumber;
    }

    public int getPort() {
	return port;
    }

    @Override
    public void run() {
	
	try (ServerSocket serverSocket = new ServerSocket(port)) {
	    logger.info("File sender for file \"" + file_name + "\" created using port " + port);
	    Socket socket = serverSocket.accept();
	    OutputStream out = socket.getOutputStream();

	    // File is taken from owner folder
	    File file = new File(User.BASE_FOLDER + owner_id + "/" + file_name);
	    byte[] fileContent = Files.readAllBytes(file.toPath());
	    out.write(fileContent);
	    out.flush();

	    out.close();
	    socket.close();
	    logger.debug("User " + owner_id + " file sender for file " + file_name + " finished successfully");
	} catch (IOException ignored) {}
    }
}
