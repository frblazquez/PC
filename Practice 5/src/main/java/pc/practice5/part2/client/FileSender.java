package pc.practice5.part2.client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileSender implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private int port;
    private String file_name;
    private String owner_id;

    public FileSender(String fileName, String ownerId) {
	file_name = fileName;
	owner_id = ownerId;
	port = 4747; // Default port number
    }

    public int getPort() {
	return port;
    }

    @Override
    public void run() {
	
	// Get an available port number
	boolean portAvailable = false;
	Random rand = new Random();
	
	while(!portAvailable) {
	    port = 1000+rand.nextInt(99000);
	    try (ServerSocket serverSocket = new ServerSocket(port)) {
		portAvailable = true;
		logger.debug("File sender for file " + file_name + " using port " + port);
		Socket socket = serverSocket.accept();
		OutputStream out = socket.getOutputStream();

		// File is taken from owner folder
		File file = new File(Client.BASE_FOLDER + owner_id + "/" + file_name);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		out.write(fileContent);
		out.flush();

		out.close();
		socket.close();
	    } catch (IOException ignored) { }
	}
    }
}
