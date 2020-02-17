package com.maximus.productivityappfinalproject.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor mainThread;


    AppExecutors(Executor mainThread, Executor fd) {
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new MainThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT));
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }

    }
}
