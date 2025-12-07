package de.tum.in.ase.eist;

import java.util.ArrayList;

public class User {
    private ArrayList<User> followers = new ArrayList<>();
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFollower(User user){
        followers.add(user);
    }

    public void removeFollower(User user){
        followers.remove(user);
    }
}
