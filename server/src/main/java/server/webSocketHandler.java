package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.severMessages.LoadGame;
import webSocketMessages.severMessages.Notification;
import webSocketMessages.userCommands.*;


import java.util.Collection;
import java.util.Objects;

@WebSocket
public class webSocketHandler {
    private final ConnectionManager connectionManager;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;


    public webSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.connectionManager = new ConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        try {
            Gson gson = new Gson();
            UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

            var conn = connectionManager.getConnection(command.getAuthString());
            var authToken = command.getAuthString();
            if (conn == null) {
                if (command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
                    var joinPlayer = gson.fromJson(msg, JoinPlayer.class);
                    authToken = joinPlayer.getAuthString();
                    var gameID = joinPlayer.getGameID();
                    conn = connectionManager.add(gameID, authToken, session);
                }
                else if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                    var joinObserver = gson.fromJson(msg, JoinObserver.class);
                    authToken = joinObserver.getAuthString();
                    var gameID = joinObserver.getGameID();
                    conn = connectionManager.add(gameID, authToken, session);
                }
            }

            switch (command.getCommandType()) {
                case JOIN_PLAYER:
                    JoinPlayer joinPlayer = gson.fromJson(msg, JoinPlayer.class);
                    this.joinGame(joinPlayer, conn, authToken);
                    break;
                case JOIN_OBSERVER:
                    JoinObserver joinObserver = gson.fromJson(msg, JoinObserver.class);
                    this.observeGame(joinObserver, conn, authToken);
                    break;
                case MAKE_MOVE:
                    MakeMove makeMove = gson.fromJson(msg, MakeMove.class);
                    this.makeMove(makeMove, conn, authToken);
                    break;
                case LEAVE:
                    Leave leave = gson.fromJson(msg, Leave.class);
                    this.leaveGame(leave, conn, authToken);
                    break;
                case RESIGN:
                    Resign resign = gson.fromJson(msg, Resign.class);
                    resignGame(resign, conn, authToken);
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            Connection.sendError(session, e.getMessage());
        }
    }

    public void joinGame(JoinPlayer joinPlayer, Connection conn, String authToken) throws Exception {
        Integer gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();
        GameData gameData = gameDAO.getGame(gameID);
        Notification notification = null;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (!Objects.equals(gameData.whiteUsername(), authDAO.getAuth(authToken).username())) {
                throw new Exception("User tried to connect with the wrong color for joinPlayer, whitw");
            }
            notification = new Notification(String.format("%s has joined the game as white.", gameData.whiteUsername()));
        }
        else {
            if (!Objects.equals(gameData.blackUsername(), authDAO.getAuth(authToken).username())) {
                throw new Exception("User tried to connect with the wrong color for joinPlayer, black");
            }
            notification = new Notification(String.format("%s has joined the game as black.", gameData.blackUsername()));
        }

        LoadGame loadGame = new LoadGame(gameData);
        Gson gson = new Gson();
        String loadGameJson = gson.toJson(loadGame);
        conn.send(loadGameJson);
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcast(joinPlayer.getAuthString(), notificationJson, gameID);
    }

    public void observeGame(JoinObserver joinObserver, Connection conn, String authToken) throws Exception {
        Integer gameID = joinObserver.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new Exception("No game found");
        }
        LoadGame loadGame = new LoadGame(gameData);
        Gson gson = new Gson();
        String loadGameJson = gson.toJson(loadGame);
        conn.send(loadGameJson);
        Notification notification = new Notification(String.format("%s has joined the game as an observer.", authDAO.getAuth(authToken).username()));
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcast(authToken, notificationJson, gameID);
    }

    public void makeMove(MakeMove makeMove, Connection conn, String authToken) throws Exception {
        String username = authDAO.getAuth(authToken).username();
        ChessMove move = makeMove.getMove();
        Integer gameID = makeMove.getGameID();

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new Exception("No game found");
        }

        ChessGame game = gameData.game();
        if (game.getBoard().checkEmpty()) {
            game.getBoard().resetBoard();
        }
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        boolean isWhite = false;
        boolean isBlack = false;
        if (username.equals(whiteUsername)) {
            isWhite = true;
            if (game.getTeamTurn() != ChessGame.TeamColor.WHITE) {
                throw new Exception("It isn't your turn, white user on black turn");
            }
        }
        else if (username.equals(blackUsername)) {
            isBlack = true;
            if (game.getTeamTurn() != ChessGame.TeamColor.BLACK) {
                throw new Exception("It isn't your turn, black user on white turn");
            }
        }
        else {
            throw new Exception("You can't make a move as an observer");
        }


        if (game.isGameComplete()) {
            throw new Exception("The game is already complete");
        }
        Collection<ChessMove> moves = game.validMoves(move.getStartPosition());
        if (!moves.contains(move)) {
            throw new Exception("This is an illegal move");
        }
        if (isWhite & game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != ChessGame.TeamColor.WHITE) {
            throw new Exception("You are trying to move the wrong piece, white trying to move black piece");
        }
        else if (isBlack & game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != ChessGame.TeamColor.BLACK) {
            throw new Exception("You are trying ot move the wrong piece, black trying ot move white piece");
        }

        game.makeMove(move);
        if (game.isGameComplete()) {
            throw new Exception("Checkmate or Stalemate");
        }

        GameData newGameData = new GameData(gameID, whiteUsername, blackUsername, gameData.gameName(), game);
        gameDAO.updateGame(newGameData);

        LoadGame loadGame = new LoadGame(gameData);
        Gson gson = new Gson();
        String loadGameJson = gson.toJson(loadGame);
        connectionManager.broadcastGame(loadGameJson, gameID);

        Notification notification = new Notification(String.format("%s has made a move.", username));
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcast(authToken, notificationJson, gameID);
    }

    public void leaveGame(Leave leave, Connection conn, String authToken) throws Exception {
        String username = authDAO.getAuth(authToken).username();
        Integer gameID = leave.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new Exception("There is no game");
        }
        GameData newGameData = gameData;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            newGameData = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else if (Objects.equals(gameData.blackUsername(), username)) {
            newGameData = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        }

        gameDAO.updateGame(newGameData);

        connectionManager.remove(newGameData.gameID(), authToken);

        Gson gson = new Gson();
        Notification notification = new Notification(String.format("%s has left the game.", username));
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcast(authToken, notificationJson, gameID);

    }

    public void resignGame(Resign resign, Connection conn, String authToken) throws Exception {
        String username = authDAO.getAuth(authToken).username();
        Integer gameID = resign.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new Exception();
        }
        GameData newGameData = gameData;
        ChessGame game = newGameData.game();
        if (game.isGameComplete()) {
            throw new Exception("The game is already complete");
        }
        if (!username.equals(gameData.whiteUsername()) & !username.equals(gameData.blackUsername())) {
            throw new Exception("You can't resign if you are observing");
        }
        game.endGame();
        newGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        gameDAO.updateGame(newGameData);

        Gson gson = new Gson();
        Notification notification = new Notification(String.format("%s has resigned.", username));
        String notificationJson = gson.toJson(notification);
        connectionManager.broadcastGame(notificationJson, gameID);
    }
}
