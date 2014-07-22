package org.kwoz.events;

/**
 * @author karol.wozniak@nsn.com
 */
public class TransmissionEvent {
    private String msgType;
    private int size;

    public TransmissionEvent(String msgType, int size) {
        this.msgType = msgType;
        this.size = size;
    }

    public TransmissionEvent(String msgType) {
        this(msgType, 0);
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
