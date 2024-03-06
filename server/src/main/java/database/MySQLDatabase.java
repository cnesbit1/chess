package database;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.Map;

// Implementation for MySQL Database
public class MySQLDatabase {

    public void createUser(UserData userData) throws DataAccessException {
        
    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void createGame(GameData game) throws DataAccessException {

    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public static AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    public static AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public static void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }

    public static void clearAuths() {
    }

    public static Map<String, AuthData> getAllAuths() {
        return null;
    }

    public Collection<GameData> listGames() {
        return null;
    }

    public void updateGame(String username, int gameID, String clientColor) {
    }

    public boolean userExistsInGame(String username, int gameID, String clientColor) {
        return true;
    }

    public void clearGames() {
    }

    public Map<Integer, GameData> getAllGames() {
        return null;
    }

    public Map<String, UserData> getAllUsers() {
        return null;
    }

    public void clearUsers() {
    }
    // Implement methods to interact with MySQL database using JDBC
    // Implement createUser, getUser, and other methods
}