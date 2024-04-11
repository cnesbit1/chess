package ui;
import model.AuthData;
import webSocketMessages.severMessages.ServerMessage;

import java.lang.Exception;
public class SignedOutUI extends AbstractREPL {


    private enum OptionsUI {
        REGISTER(1, "register"),
        LOGIN(2, "login");

        private final int number;
        private final String description;

        OptionsUI(int number, String description) {
            this.number = number;
            this.description = description;
        }
    }

    public SignedOutUI(SwitchUILoop programLoop, ServerFacade serverWrapper) {
        super(programLoop, serverWrapper);
    }

    @Override
    public void processInput(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                System.out.println("Input Username:");
                String username = scanner.nextLine();
                System.out.println("Input Password:");
                String password = scanner.nextLine();
                System.out.println("Input Email:");
                String email = scanner.nextLine();
                AuthData authData = serverWrapper.register(username, password, email);
                serverWrapper.conn.setAuthToken(authData.authToken());
                serverWrapper.webConn.setAuthToken(authData.authToken());
                System.out.println();
                System.out.println("Successful Registration!");
                programLoop.switchToSignedIn();
                this.changeUI();
            }
            else if (choice == 2) {
                System.out.println("Input Username:");
                String username = scanner.nextLine();
                System.out.println("Input Password:");
                String password = scanner.nextLine();
                AuthData authData = serverWrapper.login(username, password);
                serverWrapper.conn.setAuthToken(authData.authToken());
                serverWrapper.webConn.setAuthToken(authData.authToken());
                System.out.println();
                System.out.println("Successful Log in!");
                programLoop.switchToSignedIn();
                this.changeUI();
            }
            else { throw new Exception(); }
        }
        catch (Exception e) {
            System.out.println("That wasn't a valid choice, try again.");
        }
    }

    @Override
    public void printHelp() {
        System.out.println("You are currently signed out.");
        System.out.println("Here are your available options.");
        System.out.println("Login: sign into an existing account to play chess.");
        System.out.println("Register: create a new account to play chess.");
        System.out.println();
    }

    @Override
    public void showUIPrompt() {
        System.out.println("Give an input of 'quit' to terminate this program.");
        System.out.println("Give an input of 'help' for more possible instructions.");
        for (OptionsUI option : OptionsUI.values()) {
            System.out.println("Enter " + option.number + " to " + option.description + ".");
        }
    }
    @Override
    public void notify(ServerMessage servermessage) {}

}
