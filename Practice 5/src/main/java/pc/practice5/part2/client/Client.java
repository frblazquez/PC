package pc.practice5.part2.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Client {

    public static void main(String args[]) throws UnknownHostException {

	// The connection is done among processes in this machine
	String hostname = InetAddress.getLocalHost().getHostName();

	// Theoretically, client shouldn't have access to server classes
	// we allow it only to take the PORT
	try (Socket socket = new Socket(hostname, 4444)) {

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
