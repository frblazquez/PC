package pc.practice5.part2.messages;

public class GetFileMsg extends Message {

    private static final long serialVersionUID = -3709696094331487089L;

    public GetFileMsg() {
	this.type = MessageType.GET_FILE;
    }
}
