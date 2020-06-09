package com.chat.message;


import com.alibaba.fastjson.JSON;
import com.chat.server.Client;
import com.chat.server.ClientList;
import com.chat.server.ServerSocketHandler;

import java.net.Socket;
import java.util.Map;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/08
 * @Description:
 */
public class MessageAnalysis {

    /**
     * 三个周期没有心跳包传来则判定该客户端下线
     */
    private int T = 1000 * 60 * 3;

    public void analyze(Socket socket, MessagePacket packet) {
        System.out.println("---解析来自客户端的消息----");
        System.out.println(packet);
        int type = packet.getType();
        switch (type) {
            case MessageType.HEARTBEAT:
                heartbeatMessage(socket, packet);
                break;
            case MessageType.LOGIN_REQUEST:
                loginRequestMessage(socket, packet);
                break;
            default:
                System.out.println("---未知消息----");
                break;
        }
    }

    private void heartbeatMessage(Socket socket, MessagePacket packet) {
        long nowTime = Long.parseLong(packet.getMessage());
        Client client = ClientList.getClient(socket.getPort());
        long lastTime = client.getHeartTime();
        client.setHeartTime(nowTime);
        /**
         * 更新数据
         */
        ClientList.putToClients(client);

        /**
         * 计算
         */
        if (lastTime != 0){
            if (nowTime - lastTime > T ) {
                // 判断下线
                ClientList.remove(client);
                // 关闭该连接
                ServerSocketHandler.close(socket);
            }
        }
    }

    /**
     * 登录请求
     * @param socket
     * @param packet
     */
    private void loginRequestMessage(Socket socket, MessagePacket packet) {
        String json = packet.getMessage();
        Map parameters = JSON.parseObject(json, Map.class);
        String username = (String) parameters.get("username");
        Client client = new Client(username.trim(), socket);
        ClientList.putToClients(client);
        ClientList.putToSocket(socket);
        /**
         * 返回消息
         */
        packet = new MessagePacket(MessageType.LOGIN_RESPONSE, "来了,老弟!");
//        ServerSocketHandler.sendMessage(socket, packet);
        /**
         * 转发消息:告诉所有人, 有新人上线
         */
        ServerSocketHandler.sendMessageToWorld(packet);
    }


}
