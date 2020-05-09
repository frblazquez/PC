package pc.practice5.part2.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = -99298919837773058L;

    protected MessageType type;
    protected String origin;
    protected String destination;

    public String getOrigin() {
	return origin;
    }

    public String getDestination() {
	return destination;
    }

    public enum MessageType {
	CONNECT, DISCONNECT, GET_USERS, GET_FILE;
    }

    public MessageType getType() {
	return type;
    }
}
