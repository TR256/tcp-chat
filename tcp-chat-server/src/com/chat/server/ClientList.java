package com.chat.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class ClientList {

    // 定时检测周期
    private int T = 1000 * 60 * 5;

    private static int capacity = 10;

    {
        timingDetection();
    }

    private static Map<Integer, Client> clientMap = new HashMap<>(capacity);

    private static Map<Integer, Socket> socketMap = new HashMap<>(capacity);

    public static List<Client> clientList = new ArrayList<>(capacity);

    public static Client getClient(int port){
        return clientMap.get(port);
    }

    public static void putToClients(Client client) {
        clientMap.put(client.getPort(), client);
        clientList.add(client);
    }

    public static void putToSocket(Socket socket) {
        socketMap.put(socket.getPort(), socket);
    }

    /**
     * 移除
     * @param client
     */
    public static void remove(Client client){
        if (clientList.contains(client)){
            clientList.remove(client);
            int port = client.getPort();
            clientMap.remove(port);
            socketMap.remove(port);
        }
    }


    // 定时检测
    private void timingDetection(){
        new Thread(new TimingDetection()).start();
    }

    private class TimingDetection implements Runnable{
        @Override
        public void run() {
            while (true){
                if (clientList.size() != 0){
                    long now = System.currentTimeMillis();
                    for(Client client: clientList){
                        if (now - client.getHeartTime() > T){
                            // 判断下线
                            ClientList.remove(client);
                            // 关闭该连接
                            ServerSocketHandler.close(socketMap.get(client.getPort()));
                        }
                    }
                }
                try {
                    Thread.sleep(T);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
