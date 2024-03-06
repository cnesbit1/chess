package database;
import dataAccess.DataAccessException;
import exceptions.NoAuthException;
import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.Collection;

// Data Access Interface
public interface DataAccess {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    // Define other methods for CRUD operations

    // Methods for games
    public void createGame(GameData game) throws DataAccessException;

    public Collection<GameData> listGames();

    public GameData getGame(int gameID) throws DataAccessException;


    // Methods for auths
    public AuthData createAuth(String username) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException, NoAuthException;

    // Method to clear the database
    public void clear() throws DataAccessException;
}


