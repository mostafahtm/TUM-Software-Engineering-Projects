package de.tum.in.ase.eist;

public final class ClientMain {
    private ClientMain() {}

    public static void main(String[] args) {
        //This is a workaround for a known issue when starting JavaFX applications
        ClientApplication.startApp(args);
    }
}
