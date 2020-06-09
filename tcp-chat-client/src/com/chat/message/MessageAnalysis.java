package com.chat.message;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/08
 * @Description:
 */
public class MessageAnalysis {

    public void analyze(MessagePacket packet) {
        int type = packet.getType();
        System.out.println("---解析来自服务器的消息----");
        switch (type) {
            case MessageType.LOGIN_RESPONSE:
                login(packet);
                break;
            default:
                System.out.println("---未知消息---");
                break;
        }
    }

    private void login(MessagePacket packet) {
        System.out.println(packet.getMessage());
    }
}
