package de.tum.in.ase.eist;

import java.util.List;
import java.util.Map;

public final class Main {
    private static TummitFacade facade = new TummitFacade();
    public static void main(String[] args) {
        String username1 = "Mustermann";
        String password1 = "pa$$word";
        
        String username2 = "Musterfrau";
        String password2 = "pa$$word!";

        System.out.println("Registering users...");
        facade.registerUser(username1, password1);
        facade.registerUser(username2, password2);

        System.out.println("Authenticating users...");
        Long token1 = facade.authenticateUser(username1, password1);
        Long token2 = facade.authenticateUser(username2, password2);

        System.out.println("Creating a new discussion...\n");
        String topic = "EIST-L03PB01-Support";
        facade.createDiscussion(username1, token1, topic);
        facade.addComment(username1, token1, topic, "What is Strangler Pattern?");
        facade.addComment(username2, token2, topic, "Anyone?");
        facade.addComment(username2, token2, topic, "Nvm, I just figured it out.");
        printDiscussion(topic);
        System.out.println("\nFiltering comments by user 2\n");
        printCommentsByUser(topic, username2);
        System.out.println("\nDeleting user 2...");
        facade.deleteUser(username2, token2);
        printDiscussion(topic);

    }

    public static void printDiscussion(String topic) {
        System.out.println("#####   " + topic + "   #####");
        System.out.println("-------------------------------------------------------------------");
        List<Map.Entry<String, String>> comments = facade.getComments(topic);
        for (Map.Entry<String, String> comment : comments) {
            System.out.println(comment.getKey() + " wrote: " + comment.getValue());
        }
        System.out.println("-------------------------------------------------------------------");
    }

    public static void printCommentsByUser(String topic, String username) {
        System.out.println("#####   Comments of " + username + " on " + topic + "   #####");
        System.out.println("-------------------------------------------------------------------");
        List<String> comments = facade.getCommentsByUser(topic, username);
        for (String comment : comments) {
            System.out.println(" - " + comment);
        }
        System.out.println("-------------------------------------------------------------------");
    }
}
