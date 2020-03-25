package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Room extends Entity implements Serializable {
    private String             building;
    private Set<Activity.Type> activityTypes;

    public Room(int id) {
        super(id);
    }

    public Room(int id, String building, List<Activity.Type> types) {
        super(id);
        this.building      = building;
        this.activityTypes = new HashSet<>(types);
    }

    public Room(String csvLine) {
        String[] tokens = csvLine.split(",");
        this.id            = Integer.parseInt(tokens[0].strip());
        this.building      = tokens[1].strip();
        this.activityTypes = new HashSet<>();
        for (int i = 2; i < tokens.length; i++)
             this.activityTypes.add(Activity.Type.valueOf(tokens[i].strip()));
    }

    public Set<Activity.Type> getActivityTypes() {
        return activityTypes;
    }

    @Override
    public String toString() {
        return super.toString() +
               ", " +
               building +
               " building, activity types: " +
               activityTypes.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }

    @Override
    public String toCsvLine() {
        return id + "," + building + activityTypes.stream().map(Objects::toString).collect(Collectors.joining(","));
    }
}
