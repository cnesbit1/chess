package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.severMessages.LoadGame;
import webSocketMessages.severMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class webSocketHandler {
    private final ConnectionManager connectionManager;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public webSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.connectionManager = new ConnectionManager(authDAO);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        var conn = connectionManager.getConnection(command.getAuthString());

        if (conn == null & command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
            var joinPlayer = gson.fromJson(msg, JoinPlayer.class);
            var authToken = joinPlayer.getAuthString();
            var gameID = joinPlayer.getGameID();
            connectionManager.add(gameID, authToken, session);
        }

        if (authDAO.getAuth(command.getAuthString()) != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER:
                    JoinPlayer joinPlayer = gson.fromJson(msg, JoinPlayer.class);
                    this.joinGame(joinPlayer, conn);
                    break;
                case JOIN_OBSERVER:
                    JoinObserver joinObserver = gson.fromJson(msg, JoinObserver.class);
//                    conn.observeGame(msg);
                    break;
                case MAKE_MOVE:
                    MakeMove makeMove = gson.fromJson(msg, MakeMove.class);
//                    conn.makeMove(msg);
                    break;
                case LEAVE:
                    Leave leave = gson.fromJson(msg, Leave.class);
//                    conn.leaveGame(msg);
                    break;
                case RESIGN:
                    Resign resign = gson.fromJson(msg, Resign.class);
//                    conn.resignGame(msg);
                    break;
                default:
                    break;
            }
        } else Connection.sendError(session, "unknown user");
    }

    public void joinGame(JoinPlayer joinPlayer, Connection conn) throws DataAccessException, IOException {
        Integer gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();
        GameData gameData = gameDAO.getGame(gameID);
        LoadGame loadGame = new LoadGame(gameData);
        Gson gson = new Gson();
        String loadGameJson = gson.toJson(loadGame);
        conn.send(loadGameJson);
        Notification notification = null;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            notification = new Notification(String.format("%s has joined the game as white.", gameData.whiteUsername()));
        }
        else {
            notification = new Notification(String.format("%s has joined the game as black.", gameData.blackUsername()));
        }
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcast(joinPlayer.getAuthString(), notificationJson, gameID);
    }

    public void observeGame(String message) {
        // Logic to handle a player observing the game
    }

    public void makeMove(String message) {
        // Logic to handle a player making a move in the game
    }

    public void leaveGame(String message) {
        // Logic to handle a player leaving the game
    }

    public void resignGame(String message) {
        // Logic to handle a player resigning from the game
    }
}
