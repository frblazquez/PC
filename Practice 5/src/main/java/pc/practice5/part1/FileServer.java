package pc.practice5.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

/**
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class FileServer {

    public static final int PORT = 4444;
    private static final String BASE_FOLDER = "src/main/resources/part1/server/";

    public static void main(String args[]) throws IOException {

	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    // Wait until connection is established
	    Socket socket = serverSocket.accept();

	    // Data streams
	    InputStream in = socket.getInputStream();
	    OutputStream out = socket.getOutputStream();
	    BufferedReader string_in = new BufferedReader(new InputStreamReader(in));

	    // First we get the name of the request (name of the file)
	    String filename = string_in.readLine();

	    // Then we send the file
	    File file = new File(BASE_FOLDER + filename);
	    byte[] fileContent = Files.readAllBytes(file.toPath());
	    out.write(fileContent);
	    out.flush();

	} catch (IOException e) {
	    System.err.println("Unable to create the file server");
	    e.printStackTrace();
	}
    }
}
