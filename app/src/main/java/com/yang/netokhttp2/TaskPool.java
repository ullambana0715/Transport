package com.yang.netokhttp2;



import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/8/29.
 */
public class TaskPool {
    private LinkedList<RunningTask> mQueue = new LinkedList<RunningTask>();//该队列暂时没用，但是可以获取当前线程的数量
    private ExecutorService mExecutorService ;//一个线程池，管理所有的网络请求。
    private RunningTask taskNow;//获取当前正在执行的线程
    public TaskPool(){
        mExecutorService = Executors.newCachedThreadPool();
    }

    //另类的单例模式
    public static TaskPool getInstance(){
        return NewTaskPool.instance;
    }
    public static class NewTaskPool{
        public static TaskPool instance = new TaskPool();
    }

    public void excute(RunningTask task){//该线程在excute中通过handler发送消息传递result出去。
        taskNow = task;
        task.setLiftCycleListener(new RunningTask.TaskLifeCycleListener() {
            @Override
            public void onStart(RunningTask task) {
                mQueue.add(task);
            }

            @Override
            public void onFinish(RunningTask task) {
                mQueue.remove(task);
            }
        });

        mExecutorService.execute(task);
        System.out.println("thread pool now : "+mExecutorService.toString());
    }

    public Future submit(Callable callable){//该线程可以通过call，返回一个Future对象，获取到Future对象后再通过handler传递Response出去
        return mExecutorService.submit(callable);
    }

    RunningTask findTaskWithTag(Object tag){
        for (RunningTask task : mQueue){
            if (tag == task.getTag()){
                return task;
            }
        }
        return null;
    }

    void cancleTask(LoadingHandler handler,Object tag){//cancel的过程task可能还没被加入队列
        RunningTask task =  findTaskWithTag(tag);//返回的task也就同样可能为空
        if (null == task){
            if (null != taskNow){//cancel的过程同样taskNow也可能没被赋值
                taskNow.onTimeAccessHandler(handler);
                taskNow.cancle();
            }
        }else {
            task.cancle();
        }
    }
}
