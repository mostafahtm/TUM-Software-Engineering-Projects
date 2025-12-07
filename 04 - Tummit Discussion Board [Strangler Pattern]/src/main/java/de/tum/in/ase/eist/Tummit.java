package de.tum.in.ase.eist;

import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.util.Random;

public class Tummit {
    private List<User> users;
    private List<Discussion> discussions;
    private Map<String, Long> userTokens;
    private Random random = new Random();

    public Tummit(List<User> users, List<Discussion> discussions, Map<String, Long> userTokens) {
        this.users = users;
        this.discussions = discussions;
        this.userTokens = userTokens;
    }

    public void registerUser(String username, String password) {
        if (!users.stream().filter(user -> user.getUsername().equals(username)).findFirst().isEmpty()) {
            return;
        }
        User user = new User(username, password);
        users.add(user);
    }

    public void deleteUser(String username, Long token) {
        if (userTokens.get(username) != token) {
            return;
        }
        for (Discussion discussion : discussions) {
            discussion.deleteCommentsByUser(username);
        }
        users.removeIf(user -> user.getUsername().equals(username));
        userTokens.remove(username);
    }

    public Long authenticateUser(String username, String password) {
        var optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (optionalUser.isEmpty()) {
            return -1L;
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            return -1L;
        }
        Long token = Math.abs(this.random.nextLong());
        userTokens.put(username, token);
        return token;
    }

    public void createDiscussion(String username, Long token, String topic) {
        if (userTokens.get(username) != token) {
            return;
        }
        Discussion discussion = new Discussion(topic);
        discussions.add(discussion);
    }

    public void addComment(String username, Long token, String topic, String comment) {
        if (userTokens.get(username) != token) {
            return;
        }
        var optionalDiscussion = discussions.stream().filter(discussion -> discussion.getTopic().equals(topic)).findFirst();
        if (optionalDiscussion.isEmpty()) {
            return;
        }
        Discussion discussion = optionalDiscussion.get();
        discussion.addComment(username, comment);
    }

    public List<Map.Entry<String, String>> getComments(String topic) {
        var optionalDiscussion = discussions.stream().filter(discussion -> discussion.getTopic().equals(topic)).findFirst();
        if (optionalDiscussion.isEmpty()) {
            return null;
        }
        Discussion discussion = optionalDiscussion.get();
        return discussion.getComments();
    }

    public List<String> getCommentsByUser(String topic, String username) {
        var optionalDiscussion = discussions.stream().filter(discussion -> discussion.getTopic().equals(topic)).findFirst();
        if (optionalDiscussion.isEmpty()) {
            return null;
        }
        Discussion discussion = optionalDiscussion.get();
        return discussion.getCommentsByUser(username);
    }
}
