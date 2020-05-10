package pc.practice5.part2.messages;

import pc.practice5.part2.client.User;

public class EstablishConnectionMsg extends Message {

    private static final long serialVersionUID = 8329238901841456247L;

    private User user;

    public EstablishConnectionMsg(User user) {
	this.type = MessageType.CONNECT;
	this.user = user;
    }

}
