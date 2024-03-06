package database;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

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

    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
    // Implement methods to interact with MySQL database using JDBC
    // Implement createUser, getUser, and other methods
}