package domain;

import java.io.Serializable;

public class Formation extends Entity implements Serializable {
    private Type   type;
    private String specialisation = null;
    private String group          = null;
    private String subgroup       = null;

    public Formation(int id) {
        super(id);
    }

    public Formation(int id, String... args) {
        super(id);
        this.type = Type.values()[args.length - 1];
        switch (type) {
            case SUBGROUP: this.subgroup = args[2];
            case GROUP: this.group = args[1];
            case SPECIALISATION: this.specialisation = args[0];
        }
    }

    public Formation(String csvLine) {
        this(Integer.parseInt(csvLine.split(",", 2)[0]), csvLine.split(",", 2)[1].split(","));
    }

    @Override
    public String toString() {
        return super.toString() +
               ", type: " +
               type +
               ", specialisation: " +
               specialisation +
               ((group == null) ? "" : (", group: " + group)) +
               ((subgroup == null) ? "" : (", subgroup: " + subgroup));
    }

    @Override
    public String toCsvLine() {
        return id +
               "," +
               specialisation +
               ((group == null) ? "" : "," + group) +
               ((subgroup == null) ? "" : "," + subgroup);
    }

    private enum Type {
        SPECIALISATION, GROUP, SUBGROUP
    }
}
