package de.tum.in.ase.eist;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TummitFacade {
    private List<User> users;
    private List<Discussion> discussions;
    private Map<String, Long> userTokens;

    private final Tummit monolith;
    
    public final UserService userService;
    public final AuthenticationService authenticationService;
    public final DiscussionService discussionService;

    public TummitFacade() {
        this.users = new ArrayList<>();
        this.discussions = new ArrayList<>();
        this.userTokens = new HashMap();
        this.monolith = new Tummit(users, discussions, userTokens);
        this.userService = new UserService(users);
        this.authenticationService = new AuthenticationService(userTokens);
        this.discussionService = new DiscussionService(discussions);
        userService.setAuthenticationService(authenticationService);
        userService.setDiscussionService(discussionService);
        authenticationService.setUserService(userService);
        discussionService.setAuthenticationService(authenticationService);
    }

    public void registerUser(String username, String password) {
        try {
            userService.registerUser(username, password);
        } catch (UnsupportedOperationException e) {
            monolith.registerUser(username, password);
        }
    }

    public void deleteUser(String username, Long token) {
        try {
            userService.deleteUser(username, token);
        } catch (UnsupportedOperationException e) {
            monolith.deleteUser(username, token);
        }
    }

    public Long authenticateUser(String username, String password) {
        try {
            return authenticationService.authenticateUser(username, password);
        } catch (UnsupportedOperationException e) {
            return monolith.authenticateUser(username, password);
        }
    }

    public void createDiscussion(String username, Long token, String topic) {
        try {
            discussionService.createDiscussion(username, token, topic);
        } catch (UnsupportedOperationException e) {
            monolith.createDiscussion(username, token, topic);
        }
    }

    public void addComment(String username, Long token, String topic, String comment) {
        try {
            discussionService.addComment(username, token, topic, comment);
        } catch (UnsupportedOperationException e) {
            monolith.addComment(username, token, topic, comment);
        }
    }

    public List<Map.Entry<String, String>> getComments(String topic) {
        try {
            return discussionService.getComments(topic);
        } catch (UnsupportedOperationException e) {
            return monolith.getComments(topic);
        }
    }

    public List<String> getCommentsByUser(String topic, String username) {
        try {
            return discussionService.getCommentsByUser(topic, username);
        } catch (UnsupportedOperationException e) {
            return monolith.getCommentsByUser(topic, username);
        }
    }
}
