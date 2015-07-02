package com.github.donkirkby.vograbulary.client;

import java.util.HashMap;
import java.util.Map;

import com.github.donkirkby.vograbulary.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;

public class GwtScheduler implements Scheduler {
    private Map<Runnable, Timer> timers = new HashMap<>();
    private com.google.gwt.core.client.Scheduler scheduler = 
            com.google.gwt.core.client.Scheduler.get();

    @Override
    public void scheduleRepeating(final Runnable task, int periodMilliseconds) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                task.run();
            }
        };
        timers.put(task, timer);
        timer.scheduleRepeating(periodMilliseconds);
    }

    @Override
    public void cancel(Runnable task) {
        Timer timer = timers.remove(task);
        if (timer != null) {
            timer.cancel();
        }
    }
    
    public void scheduleDeferred(final Runnable task) {
        scheduler.scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                task.run();
            }
        });
    }
}
