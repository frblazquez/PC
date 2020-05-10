package pc.practice5.part2.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String BASE_FOLDER = "src/main/resources/part2/users/";

    private String id;
    private String ip_address;
    private List<String> file_names;

    public User(String userName, String ip) {
	this.id = userName;
	this.ip_address = ip;

	// TODO: This part can be improved
	File usr_folder = new File(BASE_FOLDER + userName);
	File[] files = usr_folder.listFiles();

	file_names = new ArrayList<String>();
	for(File f : files) {
	    file_names.add(f.getName());
	}
    }

    public String getId() {
	return id;
    }

    public String getIp_address() {
	return ip_address;
    }

    public List<String> getFile_names() {
	return file_names;
    }

}
