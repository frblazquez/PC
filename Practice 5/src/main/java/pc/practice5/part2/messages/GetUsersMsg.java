package pc.practice5.part2.messages;

public class GetUsersMsg extends Message {

    private static final long serialVersionUID = 7643104778312341243L;

    public GetUsersMsg() {
	this.type = MessageType.GET_USERS;
    }
}
