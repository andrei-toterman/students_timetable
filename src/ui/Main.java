package ui;

import domain.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.Controller;

import java.io.FileInputStream;
import java.util.Properties;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("settings.properties"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        Repository<Teacher>    teacherRepository;
        Repository<Discipline> disciplineRepository;
        Repository<Activity>   activityRepository;
        Repository<Room>       roomRepository;
        Repository<Formation>  formationRepository;
        Controller             ctrl;

        if (properties.getProperty("Repository").contains("Binary")) {
            if (properties.getProperty("Repository").contains("DB")) {
                teacherRepository    = new TeacherDBRepository(Teacher.class, properties.getProperty("TeachersDB"));
                disciplineRepository = new DisciplineDBRepository(Discipline.class,
                                                                  properties.getProperty("DisciplinesDB")
                );
            } else {
                teacherRepository    = new BinaryRepository<>(Teacher.class, properties.getProperty("TeachersBinary"));
                disciplineRepository = new BinaryRepository<>(Discipline.class,
                                                              properties.getProperty("DisciplinesBinary")
                );
            }
            activityRepository  = new BinaryRepository<>(Activity.class, properties.getProperty("ActivitiesBinary"));
            roomRepository      = new BinaryRepository<>(Room.class, properties.getProperty("RoomsBinary"));
            formationRepository = new BinaryRepository<>(Formation.class, properties.getProperty("FormationsBinary"));
            ctrl                = new Controller(teacherRepository,
                                                 disciplineRepository,
                                                 activityRepository,
                                                 roomRepository,
                                                 formationRepository,
                                                 Controller.Type.BINARY,
                                                 properties.getProperty("TimeTablesBinary")
            );
        } else if (properties.getProperty("Repository").contains("Text")) {
            if (properties.getProperty("Repository").contains("DB")) {
                teacherRepository    = new TeacherDBRepository(Teacher.class, properties.getProperty("TeachersDB"));
                disciplineRepository = new DisciplineDBRepository(Discipline.class,
                                                                  properties.getProperty("DisciplinesDB")
                );
            } else {
                teacherRepository    = new FileRepository<>(Teacher.class, properties.getProperty("TeachersText"));
                disciplineRepository = new FileRepository<>(Discipline.class,
                                                            properties.getProperty("DisciplinesText")
                );
            }
            activityRepository  = new FileRepository<>(Activity.class, properties.getProperty("ActivitiesText"));
            roomRepository      = new FileRepository<>(Room.class, properties.getProperty("RoomsText"));
            formationRepository = new FileRepository<>(Formation.class, properties.getProperty("FormationsText"));
            ctrl                = new Controller(teacherRepository,
                                                 disciplineRepository,
                                                 activityRepository,
                                                 roomRepository,
                                                 formationRepository,
                                                 Controller.Type.TEXT,
                                                 properties.getProperty("TimeTablesText")
            );
        } else {
            teacherRepository    = new Repository<>(Teacher.class);
            disciplineRepository = new Repository<>(Discipline.class);
            activityRepository   = new Repository<>(Activity.class);
            roomRepository       = new Repository<>(Room.class);
            formationRepository  = new Repository<>(Formation.class);
            ctrl                 = new Controller(teacherRepository,
                                                  disciplineRepository,
                                                  activityRepository,
                                                  roomRepository,
                                                  formationRepository,
                                                  Controller.Type.NONE,
                                                  ""
            );
        }

//        Console console = new Console(ctrl);
//        console.run();

        FXMLLoader    loader        = new FXMLLoader(getClass().getResource("GUI.fxml"));
        Parent        root          = loader.load();
        loader.<GUIController>getController().setCtrl(ctrl);

        stage.setTitle("Time table");
        stage.setScene(new Scene(root, 1100, 600));
        stage.show();
    }
}
