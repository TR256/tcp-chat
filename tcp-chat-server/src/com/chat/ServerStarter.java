package com.chat;

import com.chat.server.ChatServer;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:服务器启动类
 */
public class ServerStarter {

    public static void main(String[] args) {
        ChatServer server = ChatServer.getInstance();
        server.startUp();
    }
}
