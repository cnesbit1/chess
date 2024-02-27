package database;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;

import exceptions.NoAuthException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Implementation for Memory Database
public class MemoryDatabase {
    private Map<String, UserData> users;
    private Map<Integer, GameData> games;
    private Map<String, AuthData> auths;

    public MemoryDatabase() {
        users = new HashMap<>();
        games = new HashMap<>();
        auths = new HashMap<>();
    }

    public void createUser(UserData userData) throws DataAccessException {
        // Implementation for creating user in memory
        users.put(userData.username(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        // Implementation for getting user from memory
        return users.get(username);
    }

    public void createGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public Collection<GameData> listGames() {
        return this.games.values();
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }
    // Methods for auths
    public AuthData createAuth(String username) {
        // Generate a UUID for the authToken
        String authToken = UUID.randomUUID().toString();
        // Create an AuthData object
        AuthData auth = new AuthData(authToken, username);
        auths.put(auth.authToken(), auth);
        return auths.get(auth.authToken());
    }
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    public boolean userExistsInGame(String username, int gameID, String clientColor) {
        GameData gameData = games.get(gameID);
        if (gameData == null) {
            return false;
        }
        if ("white".equalsIgnoreCase(clientColor)) {
            return gameData.whiteUsername() != null;
        }
        if ("black".equalsIgnoreCase(clientColor)) {
            return gameData.blackUsername() != null;
        }
        return true;
    }

    public void updateGame(String username, int gameID, String clientColor) {
        GameData gameData = games.get(gameID);
        games.remove(gameID);
        ChessGame game = new ChessGame();
        GameData newGameData = null;
        if (clientColor.equalsIgnoreCase("white")) {
            newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), game);
        }
        else {
            newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), game);
        }
        games.put(gameID, newGameData);
    }
    public void deleteAuth(String authToken) throws NoAuthException {
        if (auths.get(authToken) == null) { throw new NoAuthException(); }
        auths.remove(authToken);
    }
    public void clearUsers() {
        users.clear();
    }
    public void clearGames() {
        games.clear();
    }
    public void clearAuths() {
        auths.clear();
    }

}
