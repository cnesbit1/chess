package database;

import dataAccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;

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

//    public void createGame(GameData game) {
//        games.put(game.gameID(), game);
//    }
//
//    public GameData getGame(int gameID) {
//        return games.get(gameID);
//    }
//
    // Methods for auths
    public AuthData createAuth(String username) {
        // Generate a UUID for the authToken
        String authToken = UUID.randomUUID().toString();
        // Create an AuthData object
        AuthData auth = new AuthData(authToken, username);
        auths.put(auth.authToken(), auth);
        return auths.get(auth.authToken());
    }
//
//    public AuthData getAuth(String authToken) {
//        return auths.get(authToken);
//    }
//
//    public void deleteAuth(String authToken) {
//        auths.remove(authToken);
//    }

    public void clearUsers() {
        users.clear();
    }
    public void clearGames() {
        games.clear();
    }

    public void clearAuths() {
        auths.clear();
    }

    // Implement other methods for CRUD operations
}
