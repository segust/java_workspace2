package util;

import java.sql.Timestamp;

public class TimeCorrect {
    private Timestamp time;

    public TimeCorrect(Timestamp time) {
        this.time = time;
    }

    public Timestamp getRightTime() {
        return new Timestamp(time.getTime() + 28800000);
    }
}
