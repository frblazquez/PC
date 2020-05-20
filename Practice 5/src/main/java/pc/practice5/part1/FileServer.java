package pc.practice5.part1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Practice 5 part 1 - File server. Waits for client connections and when they
 * are established it creates a new thread (FileSender) to attend the request.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileServer {

    public static final int PORT = 4444;

    public static void main(String args[]) throws IOException {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    while(true) {
		Socket socket = serverSocket.accept();
		(new Thread(new FileSender(socket))).start();
	    }
	} catch (Exception e) {
	    System.err.println("Unable to create the server");
	    e.printStackTrace();
	}
    }
}
