package de.tum.in.ase.eist.eventmicroservice;

enum Role {
    SENIOR,
    MANAGER,
    JUNIOR,
    INTERN
}

public class Employee {
    private String name;
    private int id;
    private String token;
    private Role role;
    private Event eventPreferences;
    private boolean isRegistered = false;

    public Employee() {

    }

    public Employee(String name, int id, String token, Role role, Event eventPreferences) {
        this.name = name;
        this.id = id;
        this.token = token;
        this.role = role;
        this.eventPreferences = eventPreferences;
    }

    public Employee(String name, int id, Role role) {
        this.name = name;
        this.id = id;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Event getEventPreferences() {
        return eventPreferences;
    }

    public void setEventPreferences(Event eventPreferences) {
        this.eventPreferences = eventPreferences;
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean registered) {
        this.isRegistered = registered;
    }
}
