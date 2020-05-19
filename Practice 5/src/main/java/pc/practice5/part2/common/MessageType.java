package pc.practice5.part2.common;

import java.io.Serializable;

public enum MessageType implements Serializable {
    CONNECT, 
    CONNECTED, 
    DISCONNECT, 
    DISCONNECTED, 
    ASK_USERS, 
    RECEIVE_USERS, 
    FILE_REQUEST_TO_SERVER, 
    FILE_REQUEST_OWNER,
    NON_EXISTENT_FILE, 
    FILE_SENDER_READY, 
    FILE_RECEIVE;
}
