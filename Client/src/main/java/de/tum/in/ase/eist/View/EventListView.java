package de.tum.in.ase.eist.View;

import de.tum.in.ase.eist.Controller.Controller;
import de.tum.in.ase.eist.Model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class EventListView extends Stage implements Observer {
    private static final int SCENE_WIDTH = 400;
    private static final int SCENE_HEIGHT = 400;

    private Controller controller;
    private final ObservableList<Event> events;
    private final ListView<Event> eventListView;

    private Button createButton;

    public EventListView(Controller controller, List<Event> events) {
        this.controller = controller;
        this.events = FXCollections.observableList(events);
        this.events.forEach(event -> event.addObserver(this));
        this.eventListView = new ListView<>(this.events);
        this.controller.setEventListView(this);
        generateUserInterface();
    }

    @Override
    public void update() {
        eventListView.refresh();
    }

    private void displayEvent(Event event) {
        this.controller.displayEvent(event);
    }

    public void saveEvent(Event event) {
        if (!this.events.contains(event)) {
            this.events.add(event);
            event.addObserver(this);
        }
    }

    private void generateUserInterface() {
        VBox vbox = new VBox();

        createButton = new Button("Add Event");
        createButton.setOnAction(event -> displayEvent(new Event()));

        Label bookListLabel = new Label("List of Events");
        eventListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        eventListView.setOnMouseClicked(event -> displayEvent(eventListView.getSelectionModel().getSelectedItem()));
        vbox.getChildren().addAll(bookListLabel, eventListView, createButton);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
        setTitle("Event List");
        eventListView.refresh();
    }
}