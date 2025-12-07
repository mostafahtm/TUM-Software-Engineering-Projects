package de.tum.in.ase.eist.View;

import de.tum.in.ase.eist.Controller.Controller;
import de.tum.in.ase.eist.Model.Employee;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeCreationView extends Stage implements Observer {
    private static final int PADDING = 10;
    private static final int SCENE_HEIGHT = 300;
    private static final int SCENE_WIDTH = 300;

    private static final int GRID_VGAP = 8;
    private static final int GRID_HGAP = 10;

    private Button saveButton;

    private Employee employee;
    private Controller controller;
    private TextField employeeNameTextField;

    //TODO L03P01 MVC 3.1: Implement constructor of EmployeeCreationView and add observer to employee.
    public EmployeeCreationView(Controller controller, Employee employee) {
        this.employeeNameTextField = new TextField();

        generateUserInterface();
    }

    //TODO L03P01 MVC 3.2: implement update() which is triggered when a change in the employee model is detected.
    @Override
    public void update() {
    }

    // TODO L03P01 MVC 3.3: Implement save() which is triggered when clicking the 'Save Changes' button.
    private void save() {
    }

    private void generateUserInterface() {
        VBox vbox = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PADDING));
        grid.setVgap(GRID_VGAP);
        grid.setHgap(GRID_HGAP);

        Label employeeLabel = new Label("Employee: ");
        GridPane.setConstraints(employeeLabel, 0, 0);
        GridPane.setConstraints(employeeNameTextField, 1, 0);

        saveButton = new Button("Save Changes");
        GridPane.setConstraints(saveButton, 0, 2);
        saveButton.setOnAction(employee -> save());


        grid.getChildren().addAll(employeeLabel, saveButton, employeeNameTextField);
        vbox.getChildren().add(grid);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
    }
}