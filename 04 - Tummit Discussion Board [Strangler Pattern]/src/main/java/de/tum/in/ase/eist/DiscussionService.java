package de.tum.in.ase.eist;

import java.util.List;
import java.util.Map;

public class DiscussionService {
    private List<Discussion> discussions;

    private AuthenticationService authenticationService;

    public DiscussionService(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void createDiscussion(String username, Long token, String topic) {
        // TODO Part 3: Implement Discussion Service
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void addComment(String username, Long token, String topic, String comment) {
        // TODO Part 3: Implement Discussion Service
        throw new UnsupportedOperationException("Not Implemented");
    }

    public List<Map.Entry<String, String>> getComments(String topic) {
        // TODO Part 3: Implement Discussion Service
        throw new UnsupportedOperationException("Not Implemented");
    }

    public List<String> getCommentsByUser(String topic, String username) {
        // TODO Part 3: Implement Discussion Service
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void deleteUserComments(String username) {
        for (Discussion discussion : discussions) {
            discussion.deleteCommentsByUser(username);
        }
    }
}
