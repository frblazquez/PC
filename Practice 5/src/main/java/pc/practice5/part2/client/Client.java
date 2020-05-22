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
 * clients connected to a manager server. Clients connect to the server and then
 * can see who is connected to the server or asking for a file to other users.
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
	System.out.println("------------------------");
	System.out.println("Select option:");
	System.out.println("0) Exit");
	System.out.println("1) Get all users");
	System.out.println("2) Get a file");
	System.out.println("------------------------");
	return console_in.nextInt();
    }

    // IMPORTANT
    // Methods are synchronized because they can be invoked after a client request
    // or as an answer from a server request in ServerListener.

    public synchronized void setConnectionProtocol() throws IOException, ClassNotFoundException {
	logger.info("User " + client_id + " establishing the connection with server");
	channel_object_out.writeObject(MessageType.CONNECT);
	channel_object_out.writeObject(new User(client_id, client_ip));
	channel_object_out.flush();
	MessageType answer = (MessageType) channel_object_in.readObject();
	if (answer != MessageType.CONNECTED)
	    throw new ConnectException("Connection protocol failed for user " + client_id);
	logger.debug("User " + client_id + "connection established");
    }

    public synchronized void disconnectRequest() throws IOException, ClassNotFoundException {
	logger.info("User " + client_id + " asking server to close the connection");
	channel_object_out.writeObject(MessageType.DISCONNECT);
	channel_object_out.flush();
    }

    public synchronized void getUsersRequest() throws IOException, ClassNotFoundException {
	logger.info("User " + client_id + " asking server for users connected information");
	channel_object_out.writeObject(MessageType.ASK_USERS);
	channel_object_out.flush();
    }

    public synchronized void getFileRequest() throws IOException {
	System.out.print("Desired file: ");
	String fileName = console_in.next();
	logger.info("User " + client_id + " asking for file " + fileName + " to the server");
	channel_object_out.writeObject(MessageType.FILE_REQUEST_TO_SERVER);
	channel_object_out.writeObject(fileName);
	channel_object_out.flush();
    }

    public synchronized void notifySenderReady(String fileName, String to, int port) throws IOException {
	logger.info("User "+client_id+" notifying server that file \""+fileName+"\" is ready to be transferred");
	channel_object_out.writeObject(MessageType.FILE_SENDER_READY);
	channel_object_out.writeObject(fileName);
	channel_object_out.writeObject(to);
	channel_object_out.writeObject(client_ip);
	channel_object_out.writeObject(port);
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
	    logger.error("User " + client_id + " ended execution abruptly", e);
	}
    }

    /**
     * Initializes a client connection to the server.
     * 
     * @param args unused
     */
    public static void main(String args[]) {

	// Read and set the user name
	Scanner in = new Scanner(System.in);
	System.out.print("Username: ");
	String userName = in.next();

	// In case we want to specify the server host address
	// System.out.print("Server host: ");
	// String serverHost = in.next();
	
	try {
	    // Connect with the server (server in this same machine in port 4444)
	    String host = InetAddress.getLocalHost().getHostName();
	    Socket socket = new Socket(host, 4444);

	    // Client thread execution
	    (new Thread(new Client(userName, host, socket, in))).start();
	} catch (IOException e) {
	    logger.fatal("Unable to connect client " + userName + " with server", e);
	}
    }
}
