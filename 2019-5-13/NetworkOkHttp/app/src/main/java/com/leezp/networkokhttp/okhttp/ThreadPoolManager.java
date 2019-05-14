package com.leezp.networkokhttp.okhttp;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 线程池管理类
public class ThreadPoolManager {
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return threadPoolManager;
    }

    // 创建队列
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    // 将异步任务添加到队列中
    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        addTask(r);
                    }
                });
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    // 创建"叫号员"线程，让队列和线程池进行交互
    public Runnable coreThread = new Runnable() {
        Runnable runn = null;

        @Override
        public void run() {
            while (true) {
                try {
                    runn = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runn);
            }
        }
    };

    // 创建延迟队列（重试机制是要延迟一定的时间才会进行重试）
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    // 将失败的 httpTask 添加到延迟队列
    public void addDelayTask(HttpTask ht) {
        if (ht != null) {
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    // 创建延迟线程"失败任务的叫号员"，不停从延迟队列中获取请求，并提交给线程池
    public Runnable delayThread = new Runnable() {
        HttpTask ht = null;

        @Override
        public void run() {
            while (true) {
                try {
                    ht = mDelayQueue.take();
                    if (ht.getRetryCount() < 3) {
                        mThreadPoolExecutor.execute(ht);
                        ht.setRetryCount(ht.getRetryCount() + 1);
                        Log.e("==== 重试机制 ====", ht.getRetryCount() + "  " + System.currentTimeMillis());
                    } else {
                        Log.e("==== 重试机制 ====","总是执行不成功，直接放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
