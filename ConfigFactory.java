package com.jike.socketServer;

import java.awt.geom.IllegalPathStateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * @program: jike_study
 * @description: 配置工程
 * @author: dyingstraw
 * @create: 2019-03-24 11:36
 **/
public class ConfigFactory {
    private static String socketThreadName="";

    private static String threadPoolName="";


    public static SocketThread buildSocketThread(String socketThreadName, Socket socket,String sessionId) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {
        Class<?> clzz = Class.forName(socketThreadName);
        SocketThread o = (SocketThread) clzz.newInstance();
        o.setSessionId(sessionId);
        o.setSocket(socket);
        return o;
    }
    public static SocketThread buildSocketThread(Socket socket,String sessionId) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {

        if (socketThreadName.equals("")){
            throw new IllegalPathStateException("请先配置ConfigFactory工厂类");
        }
        Class<?> clzz = Class.forName(socketThreadName);
        SocketThread o = (SocketThread) clzz.newInstance();
        o.setSessionId(sessionId);
        o.setSocket(socket);
        return o;
    }


    public static String getSocketThreadName() {
        return socketThreadName;
    }

    public static String getThreadPoolName() {
        return threadPoolName;
    }

    public static void setSocketThreadName(String socketThreadName) {
        ConfigFactory.socketThreadName = socketThreadName;
    }

    public static void setThreadPoolName(String threadPoolName) {
        ConfigFactory.threadPoolName = threadPoolName;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IOException {

        SocketThread s = buildSocketThread("com.jike.socketServer.SocketThreadImpl", new Socket(), "9960");
        System.out.println(s);
        SocketThread s1 = buildSocketThread("com.jike.socketServer.SocketThreadImpl", new Socket(), "9960");
        System.out.println(s1);
    }
}
