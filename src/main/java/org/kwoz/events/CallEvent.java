package org.kwoz.events;

import java.util.Date;

public class CallEvent {
    private String msgType;
    private Date msgTime;
    private String PhoneId;

    public CallEvent(String phoneId, String msgType, Date msgTime) {
        this.msgType = msgType;
        this.msgTime = msgTime;
        this.PhoneId = phoneId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getPhoneId() {
        return PhoneId;
    }

    public void setPhoneId(String phoneId) {
        PhoneId = phoneId;
    }
}
