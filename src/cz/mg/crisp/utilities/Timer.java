package cz.mg.crisp.utilities;

import cz.mg.annotations.classes.Utility;

public @Utility class Timer {
    private final int delay;
    private long time;

    public Timer(int delay) {
        this.delay = delay;
        this.time = getTime();
    }

    public boolean tick() {
        long currentTime = getTime();
        if (currentTime > time) {
            time = currentTime;
            return true;
        } else {
            return false;
        }
    }

    private long getTime() {
        return System.currentTimeMillis() / delay;
    }
}
