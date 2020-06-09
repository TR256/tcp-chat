package com.chat.server;

import com.alibaba.fastjson.JSON;
import com.chat.message.MessageAnalysis;
import com.chat.message.MessagePacket;

import java.io.*;
import java.net.Socket;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/03
 * @Description:
 */
public class ServerSocketHandler implements Runnable {
    private Socket socket = null;
    private MessageAnalysis analysis = null;


    public ServerSocketHandler(Socket socket) {
        this.socket = socket;
        this.analysis = new MessageAnalysis();
    }

    @Override
    public void run() {
        readMessage();
    }

    /**
     * 读取从客户端传来的消息
     */
    private void readMessage() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                JSON json = (JSON) JSON.parse(message);
                MessagePacket packet = JSON.toJavaObject(json, MessagePacket.class);
                analysis.analyze(socket, packet);
            }
        } catch (Exception ex) {
            System.out.println("---客户端掉线了-----");
            close(socket);
        }
    }

    /**
     * 关闭连接
     * @param socket
     */
    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
                System.out.println("关闭连接: " + socket.getInetAddress().getHostAddress() + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }


    /**
     * 发送消息到客户端
     */
    public static void sendMessage(Socket socket, MessagePacket packet) {
        System.out.println("---服务器发出消息---");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = JSON.toJSONString(packet);
            writer.write(msg);
            /**
             * writer.newLine()很关键
             */
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送消息到所有客户端
     */
    public static void sendMessageToWorld(MessagePacket packet) {
        System.out.println("---服务器发出消息---");
        BufferedWriter writer = null;
        try {
            for (Client client : ClientList.clientList) {
                writer = client.getWriter();
                String msg = JSON.toJSONString(packet);
                writer.write(msg);
                /**
                 * writer.newLine()很关键
                 */
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
