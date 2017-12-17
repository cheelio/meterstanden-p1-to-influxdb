package nl.cheelio.p1influxwriter.p1;

import java.time.LocalDateTime;

public class PowerFailure {
    private LocalDateTime dateTime;
    private int durationInSeconds;

    public PowerFailure(LocalDateTime dateTime, int durationInSeconds) {
        this.dateTime = dateTime;
        this.durationInSeconds = durationInSeconds;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    @Override
    public String toString() {
        return "PowerFailure{" +
                "dateTime=" + dateTime +
                ", durationInSeconds=" + durationInSeconds +
                '}';
    }
}
