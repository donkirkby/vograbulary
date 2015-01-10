package com.github.donkirkby.vograbulary;

public interface Scheduler {
    public void scheduleRepeating(Runnable task, int periodMilliseconds);
    
    public void cancel(Runnable task);
}
