package pc.practice5.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Practice 5 part 1 - Class responsible of receiving the request and sending
 * the file to the client from the server side.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileSender implements Runnable {

    private static final String BASE_FOLDER = "src/main/resources/part1/server/";
    private Socket socket;

    public FileSender(Socket s) {
	socket = s;
    }

    @Override
    public void run() {
	try {
	    // Data streams
	    InputStream in = socket.getInputStream();
	    OutputStream out = socket.getOutputStream();
	    BufferedReader string_in = new BufferedReader(new InputStreamReader(in));

	    // Client gives the file name
	    String filename = string_in.readLine();

	    // We send them the file (not appropriate for big files)
	    File file = new File(BASE_FOLDER + filename);
	    byte[] fileContent = Files.readAllBytes(file.toPath());
	    out.write(fileContent);
	    out.flush();

	    // Streams close
	    string_in.close();
	    in.close();
	    out.close();
	    socket.close();
	} catch (IOException e) {
	    System.err.println("Unable to find and send the file requested");
	    e.printStackTrace();
	}
    }

}
