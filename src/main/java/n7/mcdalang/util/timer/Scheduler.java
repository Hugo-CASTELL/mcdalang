package n7.mcdalang.util.timer;

import n7.mcdalang.util.app.AppConfig;

import java.util.Timer;
import java.util.TimerTask;

public final class Scheduler {
    public static void runAfter(Runnable runnable, long delay) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runnable.run();
                    }
                },
                delay
        );
    }
}
