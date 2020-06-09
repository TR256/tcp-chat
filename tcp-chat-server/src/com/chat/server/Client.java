package com.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class Client {

    private String name;

    private String ip;

    private int port;

    private BufferedWriter writer;

    private long heartTime;


    public Client() {
    }

    public Client(String name, Socket socket) {
        this.name = name;
        this.ip = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public long getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }
}
