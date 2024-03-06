package dataAccess;

import database.MemoryDatabase;
import database.MySQLDatabase;
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

    public Collection<GameData> listGames() {
        return mySQLDatabase.listGames();
    }

    public void joinGame(String username, int gameID, String clientColor) {
        mySQLDatabase.updateGame(username, gameID, clientColor);
    }

    public boolean userExists(String username, int gameID, String clientColor) {
        return mySQLDatabase.userExistsInGame(username, gameID, clientColor);
    }

    public void clear() {
        mySQLDatabase.clearGames();
    }

    public Map<Integer, GameData>  getAllGames() {
        return mySQLDatabase.getAllGames();
    }
}
