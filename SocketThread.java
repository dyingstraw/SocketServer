package com.jike.socketServer;

import java.io.IOException;
import java.net.Socket;

public interface SocketThread extends Runnable{
    // SocketThread(Socket socket,String sessionId);
    void setSessionId(String sessionId);
    void setSocket(Socket socket) throws IOException;
    void onMessage(String message);
    String getSessionId();
    void onClosed(Socket socket);
    void close();
    boolean send(String s);
}
