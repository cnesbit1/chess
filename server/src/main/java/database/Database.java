package database;
import dataAccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;
import java.util.Map;
import java.util.HashMap;

// Data Access Interface
public interface Database {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    // Define other methods for CRUD operations

    // Methods for games
    public void createGame(GameData game) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    // Methods for auths
    public void createAuth(AuthData auth) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;

    // Method to clear the database
    public void clear() throws DataAccessException;
}

