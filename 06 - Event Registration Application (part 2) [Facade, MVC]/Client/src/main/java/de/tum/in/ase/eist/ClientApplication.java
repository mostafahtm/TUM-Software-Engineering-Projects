package de.tum.in.ase.eist;

import de.tum.in.ase.eist.Controller.Controller;
import de.tum.in.ase.eist.Model.Event;
import de.tum.in.ase.eist.View.EventListView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public final class ClientApplication extends javafx.application.Application {

    public static void startApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<Event> events = new ArrayList<>();
        Controller controller = new Controller();
        EventListView eventListView = new EventListView(controller, events);
        eventListView.show();
    }
}
