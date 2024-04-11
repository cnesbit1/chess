package ui;


import model.GameData;
import responses.ListGames;
import webSocketMessages.severMessages.ServerMessage;

import java.util.Collection;

public class SignedInUI extends AbstractREPL {
    private enum OptionsUI {
        LISTGAMES(1, "list games"),
        CREATEGAME(2, "create game"),
        JOINGAME(3, "join game"),
        OBSERVEGAME(4, "observe game"),
        LOGOUT(5, "log out");
        private final int number;
        private final String description;
        OptionsUI(int number, String description) {
            this.number = number;
            this.description = description;
        }
    }
    public SignedInUI(SwitchUILoop programLoop, ServerFacade serverWrapper) {
        super(programLoop, serverWrapper);
    }

    @Override
    public void processInput(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                System.out.println("List Games:");
                ListGames games = serverWrapper.listGames(serverWrapper.conn.authToken);
                Collection<GameData> listGames = games.getGamesList();
                int index = 1;
                for (GameData game : listGames) {
                    System.out.println("Game Number: " + index);
                    index = index + 1;
                    System.out.printf("Game ID: %s - White Username: %s - Black Username: %s - Game Name: %s%n",
                            game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
                }
                System.out.println();
            }
            else if (choice == 2) {
                System.out.println("Input Game Name:");
                String gamename = scanner.nextLine();
                serverWrapper.createGame(serverWrapper.conn.authToken, gamename);
                System.out.println();
                System.out.println("Successful Game Created");
                System.out.println();
            }
            else if (choice == 3) {
                System.out.println("Input GameID:");
                String gameID = scanner.nextLine();
                System.out.println("Input White or Black:");
                String playerColor = scanner.nextLine();
                serverWrapper.joinGame(serverWrapper.conn.authToken, Integer.parseInt(gameID), playerColor);
                programLoop.switchToInGame();
                changeUI();
            }
            else if (choice == 4) {
                System.out.println("Input GameID:");
                String gameID = scanner.nextLine();
                serverWrapper.joinGame(serverWrapper.conn.authToken, Integer.parseInt(gameID), null);
                programLoop.switchToInGame();
                changeUI();
            }
            else if (choice == 5) {
                serverWrapper.logout(serverWrapper.conn.authToken);
                System.out.println();
                System.out.println("Successful Logout");
                System.out.println();
                programLoop.switchToSignedOut();
                changeUI();
            }
            else { throw new Exception(); }
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
        for (OptionsUI option : OptionsUI.values()) {
            System.out.println("Enter " + option.number + " to " + option.description + ".");
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {}
}
