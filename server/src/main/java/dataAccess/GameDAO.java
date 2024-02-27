package dataAccess;

import database.MemoryDatabase;
import model.GameData;

import java.util.Collection;

public class GameDAO {

    private MemoryDatabase memoryDatabase;
    public GameDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void createGame(GameData gameData) {
        memoryDatabase.createGame(gameData);
    }

    public GameData getGame(int gameID) {
        return memoryDatabase.getGame(gameID);
    }

    public Collection<GameData> listGames() {
        return memoryDatabase.listGames();
    }

    public void joinGame(String username, int gameID, String clientColor) {
        memoryDatabase.updateGame(username, gameID, clientColor);
    }

    public boolean userExists(String username, int gameID, String clientColor) {
        return memoryDatabase.userExistsInGame(username, gameID, clientColor);
    }

    public void clear() {
        memoryDatabase.clearGames();
    }
}
