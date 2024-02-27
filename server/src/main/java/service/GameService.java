package service;

import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import exceptions.BadTeamColorException;
import exceptions.NoAuthException;
import exceptions.NoGameException;
import model.AuthData;
import model.GameData;

import java.util.Map;


public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    private int gameID;

    public GameService(GameDAO gameAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
        this.gameID = 1;
    }

    public int createGame(String authToken, String gameName) throws NoGameException {
        if (this.authAccess.getAuth(authToken) == null || gameName.isEmpty()) {
            throw new NoGameException();
        }
        ChessGame game = new ChessGame();
        int gameID = this.gameID;
        this.gameID++;
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameAccess.createGame(gameData);
        return gameData.gameID();
    }

    public Map<Integer, GameData> listGames(String authToken) throws NoGameException {
        if (this.authAccess.getAuth(authToken) == null) {
            throw new NoGameException();
        }


        return gameAccess.listGames();
    }
    public void joinGame(String authToken, int gameID, String clientColor) throws NoGameException, NoAuthException, BadTeamColorException {
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