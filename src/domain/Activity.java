package domain;

import java.io.Serializable;

public class Activity extends Entity implements Serializable {
    private int  discipline;
    private Type type;
    private int  teacher;

    public Activity(int id) {
        super(id);
    }

    public Activity(int id, int discipline, Type type, int teacher) {
        super(id);
        this.discipline = discipline;
        this.type       = type;
        this.teacher    = teacher;
    }

    public Activity(String csvLine) {
        String[] tokens = csvLine.split(",");
        this.id         = Integer.parseInt(tokens[0].strip());
        this.discipline = Integer.parseInt(tokens[1].strip());
        this.type       = Activity.Type.valueOf(tokens[2].strip());
        this.teacher    = Integer.parseInt(tokens[3].strip());
    }

    public int getDiscipline() {
        return discipline;
    }

    public Type getType() {
        return type;
    }

    public int getTeacher() {
        return teacher;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + type + ", discipline id: " + discipline + ", teacher id: " + teacher;
    }

    @Override
    public String toCsvLine() {
        return this.id + "," + this.discipline + "," + this.type + "," + this.teacher;
    }

    public enum Type {
        LECTURE, SEMINAR, LABORATORY
    }
}
