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
 * Practice 5 part 1 - This class implements a client connection to the file
 * server. After the connection the client requests a file from the file server
 * and writes it in it's folder.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileClient {

    private static final String BASE_FOLDER = "src/main/resources/part1/client/";

    public static void main(String args[]) throws UnknownHostException {

	// The connection is done among processes in this machine
	String hostname = InetAddress.getLocalHost().getHostName();

	// Theoretically, client shouldn't have access to server classes
	// we allow it only to take the PORT
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
	    if (b.length > 0) {
		Path path = Paths.get(BASE_FOLDER + filename);
		Files.write(path, b);
	    }

	    // Streams close
	    string_out.close();
	    in.close();
	    out.close();

	} catch (IOException e) {
	    System.err.println("Unable to connect to file server");
	    e.printStackTrace();
	}
    }
}
