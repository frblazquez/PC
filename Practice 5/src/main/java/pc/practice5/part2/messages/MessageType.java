package pc.practice5.part2.messages;

import java.io.Serializable;

public enum MessageType implements Serializable {
    CONNECT, DISCONNECT, GET_USERS, GET_FILE;
}
