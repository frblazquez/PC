package pc.practice5.part2.common;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pc.practice5.part2.client.Client;

public class User implements Serializable {

    private static final long serialVersionUID = -5134294967447495010L;

    private String id;
    private String ip_address;
    private List<String> file_names;

    public User(String userName) throws UnknownHostException {
	this.id = userName;
	this.ip_address = InetAddress.getLocalHost().getHostName();
	this.file_names = new ArrayList<String>();

	File userFolder = new File(Client.BASE_FOLDER + userName);
	if (userFolder.exists())
	    for(File f: userFolder.listFiles())
		if(f.isFile()) file_names.add(f.getName());
    }
    
    public User(String userName, String userIp) {
	this.id = userName;
	this.ip_address = userIp;
	this.file_names = new ArrayList<String>();

	File userFolder = new File(Client.BASE_FOLDER + userName);
	if (userFolder.exists())
	    for(File f : userFolder.listFiles())
		if (f.isFile()) file_names.add(f.getName());
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
