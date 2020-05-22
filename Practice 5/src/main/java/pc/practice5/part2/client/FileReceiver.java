package pc.practice5.part2.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.User;

/**
 * When a user is notified that the file he requested is ready to be shared by
 * the owner he runs this class configured with the owner IP and port of the
 * sender process.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileReceiver implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Client requester;
    private String file_name;
    private int port;
    private String ip;

    public FileReceiver(Client client, String file_name, int port, String ip) {
	this.requester = client;
	this.file_name = file_name;
	this.port = port;
	this.ip = ip;
    }

    @Override
    public void run() {
	logger.debug("Creating file receiver process from IP " + ip + ", port " + port);

	try (Socket socket = new Socket(ip, port)) {
	    InputStream in = socket.getInputStream();
	    byte[] b = in.readAllBytes();
	    Path path = Paths.get(User.BASE_FOLDER + requester.getId() + "/input/" + file_name);
	    Files.write(path, b);
	    in.close();
	    System.out.println("Se ha obtenido correctamente el fichero \"" + file_name + "\"");
	    logger.info("File \"" + file_name + "\" successfully copied to user " + requester.getId() + " directory");
	} catch (IOException e) {
	    logger.error("File \""+file_name+"\" receiver process for user "+requester.getId()+ " failed to connect with file owner");
	    logger.error("User " + requester.getId() + " failed to obtain file \"" + file_name + "\"", e);
	}
    }

}
