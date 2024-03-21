package ui;

import java.util.Objects;

public class signedInUI extends abstractREPL {

    private enum optionsUI {
        LISTGAMES(1, "list games"),
        CREATEGAME(2, "create game"),
        JOINGAME(3, "join game"),

        OBSERVEGAME(4, "observe game"),

        LOGOUT(5, "log out");



        private final int number;
        private final String description;

        optionsUI(int number, String description) {
            this.number = number;
            this.description = description;
        }
    }
    public signedInUI(switchUILoop programLoop, serverFacade serverWrapper) {
        super(programLoop, serverWrapper);
    }

    @Override
    public void processInput(String input) {
        try {
            if (Objects.equals(input, "quit")) { throw new Exception(); }
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                System.out.println("This isn't implemented yet.");
            } else if (choice == 2) {
                System.out.println("This isn't implemented yet.");
            } else if (choice == 3) {
                System.out.println("This isn't implemented yet.");
            } else if (choice == 4) {
                System.out.println("This isn't implemented yet.");
            } else if (choice == 5) {
                System.out.println("This isn't implemented yet.");
            } else if (choice == 6) {
                programLoop.switchToInGame();
                changeUI();
            } else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("That wasn't a valid choice, try again.");
        }


    }

    @Override
    public void printHelp() {
        System.out.println("You are currently signed in.");
        System.out.println("Here are your available options.");
        System.out.println("List Games: show all the current games.");
        System.out.println("Create Game: create a new game to play chess.");
        System.out.println("Join Game: join an existing game.");
        System.out.println("Observe Game: watch an live game.");
        System.out.println("Logout: sign out of your account.");
        System.out.println();
    }

    @Override
    public void showUIPrompt() {
        System.out.println("Give an input of 'help' for more possible instructions.");
        for (signedInUI.optionsUI option : signedInUI.optionsUI.values()) {
            System.out.println("Enter " + option.number + " to " + option.description + ".");
        }
    }
}
