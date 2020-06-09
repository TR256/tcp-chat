package com.chat.client;

import com.alibaba.fastjson.JSON;
import com.chat.message.MessagePacket;
import com.chat.message.MessageType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class ChatClient {

    private final static String SERVER_IP = "127.0.0.1";

    private final static int TCP_PORT = 9966;

    private static Socket socket;

    private static boolean connecting = false;


    private ChatClient() {

    }


    public Socket getSocket() {
        return socket;
    }


    public void startUp() {
        try {
            socket = new Socket(SERVER_IP, TCP_PORT);
            connecting = true;
            System.out.println("-----连接服务器成功--------");
            /**
             * 启动客户端接收消息线程
             */
            new Thread(new MessageReceiver()).start();
            /**
             * 启动心跳线程
             */
            new Thread(new Heartbeat()).start();
            /**
             * 通知服务器我上线了
             */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username", "jax");
            String msg = JSON.toJSONString(parameters);
            MessagePacket packet = new MessagePacket(MessageType.LOGIN_REQUEST, msg);
            sendMessage(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        System.out.println("----关闭socket----");
        connecting = false;
        try {
            // 关闭socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
    }

    public void sendMessage(MessagePacket message) {
        String packet = JSON.toJSONString(message);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(packet);
            //必须newLine()
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChatClient getInstance() {
        return Client.CLIENT;
    }

    private static class Client {
        private final static ChatClient CLIENT = new ChatClient();
    }
}
