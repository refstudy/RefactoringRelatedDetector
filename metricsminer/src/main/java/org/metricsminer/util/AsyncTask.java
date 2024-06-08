package org.metricsminer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTask<T> {

    private Collection<Callable<T>> tasks = new ArrayList<>();
    private int nThreads = 5;

    public void add(Callable<T> task) {
        tasks.add(task);
    }

    public AsyncTask<T> nThreads(int nThreads) {
        this.nThreads = nThreads;
        return this;
    }

    public void runParallelCalls() {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        try {
            executor.invokeAll(tasks);
            executor.shutdown();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
