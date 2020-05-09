package pc.practice5.part2.messages;


public class EstablishConnectionMsg extends Message {

    public EstablishConnectionMsg() {
	this.type = MessageType.CONNECT;
    }

}
