package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.messages.MessageType;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Client implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private String client_id;
    private String client_ip;

    private Scanner console_in;
    private Socket channel;
    private ObjectOutputStream channel_object_out;
    private ObjectInputStream channel_object_in;

    public Client(String id, String ip, Socket s, Scanner in) throws IOException {
	client_id = id;
	client_ip = ip;

	console_in = in;
	channel = s;
	channel_object_out = new ObjectOutputStream(s.getOutputStream());
	channel_object_in = new ObjectInputStream(s.getInputStream());
    }

    private int getUserOption() {
	System.out.println("--------------------------------------------------");
	System.out.println("Set option:");
	System.out.println("0) Exit");
	System.out.println("1) Get all users");
	System.out.println("2) Get a file");
	System.out.println("--------------------------------------------------");
	System.out.println();
	return console_in.nextInt();
    }

    public void setConnectionProtocol() throws IOException {
	logger.info("Establishing the connection...");
	channel_object_out.writeObject(new User(client_id, client_ip));
	channel_object_out.flush();
	logger.debug("Connection established");
    }

    public void disconnectProtocol() throws IOException {
	logger.info("Closing connection...");
	channel_object_out.writeObject(MessageType.DISCONNECT);
	channel_object_out.flush();
	channel_object_in.close();
	channel_object_out.close();
	channel.close();
	logger.debug("Connection closed");
    }

    public void getUsersProtocol() throws IOException, ClassNotFoundException {
	logger.info("Asking for users information...");
	channel_object_out.writeObject(MessageType.GET_USERS);
	channel_object_out.flush();
	List<String> users;
	users = (ArrayList<String>) channel_object_in.readObject();
	for(String usr : users)
	    System.out.println(usr);
	logger.debug("Users successfully retrieved...");
    }

    public void getFileProtocol() throws IOException {
	logger.info("Asking for a file to the server...");
	channel_object_out.writeObject(MessageType.GET_FILE);
	channel_object_out.flush();
	logger.debug("File sucessfully retrieved...");
    }

    @Override
    public void run() {

	try {
	    setConnectionProtocol();
	    (new Thread(new ServerListener(channel_object_in, channel_object_out))).start();

	    int option = getUserOption();
	    while(option != 0) {
		switch (option) 
		{
		case 1: getUsersProtocol();     break;
		case 2: getFileProtocol();	break;
		default: System.err.println("Option " + option + " not supported");
		}
		option = getUserOption();
	    }

	    disconnectProtocol();
	} catch (ClassNotFoundException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws UnknownHostException {

	String hostname = InetAddress.getLocalHost().getHostName();

	try {
	    Socket socket = new Socket(hostname, 4444);
	    Scanner in = new Scanner(System.in);
	    System.out.print("Username: ");
	    String userName = in.next();
	    String ip = InetAddress.getLocalHost().getHostName();
	    Client user = new Client(userName, ip, socket, in);
	    (new Thread(user)).start();
	} catch (IOException e) {
	    // TODO: Exceptions management
	    e.printStackTrace();
	}
    }
}
