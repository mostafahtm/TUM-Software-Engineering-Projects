package de.tum.in.ase.eist.Model;

import java.util.ArrayList;
import java.util.List;

enum DietaryPreference {
    VEGAN, VEGETARIAN, OMNIVORE
}

public class Event extends Observable {
    private int numberOfParticipants;
    private DietaryPreference dietaryPreference;
    private String description;
    private List<Employee> employees = new ArrayList<>();

    public Event() {
        numberOfParticipants = 1;
        dietaryPreference = DietaryPreference.OMNIVORE;
        description = "";
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int participant) {
        this.numberOfParticipants = participant;
    }

    public DietaryPreference getDietaryPreference() {
        return dietaryPreference;
    }

    public void setDietaryPreference(DietaryPreference dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }
}