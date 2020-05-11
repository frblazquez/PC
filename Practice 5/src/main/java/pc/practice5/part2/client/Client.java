package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import pc.practice5.part2.messages.MessageType;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Client {

    public static void main(String args[]) throws UnknownHostException {

	String hostname = InetAddress.getLocalHost().getHostName();
	try (Socket socket = new Socket(hostname, 4444); Scanner console_in = new Scanner(System.in);) {

	    Thread th = new Thread(new ServerListener(socket));
	    th.start();

	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

	    String userName = "user1";
	    String ip = InetAddress.getLocalHost().getHostName();
	    User user = new User(userName, ip);

	    System.out.println("Establishing the connection");
	    out.writeObject(MessageType.CONNECT);
	    out.writeObject(user);
	    out.flush();

	    int option;
	    do {
		System.out.println("--------------------------------------------------");
		System.out.println("Set option:");
		System.out.println("0) Exit");
		System.out.println("1) Get all users");
		System.out.println("2) Get a file");
		System.out.println("--------------------------------------------------");
		System.out.println();
		option = console_in.nextInt();

		// TODO: Complete
		switch (option) {
		case 0:
		    out.writeObject(MessageType.DISCONNECT);
		    out.writeObject(userName);
		    out.flush();
		    break;
		case 1:
		    out.writeObject(MessageType.GET_USERS);
		    out.flush();
		    break;
		case 2:
		    out.writeObject(MessageType.GET_FILE);
		    out.flush();
		    break;
		default:
		    System.err.println("Option " + option + " not supported");
		}
	    } while(option != -1);

	} catch (IOException e) {
	    // TODO: Exceptions management
	    e.printStackTrace();
	}
    }
}
