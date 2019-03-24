package com.jike.socketServer;

import java.net.Socket;

public interface SocketThread {
    void onMessage(String message);
    String getSessionId();
    void onClosed(Socket socket);
    void close();
    boolean send(String s);
}
