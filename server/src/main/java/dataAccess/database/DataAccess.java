package dataAccess.database;
import dataAccess.DataAccessException;
import exceptions.NoAuthException;
import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.Collection;
import java.util.Map;

// Data Access Interface
public interface DataAccess {
    // Methods for users
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;

    // Methods for games
    void createGame(GameData game) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;


    // Methods for auths
    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException, NoAuthException;

    // Helper methods for users, games, and auth
    void updateGame(String username, int gameID, String clientColor) throws DataAccessException;

    boolean userExistsInGame(String username, int gameID, String clientColor) throws DataAccessException;

    Map<Integer, GameData> getAllGames() throws DataAccessException;

    Map<String, UserData> getAllUsers() throws DataAccessException;

    Map<String, AuthData> getAllAuths() throws DataAccessException;

    // Method to clear the database
    void clear() throws DataAccessException;

    void clearGames() throws DataAccessException;

    void clearUsers() throws DataAccessException;

    void clearAuths() throws DataAccessException;
}


