package ui;

import domain.*;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.Controller;
import service.TimeTableEntry;

public class GUIController {
    private final ObservableList<Teacher>        teachers    = FXCollections.observableArrayList();
    private final ObservableList<Discipline>     disciplines = FXCollections.observableArrayList();
    private final ObservableList<Activity>       activities  = FXCollections.observableArrayList();
    private final ObservableList<TimeTableEntry> timeTables  = FXCollections.observableArrayList();
    private final ObservableList<Formation>      formations  = FXCollections.observableArrayList();
    private final ObservableList<Room>           rooms       = FXCollections.observableArrayList();
    private       Controller                     ctrl;
    @FXML private Label                          errorLabel;

    @FXML private Spinner<Integer>       teacherIdSpinner;
    @FXML private TextField              teacherNameField;
    @FXML private TextField              teacherEmailField;
    @FXML private ComboBox<Teacher.Rank> teacherRankComboBox;
    @FXML private TextField              teacherCnpField;
    @FXML private ListView<Teacher>      teacherListView;
    @FXML private Button                 removeTeacherButton;
    @FXML private Button                 updateTeacherButton;

    @FXML private Spinner<Integer>          disciplineIdSpinner;
    @FXML private TextField                 disciplineNameField;
    @FXML private ComboBox<Discipline.Type> disciplineTypeComboBox;
    @FXML private TextField                 disciplineCreditsField;
    @FXML private ListView<Discipline>      disciplineListView;
    @FXML private Button                    removeDisciplineButton;
    @FXML private Button                    updateDisciplineButton;

    @FXML private Spinner<Integer>        activityIdSpinner;
    @FXML private ComboBox<Teacher>       activityTeacherComboBox;
    @FXML private ComboBox<Activity.Type> activityTypeComboBox;
    @FXML private ComboBox<Discipline>    activityDisciplineComboBox;
    @FXML private ListView<Activity>      activityListView;
    @FXML private Button                  removeActivityButton;
    @FXML private Button                  updateActivityButton;

    @FXML private ComboBox<Formation>                        formationComboBox;
    @FXML private ComboBox<Activity>                         formationActivityComboBox;
    @FXML private ComboBox<Room>                             roomComboBox;
    @FXML private ComboBox<TimeSlot.Day>                     dayComboBox;
    @FXML private Spinner<Integer>                           startHourSpinner;
    @FXML private Spinner<Integer>                           finishHourSpinner;
    @FXML private TableView<TimeTableEntry>                  timeTableTable;
    @FXML private TableColumn<TimeTableEntry, TimeSlot.Day>  dayColumn;
    @FXML private TableColumn<TimeTableEntry, String>        hoursColumn;
    @FXML private TableColumn<TimeTableEntry, Number>        roomColumn;
    @FXML private TableColumn<TimeTableEntry, Activity.Type> typeColumn;
    @FXML private TableColumn<TimeTableEntry, String>        disciplineColumn;
    @FXML private TableColumn<TimeTableEntry, String>        teacherColumn;
    @FXML private Button                                     removeTimeTableEntryButton;

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
        this.teachers.setAll(this.ctrl.getTeacherRepo().get());
        this.disciplines.setAll(this.ctrl.getDisciplineRepo().get());
        this.activities.setAll(this.ctrl.getActivityRepo().get());
        this.formations.setAll(this.ctrl.getFormationRepo().get());
        this.rooms.setAll(this.ctrl.getRoomRepo().get());
    }

    private void initializeTeachersTab() {
        this.teacherIdSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99));
        this.teacherRankComboBox.setItems(FXCollections.observableArrayList(Teacher.Rank.values()));
        this.teacherCnpField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,13}")) {
                teacherCnpField.setText(oldValue);
            }
        });

        this.teacherListView.setItems(this.teachers);
        this.teacherListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTeacher, newTeacher) -> {
            this.errorLabel.setText("");
            if (newTeacher != null) {
                this.removeTeacherButton.setDisable(false);
                this.updateTeacherButton.setDisable(false);
                this.teacherIdSpinner.getValueFactory().setValue(newTeacher.getId());
                this.teacherNameField.setText(newTeacher.getName());
                this.teacherEmailField.setText(newTeacher.getEmail());
                this.teacherRankComboBox.setValue(newTeacher.getRank());
                this.teacherCnpField.setText(String.valueOf(newTeacher.getCnp()));
            } else {
                this.removeTeacherButton.setDisable(true);
                this.updateTeacherButton.setDisable(true);
            }
        });
    }

    private void initializeDisciplinesTab() {
        this.disciplineIdSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99));
        this.disciplineTypeComboBox.setItems(FXCollections.observableArrayList(Discipline.Type.values()));
        this.disciplineCreditsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}"))
                teacherCnpField.setText(oldValue);
        });

        this.disciplineListView.setItems(this.disciplines);
        this.disciplineListView.getSelectionModel()
                               .selectedItemProperty()
                               .addListener((obs, oldDiscipline, newDiscipline) -> {
                                   this.errorLabel.setText("");
                                   if (newDiscipline != null) {
                                       this.removeDisciplineButton.setDisable(false);
                                       this.updateDisciplineButton.setDisable(false);
                                       this.disciplineIdSpinner.getValueFactory().setValue(newDiscipline.getId());
                                       this.disciplineNameField.setText(newDiscipline.getName());
                                       this.disciplineTypeComboBox.setValue(newDiscipline.getType());
                                       this.disciplineCreditsField.setText(String.valueOf(newDiscipline.getCredits()));
                                   } else {
                                       this.removeDisciplineButton.setDisable(true);
                                       this.updateDisciplineButton.setDisable(true);
                                   }
                               });
    }

    private void initializeActivitiesTab() {
        this.activityIdSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99));
        this.activityTeacherComboBox.setItems(this.teachers);
        this.activityTypeComboBox.setItems(FXCollections.observableArrayList(Activity.Type.values()));
        this.activityDisciplineComboBox.setItems(this.disciplines);

        this.activityListView.setItems(this.activities);
        this.activityListView.getSelectionModel()
                             .selectedItemProperty()
                             .addListener((obs, oldActivity, newActivity) -> {
                                 this.errorLabel.setText("");
                                 if (newActivity != null) {
                                     this.removeActivityButton.setDisable(false);
                                     this.updateActivityButton.setDisable(false);
                                     this.activityIdSpinner.getValueFactory().setValue(newActivity.getId());
                                     try {
                                         this.activityDisciplineComboBox.setValue(this.ctrl.getDisciplineRepo()
                                                                                           .get(newActivity.getDiscipline()));
                                     } catch (Exception e) {
                                         this.errorLabel.setText(e.getMessage());
                                     }
                                     this.activityTypeComboBox.setValue(newActivity.getType());
                                     try {
                                         this.activityTeacherComboBox.setValue(this.ctrl.getTeacherRepo()
                                                                                        .get(newActivity.getTeacher()));
                                     } catch (Exception e) {
                                         this.errorLabel.setText(e.getMessage());
                                     }
                                 } else {
                                     this.removeActivityButton.setDisable(true);
                                     this.updateActivityButton.setDisable(true);
                                 }
                             });
    }

    private void initializeTimeTableTab() {
        this.formationComboBox.setItems(formations);
        this.formationActivityComboBox.setItems(this.activities);
        this.roomComboBox.setItems(rooms);
        this.dayComboBox.setItems(FXCollections.observableArrayList(TimeSlot.Day.values()));
        this.startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        this.finishHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        this.timeTableTable.setItems(this.timeTables);

        this.startHourSpinner.valueProperty()
                             .addListener((obs, oldValue, newValue) -> ((SpinnerValueFactory.IntegerSpinnerValueFactory) this.finishHourSpinner
                                     .getValueFactory()).setMin(newValue));

        this.formationComboBox.getSelectionModel()
                              .selectedItemProperty()
                              .addListener((obs, oldFormation, newFormation) -> this.timeTables.setAll(this.ctrl.getTimeTable(
                                      newFormation.getId())));

        this.timeTableTable.getSelectionModel().selectedItemProperty().addListener((obs, oldTTEntry, newTTEntry) -> {
            this.errorLabel.setText("");
            this.removeTimeTableEntryButton.setDisable(newTTEntry == null);
        });

        this.dayColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.193));
        this.hoursColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.15));
        this.roomColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.1));
        this.typeColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.15));
        this.disciplineColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.2));
        this.teacherColumn.prefWidthProperty().bind(this.timeTableTable.widthProperty().multiply(0.2));

        this.dayColumn.setCellValueFactory(ttEntry -> new ReadOnlyObjectWrapper<>(ttEntry.getValue()
                                                                                         .getTimeSlot()
                                                                                         .getDay()));
        this.hoursColumn.setCellValueFactory(ttEntryData -> {
            var start  = String.valueOf(ttEntryData.getValue().getTimeSlot().getStartHour());
            var finish = String.valueOf(ttEntryData.getValue().getTimeSlot().getFinishHour());
            return new ReadOnlyStringWrapper(start + " - " + finish);
        });
        this.roomColumn.setCellValueFactory(ttEntryData -> new ReadOnlyIntegerWrapper(ttEntryData.getValue()
                                                                                                 .getRoom()
                                                                                                 .getId()));
        this.typeColumn.setCellValueFactory(ttEntryData -> new ReadOnlyObjectWrapper<>(ttEntryData.getValue()
                                                                                                  .getActivity()
                                                                                                  .getType()));
        this.disciplineColumn.setCellValueFactory(ttEntryData -> {
            Discipline discipline = null;
            try {
                discipline = this.ctrl.getDisciplineRepo().get(ttEntryData.getValue().getActivity().getDiscipline());
            } catch (Exception e) {
                e.printStackTrace();
                this.errorLabel.setText(e.getMessage());
            }
            return new ReadOnlyStringWrapper((discipline != null) ? discipline.getName() : "NULL");
        });
        this.teacherColumn.setCellValueFactory(ttEntryData -> {
            Teacher teacher = null;
            try {
                teacher = this.ctrl.getTeacherRepo().get(ttEntryData.getValue().getActivity().getTeacher());
            } catch (Exception e) {
                e.printStackTrace();
                this.errorLabel.setText(e.getMessage());
            }
            return new ReadOnlyStringWrapper((teacher != null) ? teacher.getName() : "NULL");
        });
    }

    @FXML
    public void initialize() {
        this.initializeTeachersTab();
        this.initializeDisciplinesTab();
        this.initializeActivitiesTab();
        this.initializeTimeTableTab();
    }

    @FXML
    public void addTeacherButtonPressed() {
        var id    = this.teacherIdSpinner.getValue();
        var name  = this.teacherNameField.getText().strip();
        var email = this.teacherEmailField.getText().strip();
        var rank  = this.teacherRankComboBox.getValue().name();
        var cnp   = this.teacherCnpField.getText().strip();
        try {
            this.ctrl.addTeacher(id, name, email, rank, cnp);
            this.teachers.add(this.ctrl.getTeacherRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void removeTeacherButtonPressed() {
        Teacher teacher = this.teacherListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.removeTeacher(teacher.getId());
            this.teacherListView.getSelectionModel().clearSelection();
            this.teachers.remove(teacher);
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void updateTeacherButtonPressed() {
        var id      = this.teacherIdSpinner.getValue();
        var name    = this.teacherNameField.getText().strip();
        var email   = this.teacherEmailField.getText().strip();
        var rank    = this.teacherRankComboBox.getValue().name();
        var cnp     = this.teacherCnpField.getText().strip();
        var teacher = this.teacherListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.updateTeacher(teacher.getId(), id, name, email, rank, cnp);
            this.teachers.set(this.teachers.indexOf(teacher), this.ctrl.getTeacherRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void addDisciplineButtonPressed() {
        var id      = this.disciplineIdSpinner.getValue();
        var name    = this.disciplineNameField.getText().strip();
        var type    = this.disciplineTypeComboBox.getValue().name();
        var credits = this.disciplineCreditsField.getText().strip();
        try {
            this.ctrl.addDiscipline(id, name, credits, type);
            this.disciplines.add(this.ctrl.getDisciplineRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void removeDisciplineButtonPressed() {
        Discipline discipline = this.disciplineListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.removeDiscipline(discipline.getId());
            this.disciplineListView.getSelectionModel().clearSelection();
            this.disciplines.remove(discipline);
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void updateDisciplineButtonPressed() {
        var id         = this.disciplineIdSpinner.getValue();
        var name       = this.disciplineNameField.getText().strip();
        var type       = this.disciplineTypeComboBox.getValue().name();
        var credits    = this.disciplineCreditsField.getText().strip();
        var discipline = this.disciplineListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.updateDiscipline(discipline.getId(), id, name, credits, type);
            this.disciplines.set(this.disciplines.indexOf(discipline), this.ctrl.getDisciplineRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void addActivityButtonPressed() {
        var id           = this.activityIdSpinner.getValue();
        var disciplineId = this.activityDisciplineComboBox.getValue().getId();
        var type         = this.activityTypeComboBox.getValue().name();
        var teacherId    = this.activityTeacherComboBox.getValue().getId();
        try {
            this.ctrl.addActivity(id, disciplineId, type, teacherId);
            this.activities.add(this.ctrl.getActivityRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void removeActivityButtonPressed() {
        Activity activity = this.activityListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.removeActivity(activity.getId());
            this.activityListView.getSelectionModel().clearSelection();
            this.activities.remove(activity);
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void updateActivityButtonPressed() {
        var id           = this.activityIdSpinner.getValue();
        var disciplineId = this.activityDisciplineComboBox.getValue().getId();
        var type         = this.activityTypeComboBox.getValue().name();
        var teacherId    = this.activityTeacherComboBox.getValue().getId();
        var activity     = this.activityListView.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.updateActivity(activity.getId(), id, disciplineId, type, teacherId);
            this.activities.set(this.activities.indexOf(activity), this.ctrl.getActivityRepo().get(id));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void addTimeTableEntryButtonPressed() {
        var formationId = this.formationComboBox.getValue().getId();
        var activityId  = this.formationActivityComboBox.getValue().getId();
        var roomId      = this.roomComboBox.getValue().getId();
        var day         = this.dayComboBox.getValue().name();
        var startHour   = this.startHourSpinner.getValue();
        var finishHour  = this.finishHourSpinner.getValue();
        try {
            this.ctrl.addTimeTableEntry(formationId, activityId, roomId, day, startHour, finishHour);
            this.timeTables.setAll(this.ctrl.getTimeTable(formationId));
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void removeTimeTableEntryButtonPressed() {
        TimeTableEntry ttEntry = this.timeTableTable.getSelectionModel().getSelectedItem();
        try {
            this.ctrl.removeTimeTableEntry(ttEntry.getFormation().getId(),
                                           ttEntry.getActivity().getId(),
                                           ttEntry.getRoom().getId(),
                                           ttEntry.getTimeSlot().getDay().name(),
                                           ttEntry.getTimeSlot().getStartHour(),
                                           ttEntry.getTimeSlot().getFinishHour()
            );
            this.timeTableTable.getSelectionModel().clearSelection();
            this.timeTables.remove(ttEntry);
            this.errorLabel.setText("");
        } catch (Exception e) {
            this.errorLabel.setText(e.getMessage());
        }
    }
}
