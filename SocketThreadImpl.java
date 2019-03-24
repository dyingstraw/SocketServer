package com.jike.socketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @program: jike_study
 * @description: 消息接受线程
 * @author: dyingstraw
 * @create: 2019-03-23 21:51
 **/
public class SocketThreadImpl implements Runnable, SocketThread {
    private Queue<String> messageQueue = new PriorityQueue<>();
    private Socket socket;
    private String sessionId;
    private OutputStream o;

    public SocketThreadImpl() {
    }

    public SocketThreadImpl(Socket socket, String sessionId) throws IOException {
        this.socket = socket;
        this.sessionId = sessionId;
        this.o = socket.getOutputStream();
    }
    // 获取sessionId
    public String getSessionId() {
        return sessionId;
    }

    public Socket getSocket() throws IOException {

        return socket;
    }

    public void setSocket(Socket socket) throws IOException {
        this.o = socket.getOutputStream();
        this.socket = socket;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // 获得消息监听
    public void onMessage(String message){
        System.out.println(message);
    }

    // 线程关闭监听
    public void onClosed(Socket socket){
        System.out.println("onClosed");
    }
    // 关闭线程
    public void close(){
        Thread.currentThread().interrupt();
    }
    // socket发送
    public boolean send(String s){
        try {
            o.write(s.getBytes());
            o.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public void run()  {

        try {
            InputStream in = null;
            // 得到输入流
            in = socket.getInputStream();

            System.out.println("connect from:"+socket.getLocalAddress() +":" + socket.getPort());
            while (true){
                // 读取客户端发来的信息
                if (in.available()>0){
                    byte[] buff = new byte[in.available()];
                    in.read(buff);
                    onMessage(Arrays.toString(buff));
                }
                // 接受外部interrupted指令，退出线程
                if (Thread.currentThread().isInterrupted()){
                    throw new Exception("SocketThreadImpl is dead!");
                }
                // 查询是否和客户端断开通讯
                if (in.read()==-1){
                    throw new Exception("Socket closed!");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
