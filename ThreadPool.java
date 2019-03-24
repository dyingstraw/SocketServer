package com.jike.socketServer;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: jike_study
 * @description: 线程池
 * @author: dyingstraw
 * @create: 2019-03-23 21:45
 **/
public class ThreadPool extends ThreadPoolExecutor {

    public ArrayList<Runnable> workList = new ArrayList<>();
    // 获得正在工作的线程
    public ArrayList<Runnable> getWorkList() {
        return workList;
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        workList.add(r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        workList.remove(r);
    }
}
