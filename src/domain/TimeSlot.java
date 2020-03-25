package domain;

import java.util.Objects;

public class TimeSlot extends Entity implements Comparable<TimeSlot> {
    private final Day day;
    private final int startHour;
    private final int finishHour;

    public TimeSlot(Day day, int startHour, int finishHour) {
        this.day        = day;
        this.startHour  = startHour;
        this.finishHour = finishHour;
    }

    public Day getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getFinishHour() {
        return finishHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return startHour == timeSlot.startHour && finishHour == timeSlot.finishHour && day == timeSlot.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), day, startHour, finishHour);
    }

    @Override
    public String toCsvLine() {
        return this.toString();
    }

    @Override
    public String toString() {
        return day + "," + startHour + "," + finishHour;
    }

    @Override
    public int compareTo(TimeSlot o) {
        if (this.day == o.day) {
            if (this.startHour == o.startHour)
                return Integer.compare(this.finishHour, o.finishHour);
            return Integer.compare(this.startHour, o.startHour);
        }
        return Integer.compare(this.day.ordinal(), o.day.ordinal());
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    }
}
