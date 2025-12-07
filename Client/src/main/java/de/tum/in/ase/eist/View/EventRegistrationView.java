package de.tum.in.ase.eist.View;

import de.tum.in.ase.eist.Controller.Controller;
import de.tum.in.ase.eist.Model.Employee;
import de.tum.in.ase.eist.Model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EventRegistrationView extends Stage implements Observer {
    private static final int PADDING = 10;
    private static final int SCENE_HEIGHT = 300;
    private static final int SCENE_WIDTH = 350;
    private static final int GRID_VGAP = 8;
    private static final int GRID_HGAP = 10;
    private Button saveButton;
    private Button addEmployeeButton;
    private final Event event;
    private Controller controller;
    private final TextField eventDescriptionTextField;
    private ListView<Employee> employeeListView;
    private ObservableList<Employee> employees;

    public EventRegistrationView(Controller controller, Event event) {
        this.event = event;
        this.controller = controller;
        this.eventDescriptionTextField = new TextField(event.getDescription());
        this.employees = FXCollections.observableList(this.event.getEmployees());
        this.employeeListView = new ListView<>(this.employees);
        this.controller.setEventRegistrationView(this);
        generateUserInterface();
    }

    private void save() {
        this.event.setDescription(eventDescriptionTextField.getText());
        this.controller.saveEvent(event);
    }

    @Override
    public void update() {
        this.eventDescriptionTextField.setText(event.getDescription());
        this.setTitle(event.getDescription());
        employeeListView.refresh();
    }

    public void addNewEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.addObserver(this);
        }
    }

    private void displayEmployee(Employee employee) {
        this.controller.displayEmployee(employee);
    }

    private void generateUserInterface() {
        VBox vbox = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PADDING));
        grid.setVgap(GRID_VGAP);
        grid.setHgap(GRID_HGAP);

        Label eventLabel = new Label("Event: ");
        GridPane.setConstraints(eventLabel, 0, 0);
        GridPane.setConstraints(eventDescriptionTextField, 1, 0);

        saveButton = new Button("Save Changes");
        GridPane.setConstraints(saveButton, 0, 2);
        saveButton.setOnAction(event -> save());

        addEmployeeButton = new Button("Add Employee");
        addEmployeeButton.setOnAction(employee -> displayEmployee(new Employee()));

        employeeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employeeListView.setOnMouseClicked(employee -> displayEmployee(employeeListView.getSelectionModel()
                .getSelectedItem()));


        GridPane.setConstraints(addEmployeeButton, 1, 3);
        GridPane.setConstraints(employeeListView, 0, 3);

        grid.getChildren().addAll(eventLabel, eventDescriptionTextField, employeeListView, saveButton, addEmployeeButton);
        vbox.getChildren().add(grid);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
        if (this.event.getDescription().isEmpty()) {
            setTitle("Add new Event");
        } else {
            setTitle(this.event.getDescription());
        }
        employeeListView.refresh();
    }
}