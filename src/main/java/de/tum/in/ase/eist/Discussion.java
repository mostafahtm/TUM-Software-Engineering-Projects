package de.tum.in.ase.eist;

import java.util.Map;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Discussion {
    private String topic;
    private List<Map.Entry<String, String>> comments;

    public Discussion(String topic) {
        this.topic = topic;
        this.comments = new ArrayList<>();
    }
    
    public String getTopic() {
        return this.topic;
    }

    public List<Map.Entry<String, String>> getComments() {
        return comments;
    }

    public void addComment(String username, String comment) {
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(username, comment);
        comments.add(entry);
    }

    public List<String> getCommentsByUser(String username) {
        List<String> commentsByUser = new ArrayList<String>();
        for (var comment : comments) {
            if (comment.getKey().equals(username)) {
                commentsByUser.add(comment.getValue());
            }
        }
        return commentsByUser;
    }

    public void deleteCommentsByUser(String username) {
        comments.removeIf(comment -> comment.getKey().equals(username));
    }
}
