package de.tum.in.ase.eist;

import java.util.Map;
import java.util.Random;
import java.lang.Math;

public class AuthenticationService {
    private Map<String, Long> userTokens;
    private Random random = new Random();

    private UserService userService;

    public AuthenticationService(Map<String, Long> userTokens) {
        this.userTokens = userTokens;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Long authenticateUser(String username, String password) {
        // TODO Part 2: Implement Authentication Service
        throw new UnsupportedOperationException("Not Implemented");
    }

    public boolean isAuthenticated(String username, Long token) {
        return userTokens.get(username) == token;
    }

    public void deauthenticateUser(String username) {
        userTokens.remove(username);
    }
}
