package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.Map;

public class GameDAO {

    private DataAccess database;
    public GameDAO(DataAccess database) {
        this.database = database;
    }

    public void createGame(GameData gameData) throws DataAccessException {
        this.database.createGame(gameData);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return this.database.getGame(gameID);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return this.database.listGames();
    }

    public void joinGame(String username, int gameID, String clientColor) throws DataAccessException {
        this.database.updateGame(username, gameID, clientColor);
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        this.database.updateFullGame(gameData);
    }

    public boolean userExists(String username, int gameID, String clientColor) throws DataAccessException {
        return this.database.userExistsInGame(username, gameID, clientColor);
    }

    public void clear() throws DataAccessException {
        this.database.clearGames();
    }

    public Map<Integer, GameData>  getAllGames() throws DataAccessException {
        return this.database.getAllGames();
    }
}
