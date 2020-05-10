package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import pc.practice5.part2.messages.CloseConnectionMsg;
import pc.practice5.part2.messages.EstablishConnectionMsg;
import pc.practice5.part2.messages.GetFileMsg;
import pc.practice5.part2.messages.GetUsersMsg;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Client {

    public static void main(String args[]) throws UnknownHostException {

	String hostname = InetAddress.getLocalHost().getHostName();
	try (Socket socket = new Socket(hostname, 4444); Scanner console_in = new Scanner(System.in);) {

	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

	    String userName = "user1";
	    String ip = InetAddress.getLocalHost().getHostName();
	    User user = new User(userName, ip);

	    out.writeObject(new EstablishConnectionMsg(user));
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
		    out.writeObject(new CloseConnectionMsg());
		    out.flush();
		    break;
		case 1:
		    out.writeObject(new GetUsersMsg());
		    out.flush();
		    break;
		case 2:
		    out.writeObject(new GetFileMsg());
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
