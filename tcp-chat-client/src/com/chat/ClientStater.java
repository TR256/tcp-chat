package com.chat;

import com.chat.client.ChatClient;

/**
 * @author: 孟祥洪
 * @Date: 2020/06/09
 * @Description:
 */
public class ClientStater {

    public static void main(String[] args) {
        ChatClient.getInstance().startUp();
    }
}
