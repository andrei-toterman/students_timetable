package service;

import domain.Activity;
import domain.Formation;
import domain.Room;
import domain.TimeSlot;

import java.io.Serializable;
import java.util.Objects;

public class TimeTableEntry implements Serializable {
    private final Formation formation;
    private final Activity  activity;
    private final Room      room;
    private final TimeSlot  timeSlot;

    public TimeTableEntry(Formation formation, Activity activity, Room room, TimeSlot timeSlot) {
        this.formation = formation;
        this.activity  = activity;
        this.room      = room;
        this.timeSlot  = timeSlot;
    }

    public Formation getFormation() {
        return formation;
    }

    public Activity getActivity() {
        return activity;
    }

    public Room getRoom() {
        return room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return formation.getId() + "," + activity.getId() + "," + room.getId() + "," + timeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeTableEntry that = (TimeTableEntry) o;
        return Objects.equals(formation, that.formation) && Objects.equals(activity, that.activity) && Objects.equals(
                room,
                that.room
        ) && Objects.equals(timeSlot, that.timeSlot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formation, activity, room, timeSlot);
    }
}
