package ui;

import domain.*;
import javafx.util.Pair;
import service.Controller;
import service.TimeTableEntry;

import java.util.Arrays;
import java.util.Scanner;

class Console {
    private final Controller ctrl;

    Console(Controller ctrl) {
        this.ctrl = ctrl;
    }

    private void printMenu() {
        System.out.println("\n1. print teachers" +
                           "\n2. add teacher" +
                           "\n3. remove teacher" +
                           "\n4. update teacher" +
                           "\n5. print disciplines" +
                           "\n6. add discipline" +
                           "\n7. remove discipline" +
                           "\n8. update discipline" +
                           "\n9. print activities" +
                           "\n10. add activity" +
                           "\n11. remove activity" +
                           "\n12. update activity" +
                           "\n13. print rooms" +
                           "\n14. add room" +
                           "\n15. remove room" +
                           "\n16. update room" +
                           "\n17. print formations" +
                           "\n18. add formation" +
                           "\n19. remove formation" +
                           "\n20. update formation" +
                           "\n21. print associations" +
                           "\n22. add association" +
                           "\n23. remove association" +
                           "\n24. get teachers by rank" +
                           "\n25. get activities by room" +
                           "\n26. get formation timetable" +
                           "\n0. exit");
    }

    private void printTeachers() {
        System.out.println("current teachers:");
        for (Teacher t : this.ctrl.getTeacherRepo().get())
            System.out.println(t);
    }

    private void addTeacher() {
        Scanner input = new Scanner(System.in);
        System.out.print("id: ");
        int id = input.nextInt();
        System.out.print("name: ");
        String name = input.next();
        System.out.print("email: ");
        String email = input.next();
        System.out.print("rank: ");
        String rank = input.next();
        System.out.print("cnp: ");
        String cnp = input.next();
        try {
            this.ctrl.addTeacher(id, name, email, rank, cnp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeTeacher() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the teacher id: ");
        int id = input.nextInt();
        try {
            this.ctrl.removeTeacher(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTeacher() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the teacher id: ");
        int oldId = input.nextInt();
        System.out.print("new id: ");
        int id = input.nextInt();
        System.out.print("new name: ");
        String name = input.next();
        System.out.print("new email: ");
        String email = input.next();
        System.out.print("new rank: ");
        String rank = input.next();
        System.out.print("new cnp: ");
        String cnp = input.next();
        try {
            this.ctrl.updateTeacher(oldId, id, name, email, rank, cnp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printDisciplines() {
        System.out.println("current disciplines:");
        for (Discipline d : this.ctrl.getDisciplineRepo().get())
            System.out.println(d);
    }

    private void addDiscipline() {
        Scanner input = new Scanner(System.in);
        System.out.print("id: ");
        int id = input.nextInt();
        System.out.print("name: ");
        String name = input.next();
        System.out.print("credits: ");
        String credits = input.next();
        System.out.print("type (MANDATORY, OPTIONAL, FACULTATIVE): ");
        String type = input.next();
        try {
            this.ctrl.addDiscipline(id, name, credits, type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeDiscipline() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the discipline id: ");
        int id = input.nextInt();
        try {
            this.ctrl.removeDiscipline(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateDiscipline() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the discipline id: ");
        int oldId = input.nextInt();
        System.out.print("new id: ");
        int id = input.nextInt();
        System.out.print("new name: ");
        String name = input.next();
        System.out.print("new credits: ");
        String credits = input.next();
        System.out.print("new type (MANDATORY, OPTIONAL, FACULTATIVE): ");
        String type = input.next();
        try {
            this.ctrl.updateDiscipline(oldId, id, name, credits, type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printActivities() {
        System.out.println("\ncurrent activities:");
        for (Activity a : this.ctrl.getActivityRepo().get())
            System.out.println(a);
    }

    private void addActivity() {
        Scanner input = new Scanner(System.in);
        System.out.print("id: ");
        int id = input.nextInt();
        this.printDisciplines();
        System.out.print("select the discipline id: ");
        int disciplineId = input.nextInt();
        System.out.print("type (LECTURE, SEMINAR, LABORATORY): ");
        String type = input.next();
        this.printTeachers();
        System.out.print("select the teacher id: ");
        int teacherId = input.nextInt();
        try {
            this.ctrl.addActivity(id, disciplineId, type, teacherId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeActivity() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the activity id: ");
        int id = input.nextInt();
        try {
            this.ctrl.removeActivity(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateActivity() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the activity id: ");
        int oldId = input.nextInt();
        System.out.print("new id: ");
        int id = input.nextInt();
        this.printDisciplines();
        System.out.print("select the new discipline id: ");
        int disciplineId = input.nextInt();
        System.out.print("new type (LECTURE, SEMINAR, LABORATORY): ");
        String type = input.next();
        this.printTeachers();
        System.out.print("select the new teacher id: ");
        int teacherId = input.nextInt();
        try {
            this.ctrl.updateActivity(oldId, id, disciplineId, type, teacherId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printRooms() {
        System.out.println("current rooms:");
        for (Room r : this.ctrl.getRoomRepo().get())
            System.out.println(r);
    }

    private void addRoom() {
        Scanner input = new Scanner(System.in);
        System.out.print("id: ");
        int id = input.nextInt();
        System.out.print("building: ");
        String building = input.next();
        System.out.print("activity types (LECTURE, SEMINAR, LABORATORY): ");
        input.nextLine();
        String types = input.nextLine();
        try {
            this.ctrl.addRoom(id, building, Arrays.asList(types.split(",")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeRoom() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the room id: ");
        int id = input.nextInt();
        try {
            this.ctrl.removeRoom(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateRoom() {
        Scanner input = new Scanner(System.in);
        System.out.println("select the room id: ");
        int oldId = input.nextInt();
        System.out.print("new id: ");
        int id = input.nextInt();
        System.out.print("new building: ");
        String building = input.next();
        System.out.print("new activity types (LECTURE, SEMINAR, LABORATORY): ");
        input.nextLine();
        String types = input.nextLine();
        try {
            this.ctrl.updateRoom(oldId, id, building, Arrays.asList(types.split(",")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printFormations() {
        System.out.println("current formations:");
        for (Formation f : this.ctrl.getFormationRepo().get())
            System.out.println(f);
    }

    private void addFormation() {
        Scanner input = new Scanner(System.in);
        System.out.print("id: ");
        int id = input.nextInt();
        System.out.print("specialisation [group [subgroup]]: ");
        input.nextLine();
        String formation = input.nextLine();
        try {
            this.ctrl.addFormation(id, formation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFormation() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the formation id: ");
        int id = input.nextInt();
        try {
            this.ctrl.removeFormation(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateFormation() {
        Scanner input = new Scanner(System.in);
        System.out.println("select the formation id: ");
        int oldId = input.nextInt();
        System.out.print("new id: ");
        int id = input.nextInt();
        System.out.print("new formation: ");
        input.nextLine();
        String formation = input.nextLine();
        try {
            this.ctrl.updateFormation(oldId, id, formation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAssociations() {
        System.out.println("current associations:");
        for (TimeTableEntry timeTableEntry : this.ctrl.getTimeTableEntries())
            System.out.println(timeTableEntry);
    }

    private void addAssociation() {
        Scanner input = new Scanner(System.in);
        this.printFormations();
        System.out.print("select the formation id: ");
        int formationId = input.nextInt();
        this.printActivities();
        System.out.print("select the activity id: ");
        int activityId = input.nextInt();
        this.printRooms();
        System.out.print("select the room id: ");
        int roomId = input.nextInt();
        System.out.print("day (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY): ");
        String day = input.next();
        System.out.print("start hour: ");
        int startHour = input.nextInt();
        System.out.print("finish hour: ");
        int finishHour = input.nextInt();
        try {
            this.ctrl.addTimeTableEntry(formationId, activityId, roomId, day, startHour, finishHour);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeAssociation() {
        Scanner input = new Scanner(System.in);
        System.out.print("select the formation id: ");
        int formationId = input.nextInt();
        System.out.print("select the activity id: ");
        int activityId = input.nextInt();
        System.out.print("select the room id: ");
        int roomId = input.nextInt();
        System.out.print("day (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY): ");
        String day = input.next();
        System.out.print("start hour: ");
        int startHour = input.nextInt();
        System.out.print("finish hour: ");
        int finishHour = input.nextInt();
        try {
            this.ctrl.removeTimeTableEntry(formationId, activityId, roomId, day, startHour, finishHour);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getTeachersByRank() {
        Scanner input = new Scanner(System.in);
        System.out.print("rank (LECTURER, PROFESSOR, ASSISTANT): ");
        String rank = input.next();
        try {
            this.ctrl.getTeachersByRank(rank).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getActivitiesByRoom() {
        Scanner input = new Scanner(System.in);
        this.printRooms();
        System.out.print("select the room id: ");
        int roomId = input.nextInt();
        for (Pair<Activity, TimeSlot> pair : this.ctrl.getActivitiesByRoom(roomId))
            System.out.println(pair.getKey() + " - " + pair.getValue());
    }

    private void getFormationTimetable() {
        Scanner input = new Scanner(System.in);
        this.printFormations();
        System.out.println("select the formation id: ");
        int formationId = input.nextInt();
        for (TimeTableEntry timeTableEntry : this.ctrl.getTimeTable(formationId))
            System.out.println(timeTableEntry.getActivity().getId() +
                               ", " +
                               timeTableEntry.getRoom().getId() +
                               ", " +
                               timeTableEntry.getTimeSlot());
    }

    void run() {
        int     cmd   = -1;
        Scanner input = new Scanner(System.in);
        while (cmd != 0) {
            printMenu();
            System.out.print("your command: ");
            cmd = input.nextInt();
            switch (cmd) {
                case 0:
                    break;
                case 1: {
                    this.printTeachers();
                    break;
                }
                case 2: {
                    this.addTeacher();
                    break;
                }
                case 3: {
                    this.removeTeacher();
                    break;
                }
                case 4: {
                    this.updateTeacher();
                    break;
                }
                case 5: {
                    this.printDisciplines();
                    break;
                }
                case 6: {
                    this.addDiscipline();
                    break;
                }
                case 7: {
                    this.removeDiscipline();
                    break;
                }
                case 8: {
                    this.updateDiscipline();
                    break;
                }
                case 9: {
                    this.printActivities();
                    break;
                }
                case 10: {
                    this.addActivity();
                    break;
                }
                case 11: {
                    this.removeActivity();
                    break;
                }
                case 12: {
                    this.updateActivity();
                    break;
                }
                case 13: {
                    this.printRooms();
                    break;
                }
                case 14: {
                    this.addRoom();
                    break;
                }
                case 15: {
                    this.removeRoom();
                    break;
                }
                case 16: {
                    this.updateRoom();
                    break;
                }
                case 17: {
                    this.printFormations();
                    break;
                }
                case 18: {
                    this.addFormation();
                    break;
                }
                case 19: {
                    this.removeFormation();
                    break;
                }
                case 20: {
                    this.updateFormation();
                    break;
                }
                case 21: {
                    this.printAssociations();
                    break;
                }
                case 22: {
                    this.addAssociation();
                    break;
                }
                case 23: {
                    this.removeAssociation();
                    break;
                }
                case 24: {
                    this.getTeachersByRank();
                    break;
                }
                case 25: {
                    this.getActivitiesByRoom();
                    break;
                }
                case 26: {
                    this.getFormationTimetable();
                    break;
                }
                default:
                    System.out.println("\ninvalid command\n");
            }
        }
    }
}
