package pc.practice5.part2.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible of listening and managing all the messages received
 * from the server.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class ServerListener implements Runnable {

    // TODO: Remove unnecessary attributes
    private Socket channel;
    private ObjectInputStream channel_object_in;
    private ObjectOutputStream channel_object_out;

    public ServerListener(Socket socket) throws IOException {
	this.channel = socket;
	this.channel_object_in = new ObjectInputStream(socket.getInputStream());
	this.channel_object_out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
	try {
	    // TODO: Unsafe!
	    List<String> users = (ArrayList<String>) channel_object_in.readObject();
	    for(String usr : users)
		System.out.println(usr);
	} catch (ClassNotFoundException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
