package com.chat.message;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/08
 * @Description:
 */
public class MessagePacket {
    private int type;
    private String message;

    public MessagePacket() {

    }

    public MessagePacket(int type) {
        this.type = type;
    }

    public MessagePacket(int type, String message) {
        this.type = type;
        this.message = message;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessagePacket{" +
                "type=" + type +
                ", message=" + message +
                '}';
    }
}
