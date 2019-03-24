package com.jike.socketServer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @program: jike_study
 * @description:
 * @author: dyingstraw
 * @create: 2019-03-23 21:56
 **/
public class Main {
    public static void main(String[] args) throws IOException {
        // 启动监听线程
        SocketServer socketServer = new SocketServer("localhost",8888);
        new Thread(socketServer).start();
        //启动交互线程
        Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("请输入要发送的连接的端口号:");
                    String sid = scanner.nextLine();
                    System.out.println("请输入要发送的内容:");
                    String ccc = scanner.nextLine();

                    Iterator<Runnable> it = ((ThreadPool) (socketServer.getPoolExecutor())).getWorkList().iterator();
                    while (it.hasNext()){
                        SocketThreadImpl temp = (SocketThreadImpl) it.next();
                        System.out.println(temp.getSessionId());
                        if (temp.getSessionId().equals(sid)){
                            temp.send(ccc);
                        }
                    }
                }
            }
        };
        t.start();
    }
}
