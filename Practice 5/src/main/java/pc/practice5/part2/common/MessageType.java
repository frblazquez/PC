package pc.practice5.part2.common;

import java.io.Serializable;

public enum MessageType implements Serializable {
    CONNECT, CONNECTED, DISCONNECT, DISCONNECTED, ASK_USERS, RECEIVE_USERS, GET_FILE;
}
