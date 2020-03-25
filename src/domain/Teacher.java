package domain;

import java.io.Serializable;

public class Teacher extends Entity implements Serializable {
    private String name;
    private String email;
    private Rank   rank;
    private long   cnp;

    public Teacher(int id) {
        super(id);
    }

    public Teacher(int id, String name, String email, Rank rank, long cnp) {
        super(id);
        this.name  = name;
        this.email = email;
        this.rank  = rank;
        this.cnp   = cnp;
    }

    public Teacher(String csvLine) {
        String[] tokens = csvLine.split(",");
        this.id    = Integer.parseInt(tokens[0].strip());
        this.name  = tokens[1].strip();
        this.email = tokens[2].strip();
        this.rank  = Rank.valueOf(tokens[3].strip());
        this.cnp   = Long.parseLong(tokens[4].strip());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Rank getRank() {
        return rank;
    }

    public long getCnp() {
        return cnp;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + rank + " " + name + ", cnp: " + cnp + ", " + email;
    }

    @Override
    public String toCsvLine() {
        return id + "," + name + "," + email + "," + rank + "," + cnp;
    }

    public enum Rank {
        LECTURER, PROFESSOR, ASSISTANT
    }
}
