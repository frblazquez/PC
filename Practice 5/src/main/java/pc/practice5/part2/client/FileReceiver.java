package pc.practice5.part2.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReceiver implements Runnable {

    private Client client;
    private String file_name;
    private int port;
    private String ip;

    public FileReceiver(Client client, String file_name, int port, String ip) {
	this.client = client;
	this.file_name = file_name;
	this.port = port;
	this.ip = ip;
    }

    @Override
    public void run() {
	try (Socket socket = new Socket(ip, port)) {
	    InputStream in = socket.getInputStream();
	    byte[] b = in.readAllBytes();
	    Path path = Paths.get(Client.BASE_FOLDER + client.getId() + "/input/" + file_name);
	    Files.write(path, b);
	} catch (IOException e) {
	    System.err.println("Unable to connect to file server");
	    e.printStackTrace();
	}
    }

}
