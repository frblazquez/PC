package pc.practice5.part1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileClient {

    private static final String BASE_FOLDER = "src/main/resources/part1/client/";

    public static void main(String args[]) throws UnknownHostException {

	// The connection is done among processes in this machine
	String hostname = InetAddress.getLocalHost().getHostName();

	try (Socket socket = new Socket(hostname, FileServer.PORT)) {

	    // First we get the data streams
	    InputStream in = socket.getInputStream();
	    OutputStream out = socket.getOutputStream();
	    BufferedWriter string_out = new BufferedWriter(new OutputStreamWriter(out));

	    // Then we send a request with the file name
	    String filename = "quijote.txt";
	    string_out.append(filename);
	    string_out.append('\n');
	    string_out.flush();

	    // We read the file retrieved
	    byte[] b = in.readAllBytes();
	    
	    // Finally we place the file in our folder
	    Path path = Paths.get(BASE_FOLDER + filename);
	    Files.write(path, b);
	    
	} catch (IOException e) {
	    System.err.println("Unable to connect to file server");
	    e.printStackTrace();
	}
    }
}
