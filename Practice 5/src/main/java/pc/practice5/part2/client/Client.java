package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import pc.practice5.part2.messages.CloseConnectionMsg;
import pc.practice5.part2.messages.EstablishConnectionMsg;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Client {

    public static void main(String args[]) throws UnknownHostException {
	String hostname = InetAddress.getLocalHost().getHostName();
	try (Socket socket = new Socket(hostname, 4444); Scanner console_in = new Scanner(System.in);) {
	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	    out.writeObject("Hola");
	    out.flush();
	    out.writeObject(new EstablishConnectionMsg());
	    out.flush();
	    out.writeObject(new CloseConnectionMsg());
	    out.flush();

	    // It's important not finishing before the listener gets the objects
	    while(true) {
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

//    public static void main(String args[]) throws UnknownHostException {
//
//	Scanner console_in = new Scanner(System.in);
//
//	// Read client name
//	String clientName = "user"; // TODO: console_in.nextLine();
//
//	// The connection is done among processes in this machine
//	String hostname = InetAddress.getLocalHost().getHostName();
//
//	// TODO: WARNING! Handcoded port number!
//	try (Socket socket = new Socket(hostname, 4444)) {
//	    (new Thread(new ServerListener(socket))).start();
//	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//	    out.writeObject(new EstablishConnectionMsg());
//	    out.flush();
//
//	    int option;
//	    do {
//		System.out.println("--------------------------------------------------");
//		System.out.println("Set option:");
//		System.out.println("0) Exit");
//		System.out.println("1) Get all users");
//		System.out.println("2) Get a file");
//		System.out.println("--------------------------------------------------");
//		option = console_in.nextInt();
//		
//		// TODO: Complete
//		switch (option) {
//		case 0:
//		    out.writeObject(new CloseConnectionMsg());
//		    out.flush();
//		    break;
//		case 1:
//		    break;
//		case 2:
//		    break;
//		default:
//		    System.err.println("Option " + option + " not supported");
//		}
//	    } while(option != 0);
//
//	} catch (IOException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	} finally {
//	    console_in.close();
//	}
//    }
}
