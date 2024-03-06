package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import exceptions.BadTeamColorException;
import exceptions.NoAuthException;
import exceptions.NoGameException;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.Map;


public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    private static int gameID = 1;

    public GameService(GameDAO gameAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
    }

    public int createGame(String authToken, String gameName) throws NoAuthException, DataAccessException {
        if (this.authAccess.getAuth(authToken) == null || gameName.isEmpty()) {
            throw new NoAuthException();
        }
        ChessGame game = new ChessGame();
        int gameID = GameService.gameID;
        GameService.gameID++;
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameAccess.createGame(gameData);
        return gameData.gameID();
    }

    public Collection<GameData> listGames(String authToken) throws NoAuthException, DataAccessException {
        if (this.authAccess.getAuth(authToken) == null) {
            throw new NoAuthException();
        }
        return gameAccess.listGames();
    }
    public void joinGame(String authToken, int gameID, String clientColor) throws NoGameException, NoAuthException, BadTeamColorException, DataAccessException {
        AuthData authData = authAccess.getAuth(authToken);
        if (authData == null) {
            throw new NoAuthException();
        }

        String username = authData.username();
        GameData gameData = gameAccess.getGame(gameID);

        if (gameData == null) {
            throw new NoGameException();
        }

        if (clientColor != null) {
            if (gameAccess.userExists(username, gameID, clientColor)) {
                throw new BadTeamColorException();
            }
            gameAccess.joinGame(username, gameID, clientColor);
        }

    }
}