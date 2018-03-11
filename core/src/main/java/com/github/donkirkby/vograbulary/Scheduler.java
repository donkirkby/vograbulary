package com.github.donkirkby.vograbulary;

public interface Scheduler {
    /** Schedule a task to repeat.
     */
    public void scheduleRepeating(Runnable task, int periodMilliseconds);
    
    /** Cancel a scheduled task.
     * 
     * If the task is not scheduled or is null, do nothing.
     */
    public void cancel(Runnable task);
}
