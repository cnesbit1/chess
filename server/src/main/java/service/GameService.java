package service;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;


public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    public GameService(GameDAO gameAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
    }

//    public void createGame() {
//        gameAccess.createGame();
//    }
//
//    public void getGame() {
//        gameAccess.getGame();
//    }
//
//    public void listGames() {
//        gameAccess.listGames();
//    }
//
//    public void updateGame() {
//        gameAccess.updateGame();
//    }
//
//    public void createAuth() {
//        authAccess.createAuth();
//    }
//
//    public void getAuth() {
//        authAccess.getAuth();
//    }
//
//    public void deleteAuth() {
//        authAccess.deleteAuth();
//    }
}