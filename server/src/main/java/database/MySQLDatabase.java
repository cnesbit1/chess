package database;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

// Implementation for MySQL Database
public class MySQLDatabase implements Database {
    @Override
    public void createUser(UserData userData) throws DataAccessException {
        
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
    // Implement methods to interact with MySQL database using JDBC
    // Implement createUser, getUser, and other methods
}
