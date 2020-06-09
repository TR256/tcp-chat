package com.chat.client;

import com.chat.message.MessagePacket;
import com.chat.message.MessageType;

import java.awt.*;
import java.util.Date;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class Heartbeat implements Runnable {

    @Override
    public void run() {
        MessagePacket packet = new MessagePacket(MessageType.HEARTBEAT);
        ChatClient client = ChatClient.getInstance();
        long now = 0L;
        String nowStr;
        /**
         * 心跳周期
         */
        int T = 1000 * 60;
        while (true){
            try {
                now = System.currentTimeMillis();
                nowStr = String.valueOf(now);
                packet.setMessage(nowStr);
                client.sendMessage(packet);
                Thread.sleep(T);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
