package com.github.donkirkby.vograbulary;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AndroidScheduler implements Scheduler {
    private Timer timer;
    private Map<Runnable, TimerTask> timerTasks = new HashMap<>();
    
    public AndroidScheduler() {
        boolean isDaemon = true;
        timer = new Timer(isDaemon);
    }

    @Override
    public void scheduleRepeating(final Runnable task, int periodMilliseconds) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        timerTasks.put(task, timerTask);
        timer.scheduleAtFixedRate(
                timerTask,
                periodMilliseconds,
                periodMilliseconds);
    }

    @Override
    public void cancel(Runnable task) {
        TimerTask timerTask = timerTasks.remove(task);
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

}
