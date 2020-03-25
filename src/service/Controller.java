package service;

import domain.*;
import javafx.util.Pair;
import repository.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private final Type                   fileType;
    private final String                 associationsFile;
    private final Repository<Teacher>    teacherRepo;
    private final Repository<Discipline> disciplineRepo;
    private final Repository<Activity>   activityRepo;
    private final Repository<Room>       roomRepo;
    private final Repository<Formation>  formationRepo;
    private       Set<TimeTableEntry>    timeTableEntries;

    public Controller(Repository<Teacher> teacherRepo, Repository<Discipline> disciplineRepo,
                      Repository<Activity> activityRepo, Repository<Room> roomRepo, Repository<Formation> formationRepo,
                      Type fileType, String associationsFile) {
        this.teacherRepo      = teacherRepo;
        this.disciplineRepo   = disciplineRepo;
        this.activityRepo     = activityRepo;
        this.roomRepo         = roomRepo;
        this.formationRepo    = formationRepo;
        this.fileType         = fileType;
        this.associationsFile = associationsFile;
        this.timeTableEntries = new LinkedHashSet<>();
        if (this.fileType.equals(Type.BINARY))
            this.readAssociationsBinary();
        else if (this.fileType.equals(Type.TEXT))
            this.readAssociationsText();
    }

    private void validateTeacher(String name, String email, String rank, String cnp) throws Exception {
        var errors = (name.isBlank() ? "\tname can't be empty;" : "") +
                     (!email.contains("@") ? "\temail must contain @;" : "") +
                     (cnp.isBlank() ? "\tcnp can't be empty;" : "") +
                     (Arrays.stream(Teacher.Rank.values())
                            .map(Enum::name)
                            .noneMatch(s -> s.equalsIgnoreCase(rank.strip())) ? "\tinvalid rank;" : "");
        if (!errors.isEmpty())
            throw new Exception("invalid teacher:\n" + errors);
    }

    private void validateDiscipline(String name, String credits, String type) throws Exception {
        var errors = (name.isBlank() ? "\tname can't be empty;" : "") +
                     (credits.isBlank() ? "\tcredits can't be empty;" : "") +
                     (Arrays.stream(Discipline.Type.values())
                            .map(Enum::name)
                            .noneMatch(s -> s.equalsIgnoreCase(type.strip())) ? "\tinvalid type;" : "");
        if (!errors.isEmpty())
            throw new Exception("invalid discipline:\n" + errors);
    }

    private void validateActivity(int disciplineId, String type, int teacherId) throws Exception {
        var errors = "";
        try {
            this.disciplineRepo.get(disciplineId);
        } catch (Exception e) {
            errors += "\t" + e.getMessage() + ";";
        }
        if (Arrays.stream(Activity.Type.values()).map(Enum::name).noneMatch(s -> s.equalsIgnoreCase(type.strip())))
            errors += "\tinvalid type;";
        try {
            this.teacherRepo.get(teacherId);
        } catch (Exception e) {
            errors += "\t" + e.getMessage() + ";";
        }
        if (!errors.isEmpty())
            throw new Exception("invalid activity:\n" + errors);
    }

    private void validateRoom(String building, List<String> types) throws Exception {
        var enums = Arrays.stream(Activity.Type.values()).map(Enum::name).collect(Collectors.toList());
        var errors = (building.isBlank() ? "\tinvalid name;" : "") +
                     (types.stream()
                           .map(String::strip)
                           .map(String::toUpperCase)
                           .noneMatch(enums::contains) ? "\tinvalid activity type;" : "");
        if (!errors.isEmpty())
            throw new Exception("invalid rooms:\n" + errors);
    }

    private void validateTimeSlot(String day, int startHour, int finishHour) throws Exception {
        var errors = (Arrays.stream(TimeSlot.Day.values())
                            .map(Enum::name)
                            .noneMatch(s -> s.equalsIgnoreCase(day.strip())) ? "\tinvalid day;" : "") +
                     ((startHour > finishHour) ? "\tinvalid hours;" : "");
        if (!errors.isEmpty())
            throw new Exception("invalid time slot:\n" + errors);
    }

    private void validateTimeTableEntry(int formationId, int activityId, int roomId, String day, int startHour,
                                        int finishHour) throws Exception {
        String errors = "";
        try {
            this.validateTimeSlot(day, startHour, finishHour);
        } catch (Exception e) {
            errors += "\t " + e.getMessage() + ";";
        }
        Formation formation = this.formationRepo.get(formationId);
        Activity  activity  = this.activityRepo.get(activityId);
        Room      room      = this.roomRepo.get(roomId);
        TimeSlot  timeSlot  = new TimeSlot(TimeSlot.Day.valueOf(day), startHour, finishHour);
        boolean formationActivityUniqueTimeSlot = this.timeTableEntries.stream().noneMatch(timeTableEntry ->
                                                                                                   timeTableEntry.getFormation()
                                                                                                                 .equals(formation) &&
                                                                                                   timeTableEntry.getTimeSlot()
                                                                                                                 .equals(timeSlot));
        boolean teacherUniqueTimeSlot = this.timeTableEntries.stream()
                                                             .noneMatch(timeTableEntry -> timeTableEntry.getActivity()
                                                                                                        .getTeacher() ==
                                                                                          activity.getTeacher() &&
                                                                                          timeTableEntry.getTimeSlot()
                                                                                                        .equals(timeSlot));
        boolean activityUniqueRoomTimeSlot = this.timeTableEntries.stream()
                                                                  .noneMatch(timeTableEntry -> timeTableEntry.getRoom()
                                                                                                             .equals(room) &&
                                                                                               timeTableEntry.getTimeSlot()
                                                                                                             .equals(timeSlot));
        boolean formationUniqueActivity = this.timeTableEntries.stream()
                                                               .noneMatch(timeTableEntry -> timeTableEntry.getFormation()
                                                                                                          .equals(formation) &&
                                                                                            timeTableEntry.getActivity()
                                                                                                          .equals(activity));
        boolean validRoomActivities = room.getActivityTypes().contains(activity.getType());

        errors += (formationActivityUniqueTimeSlot ? "" : "\tformation has an activity at the given time;") +
                  (teacherUniqueTimeSlot ? "" : "\tteacher has an activity at the given time;") +
                  (activityUniqueRoomTimeSlot ? "" : "\tthere is an activity in the given room at the given time;") +
                  (formationUniqueActivity ? "" : "\tformation already has the given activity;") +
                  (validRoomActivities ? "" : "\tactivity can't take place in the given room;");
        if (!errors.isEmpty())
            throw new Exception("invalid timetable entry:\n" + errors);

    }

    private void readAssociationsText() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.associationsFile));
            String         line;
            while ((line = br.readLine()) != null) {
                String[]  tokens    = line.split(",");
                Formation formation = this.formationRepo.get(Integer.parseInt(tokens[0].strip()));
                Activity  activity  = this.activityRepo.get(Integer.parseInt(tokens[1].strip()));
                Room      room      = this.roomRepo.get(Integer.parseInt(tokens[2].strip()));
                TimeSlot timeSlot = new TimeSlot(TimeSlot.Day.valueOf(tokens[3].strip()),
                                                 Integer.parseInt(tokens[4].strip()),
                                                 Integer.parseInt(tokens[5].strip())
                );
                this.timeTableEntries.add(new TimeTableEntry(formation, activity, room, timeSlot));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("can't read associations from text file");
            e.getStackTrace();
            System.exit(0);
        }
    }

    private void writeAssociationsText() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.associationsFile));
            for (TimeTableEntry timeTableEntry : this.timeTableEntries)
                bw.write(timeTableEntry.toString() + System.lineSeparator());
            bw.close();
        } catch (Exception e) {
            System.out.println("can't write associations to text file");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void readAssociationsBinary() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.associationsFile));
            this.timeTableEntries = (Set<TimeTableEntry>) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println("can't read associations from binary file");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void writeAssociationsBinary() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.associationsFile));
            out.writeObject(this.timeTableEntries);
            out.close();
        } catch (Exception e) {
            System.out.println("can't write associations to binary file");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Repository<Teacher> getTeacherRepo() {
        return teacherRepo;
    }

    public Repository<Discipline> getDisciplineRepo() {
        return disciplineRepo;
    }

    public Repository<Activity> getActivityRepo() {
        return activityRepo;
    }

    public Repository<Room> getRoomRepo() {
        return roomRepo;
    }

    public Repository<Formation> getFormationRepo() {
        return formationRepo;
    }

    public Set<TimeTableEntry> getTimeTableEntries() {
        return timeTableEntries;
    }

    public void addTeacher(int id, String name, String email, String rank, String cnp) throws Exception {
        this.validateTeacher(name, email, rank, cnp);
        this.teacherRepo.add(new Teacher(id, name, email, Teacher.Rank.valueOf(rank), Long.parseLong(cnp)));
    }

    public void removeTeacher(int id) throws Exception {
        this.teacherRepo.remove(id);
    }

    public void updateTeacher(int oldId, int id, String name, String email, String rank, String cnp) throws Exception {
        this.validateTeacher(name, email, rank, cnp);
        this.teacherRepo.update(oldId, new Teacher(id, name, email, Teacher.Rank.valueOf(rank), Long.parseLong(cnp)));
    }

    public void addDiscipline(int id, String name, String credits, String type) throws Exception {
        this.validateDiscipline(name, credits, type);
        this.disciplineRepo.add(new Discipline(id, name, Integer.parseInt(credits), Discipline.Type.valueOf(type)));
    }

    public void removeDiscipline(int id) throws Exception {
        this.disciplineRepo.remove(id);
    }

    public void updateDiscipline(int oldId, int id, String name, String credits, String type) throws Exception {
        this.validateDiscipline(name, credits, type);
        this.disciplineRepo.update(oldId,
                                   new Discipline(id, name, Integer.parseInt(credits), Discipline.Type.valueOf(type))
        );
    }

    public void addActivity(int id, int disciplineId, String type, int teacherId) throws Exception {
        this.validateActivity(disciplineId, type, teacherId);
        this.activityRepo.add(new Activity(id, disciplineId, Activity.Type.valueOf(type), teacherId));
    }

    public void removeActivity(int id) throws Exception {
        this.activityRepo.remove(id);
    }

    public void updateActivity(int oldId, int id, int disciplineId, String type, int teacherId) throws Exception {
        this.validateActivity(disciplineId, type, teacherId);
        this.activityRepo.update(oldId, new Activity(id, disciplineId, Activity.Type.valueOf(type), teacherId));
    }

    public void addRoom(int id, String building, List<String> types) throws Exception {
        this.validateRoom(building, types);
        var activityTypes = types.stream().map(Activity.Type::valueOf).collect(Collectors.toList());
        this.roomRepo.add(new Room(id, building, activityTypes));
    }

    public void removeRoom(int id) throws Exception {
        this.roomRepo.remove(id);
    }

    public void updateRoom(int oldId, int id, String building, List<String> types) throws Exception {
        this.validateRoom(building, types);
        var activityTypes = types.stream().map(Activity.Type::valueOf).collect(Collectors.toList());
        this.roomRepo.update(oldId, new Room(id, building, activityTypes));
    }

    public void addFormation(int id, String formation) throws Exception {
        var tokens = formation.split(",");
        if (tokens.length > 3)
            throw new Exception("invalid formation values");
        this.formationRepo.add(new Formation(id, tokens));
    }

    public void removeFormation(int id) throws Exception {
        this.formationRepo.remove(id);
    }

    public void updateFormation(int oldId, int id, String formation) throws Exception {
        var tokens = formation.split(",");
        if (tokens.length > 3)
            throw new Exception("invalid formation values");
        this.formationRepo.add(new Formation(id, tokens));
        this.formationRepo.update(oldId, new Formation(id, tokens));
    }

    public void addTimeTableEntry(int formationId, int activityId, int roomId, String day, int startHour,
                                  int finishHour) throws Exception {
        this.validateTimeTableEntry(formationId, activityId, roomId, day, startHour, finishHour);
        this.timeTableEntries.add(new TimeTableEntry(this.formationRepo.get(formationId),
                                                     this.activityRepo.get(activityId),
                                                     this.roomRepo.get(roomId),
                                                     new TimeSlot(TimeSlot.Day.valueOf(day), startHour, finishHour)
        ));
        if (this.fileType.equals(Type.BINARY))
            this.writeAssociationsBinary();
        else if (this.fileType.equals(Type.TEXT))
            this.writeAssociationsText();
    }

    public void removeTimeTableEntry(int formationId, int activityId, int roomId, String day, int startHour,
                                     int finishHour) throws Exception {
        if (!this.timeTableEntries.removeIf(ttEntry -> ttEntry.getFormation().getId() == formationId &&
                                                       ttEntry.getActivity().getId() == activityId &&
                                                       ttEntry.getRoom().getId() == roomId &&
                                                       ttEntry.getTimeSlot().getDay().name().equals(day) &&
                                                       ttEntry.getTimeSlot().getStartHour() == startHour &&
                                                       ttEntry.getTimeSlot().getFinishHour() == finishHour))
            throw new Exception("association not found");
        if (this.fileType.equals(Type.BINARY))
            this.writeAssociationsBinary();
        else if (this.fileType.equals(Type.TEXT))
            this.writeAssociationsText();
    }

    public ArrayList<Teacher> getTeachersByRank(String rank) throws Exception {
        if (Arrays.stream(Teacher.Rank.values()).map(Enum::name).noneMatch(s -> s.equals(rank)))
            throw new Exception("invalid rank");
        return this.teacherRepo.get()
                               .stream()
                               .filter(teacher -> teacher.getRank() == Teacher.Rank.valueOf(rank))
                               .sorted(Comparator.comparing(Teacher::getName))
                               .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Pair<Activity, TimeSlot>> getActivitiesByRoom(int roomId) {
        return this.timeTableEntries.stream()
                                    .filter(ttEntry -> ttEntry.getRoom().getId() == roomId)
                                    .sorted(Comparator.comparing(TimeTableEntry::getTimeSlot))
                                    .map(ttEntry -> new Pair<>(ttEntry.getActivity(), ttEntry.getTimeSlot()))
                                    .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<TimeTableEntry> getTimeTable(int formationId) {
        return this.timeTableEntries.stream().filter(ttEntry -> ttEntry.getFormation().getId() == formationId).sorted(
                Comparator.comparing(TimeTableEntry::getTimeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    public enum Type {
        NONE, TEXT, BINARY
    }
}
