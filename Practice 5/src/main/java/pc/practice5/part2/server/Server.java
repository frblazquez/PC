package pc.practice5.part2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Server {

    private static final int PORT = 4444;

    // TODO: Add data structures to keep common information
    // private ??
    // private ??

    public static void main(String args[]) {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    while(true) {
		Socket socket = serverSocket.accept();
		(new Thread(new ClientListener(socket))).start();
	    }
	} catch (IOException e) {
	    System.err.println("Unable to create the server");
	    e.printStackTrace();
	}
    }
}
