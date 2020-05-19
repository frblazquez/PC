package pc.practice5.part2.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * When a user is notified that the file he requested is ready to be sent by the
 * owner he runs this class configured with the owner IP and port of the sender
 * process.
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
	logger.debug("Trying to connect to " + ip + ", port " + port);

	try (Socket socket = new Socket(ip, port)) {
	    InputStream in = socket.getInputStream();
	    byte[] b = in.readAllBytes();
	    Path path = Paths.get(Client.BASE_FOLDER + requester.getId() + "/input/" + file_name);
	    Files.write(path, b);
	    in.close();
	} catch (IOException e) {
	    logger.error("User "+requester.getId()+" not able to connect with file \""+file_name+"\" sender", e);
	}
    }

}
