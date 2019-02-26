package com.example.whu.bcrc.entity;

/**
 * Created by guochen.zhang on 2018/5/19.
 */

public class ContactMessage {
    public static final int TYPE_RECIEVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int msgType;

    public ContactMessage(String content, int msgType) {
        this.content = content;
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public int getMsgType() {
        return msgType;
    }

}
