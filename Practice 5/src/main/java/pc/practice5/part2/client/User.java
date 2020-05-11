package pc.practice5.part2.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = -5134294967447495010L;
    private static final String BASE_FOLDER = "src/main/resources/part2/users/";

    private String id;
    private String ip_address;
    private List<String> file_names;

    public User(String userName, String ip) {
	this.id = userName;
	this.ip_address = ip;

	// TODO: Always empty
	this.file_names = new ArrayList<String>();
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
