package domain;

import java.io.Serializable;

public class Discipline extends Entity implements Serializable {
    private String name;
    private int    credits;
    private Type   type;

    public Discipline(int id) {
        super(id);
    }

    public Discipline(int id, String name, int credits, Type type) {
        super(id);
        this.name    = name;
        this.credits = credits;
        this.type    = type;
    }

    public Discipline(String csvLine) {
        String[] tokens = csvLine.split(",");
        this.id      = Integer.parseInt(tokens[0].strip());
        this.name    = tokens[1].strip();
        this.credits = Integer.parseInt(tokens[2].strip());
        this.type    = Discipline.Type.valueOf(tokens[3].strip());
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + name + ", " + credits + " credits, " + type;
    }

    @Override
    public String toCsvLine() {
        return this.id + "," + this.name + "," + this.credits + "," + this.type;
    }

    public enum Type {
        MANDATORY, OPTIONAL, FACULTATIVE
    }
}
