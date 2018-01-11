package com.yang.activities;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2017/9/26.
 */

public class ThreadPoolManager {
    //1，把任务放入请求队列
    //定义请求队列，容量多大，需要阻塞运转的过程
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();
    //添加任务方法
    public void excute(Runnable runnable){
        try {
            queue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };
    //2，把请求队列中的人物放入线程池处理
    //定义线程池
    public ThreadPoolExecutor threadPoolExecutor;
    private ThreadPoolManager(){

    }

    //3，让机器运转起来。





    //单例
}
