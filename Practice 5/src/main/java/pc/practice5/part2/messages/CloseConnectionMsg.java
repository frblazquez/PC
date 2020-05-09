package pc.practice5.part2.messages;

public class CloseConnectionMsg extends Message {

    public CloseConnectionMsg() {
	this.type = MessageType.DISCONNECT;
    }
}
