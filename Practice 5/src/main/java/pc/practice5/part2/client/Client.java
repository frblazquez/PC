package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pc.practice5.part2.common.MessageType;
import pc.practice5.part2.common.User;

/**
 * Client class implementation for file-sharing application among several
 * clients connected to a manager server.
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

    public String getId() {
	return client_id;
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

    public void setConnectionProtocol() throws IOException, ClassNotFoundException {
	logger.info("Client " + client_id + " establishing the connection");
	channel_object_out.writeObject(MessageType.CONNECT);
	channel_object_out.writeObject(new User(client_id, client_ip));
	channel_object_out.flush();
	MessageType answer = (MessageType) channel_object_in.readObject();
	if (answer != MessageType.CONNECTED)
	    throw new ConnectException();
	logger.debug("Client " + client_id + "connection established");
    }

    public void disconnectRequest() throws IOException, ClassNotFoundException {
	logger.info("Client " + client_id + " closing connection...");
	channel_object_out.writeObject(MessageType.DISCONNECT);
	channel_object_out.flush();
    }

    public void getUsersRequest() throws IOException, ClassNotFoundException {
	logger.info("Asking for users information...");
	channel_object_out.writeObject(MessageType.ASK_USERS);
	channel_object_out.flush();
    }

    public void getFileRequest() throws IOException {
	logger.info("Asking for a file to the server...");
	channel_object_out.writeObject(MessageType.GET_FILE);
	channel_object_out.flush();
    }

    /**
     * Gets indefinitely a client order and sends the corresponding request to the
     * server.
     */
    @Override
    public void run() {

	try {
	    setConnectionProtocol();
	    (new Thread(new ServerListener(this, channel, channel_object_in, channel_object_out))).start();

	    int option = getUserOption();
	    while(option != 0) {
		switch (option) 
		{
		case 1: getUsersRequest();     break;
		case 2: getFileRequest();	break;
		default: System.err.println("Option " + option + " not supported");
		}
		option = getUserOption();
	    }

	    disconnectRequest();
	} catch (ClassNotFoundException | IOException e) {
	    logger.error("Client " + client_id + " ended execution abruptly", e);
	}
    }

    /**
     * 
     * @param args
     */
    public static void main(String args[]) {

	// Read and set the user name
	Scanner in = new Scanner(System.in);
	System.out.print("Username: ");
	String userName = in.next();

	try {
	    // Connect with the server
	    String hostname = InetAddress.getLocalHost().getHostName();
	    Socket socket = new Socket(hostname, 4444);

	    // Client thread execution
	    (new Thread(new Client(userName, hostname, socket, in))).start();
	} catch (IOException e) {
	    logger.fatal("Unable to connect client " + userName + " with server", e);
	}
    }
}
