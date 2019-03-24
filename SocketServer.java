package com.jike.socketServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: jike_study
 * @description: socket
 * @author: dyingstraw
 * @create: 2019-03-23 21:58
 **/
public class SocketServer implements Runnable{

    private String address="127.0.0.1";
    private int port = 8888;
    // 线程池的阻塞队列
    private ArrayBlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(15);
    // 为socket创建线程池
    ThreadPoolExecutor poolExecutor = new ThreadPool(
            1024,1024+512,50,
            TimeUnit.SECONDS,queue);

    public SocketServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ThreadPoolExecutor getPoolExecutor() {
        return poolExecutor;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    @Override
    public void run() {
        try {
            server();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    // 监听socket连接
    public void server() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ServerSocket ssocket = new ServerSocket();
        // 绑定端口
        ssocket.bind(new InetSocketAddress(this.getPort()));
        while (true){
            Socket socket = ssocket.accept();
            if (socket!=null){
                // 把新进的socket放入线程池,标识随意
                // SocketThreadImpl r = new SocketThreadImpl(socket, String.valueOf(socket.getPort()));
                SocketThread r = ConfigFactory.buildSocketThread(socket, String.valueOf(UUID.randomUUID().hashCode()) );
                poolExecutor.execute(r);
            }
        }
    }
}
