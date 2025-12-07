package de.tum.in.ase.eist.Controller;

import de.tum.in.ase.eist.Model.Employee;
import de.tum.in.ase.eist.Model.Event;
import de.tum.in.ase.eist.View.EmployeeCreationView;
import de.tum.in.ase.eist.View.EventListView;
import de.tum.in.ase.eist.View.EventRegistrationView;

public class Controller {
    private EventListView eventListView;
    private EventRegistrationView eventRegistrationView;
    private EmployeeCreationView employeeCreationView;

    public void displayEvent(Event event) {
        this.eventRegistrationView = new EventRegistrationView(this, event);
        this.eventRegistrationView.show();
    }

    public void setEventListView(EventListView eventListView) {
        this.eventListView = eventListView;
    }

    public void setEventRegistrationView(EventRegistrationView eventRegistrationView) {
        this.eventRegistrationView = eventRegistrationView;
    }

    public void saveEvent(Event event) {
        this.eventListView.saveEvent(event);
        event.notifyObservers();
    }


    //TODO L03P01 MVC 2.1: Implement addNewEmployee(...).
    public void addNewEmployee(Employee employee) {
    }

    public void displayEmployee(Employee employee) {
        this.employeeCreationView = new EmployeeCreationView(this, employee);
        this.employeeCreationView.show();
    }
}
