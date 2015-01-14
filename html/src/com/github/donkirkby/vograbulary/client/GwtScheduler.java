package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.Scheduler;
import com.google.gwt.user.client.Timer;

public class GwtScheduler implements Scheduler {
    Timer timer;

    @Override
    public void scheduleRepeating(final Runnable task, int periodMilliseconds) {
        if (timer != null) {
            throw new IllegalStateException("A task is already scheduled.");
        }
        timer = new Timer() {
            @Override
            public void run() {
                task.run();
            }
        };
        timer.scheduleRepeating(periodMilliseconds);
    }

    @Override
    public void cancel(Runnable task) {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }
    
}
