package pc.practice5.part2.messages;

import java.io.Serializable;

import pc.practice5.part2.client.User;

public class EstablishConnectionMsg implements Serializable {

    private static final long serialVersionUID = 8329238901841456247L;

    private User user;

    public EstablishConnectionMsg(User user) {
	this.user = user;
    }

}
