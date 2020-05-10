package pc.practice5.part2.messages;

public class CloseConnectionMsg extends Message {

    private static final long serialVersionUID = -6594576230048602246L;

    public CloseConnectionMsg() {
	this.type = MessageType.DISCONNECT;
    }
}
