package dataAccess;

import dataAccess.database.MySQLDatabase;
import model.GameData;

import java.util.Collection;
import java.util.Map;

public class GameDAO {

    private MySQLDatabase mySQLDatabase;
    public GameDAO(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public void createGame(GameData gameData) throws DataAccessException {
        mySQLDatabase.createGame(gameData);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return mySQLDatabase.getGame(gameID);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return mySQLDatabase.listGames();
    }

    public void joinGame(String username, int gameID, String clientColor) throws DataAccessException {
        mySQLDatabase.updateGame(username, gameID, clientColor);
    }

    public boolean userExists(String username, int gameID, String clientColor) throws DataAccessException {
        return mySQLDatabase.userExistsInGame(username, gameID, clientColor);
    }

    public void clear() throws DataAccessException {
        mySQLDatabase.clearGames();
    }

    public Map<Integer, GameData>  getAllGames() throws DataAccessException {
        return mySQLDatabase.getAllGames();
    }
}
