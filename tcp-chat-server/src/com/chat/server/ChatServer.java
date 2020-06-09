package com.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class ChatServer {

    private static ServerSocket serverSocket;

    private static ThreadPoolExecutor threadPool;

    private static boolean running = false;
    /**
     * 用来标识客户端
     */
    private static int ID = 0;

    /**
     * 接受客户端连接
     */
    private static final int TCP_PORT = 9966;

    private ChatServer() {
        startUp();
    }

    public void startUp() {
        try {
            serverSocket = new ServerSocket(TCP_PORT);
            running = true;
            System.out.println("服务器创建成功：" + TCP_PORT);
            /**
             * 手动创建一个线程池
             */
            threadPool = new ThreadPoolExecutor(10, 15, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
            while (running) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ServerSocketHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        running = false;
        try {
            // 关闭socket
            serverSocket.close();
            //关闭线程池
            threadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ChatServer getInstance() {
        System.out.println("---获取实例方法----");
        return SingletonServer.SERVER;
    }

    private static class SingletonServer {
        private static final ChatServer SERVER = new ChatServer();
    }


}
