package com.github.donkirkby.vograbulary;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidScheduler implements Scheduler {
    private Timer timer;
    private TimerTask timerTask;
    
    public AndroidScheduler() {
        boolean isDaemon = true;
        timer = new Timer(isDaemon);
    }

    @Override
    public void scheduleRepeating(final Runnable task, int periodMilliseconds) {
        if (this.timerTask != null) {
            throw new IllegalStateException("There is already a scheduled task.");
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        timer.scheduleAtFixedRate(
                timerTask,
                periodMilliseconds,
                periodMilliseconds);
    }

    @Override
    public void cancel(Runnable task) {
        if (timerTask != null) {
            timerTask.cancel();
        }
        this.timerTask = null;
    }

}
