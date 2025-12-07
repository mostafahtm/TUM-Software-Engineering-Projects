package de.tum.in.ase.eist.Model;

public class Employee extends Observable {
    private String name;
    private int id;
    private String token;
    private Role role;
    private Event eventPreference;
    private boolean isRegistered = false;

    public Employee() {
        name = "";
    }

    //TODO L03P01 MVC 1.1: Implement the Constructor and add getter and setter for all attributes.
    public Employee(String name, int id, String token, Role role, Event eventPreference) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}