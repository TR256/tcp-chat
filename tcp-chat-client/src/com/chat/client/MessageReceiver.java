package com.chat.client;

import com.alibaba.fastjson.JSON;
import com.chat.message.MessageAnalysis;
import com.chat.message.MessagePacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class MessageReceiver implements Runnable {

    private Socket socket;
    private MessageAnalysis analysis;

    public MessageReceiver() {
        init();
    }

    private void init() {
        this.socket = ChatClient.getInstance().getSocket();
        analysis = new MessageAnalysis();

    }

    @Override
    public void run() {
        receive();
    }

    // 接收消息
    private void receive() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (reader != null) {
                String message;
                while ((message = reader.readLine()) != null) {
                    JSON json = (JSON) JSON.parse(message);
                    MessagePacket packet = JSON.toJavaObject(json, MessagePacket.class);
                    /**
                     * 将消息转给MessageAnalysis处理
                     */
                    analysis.analyze(packet);
                }
            }
        } catch (Exception e) {
            System.out.println("---断开了连接--");
            // 关闭socket
            ChatClient.getInstance().shutDown();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                reader = null;
            }
        }

    }

}
