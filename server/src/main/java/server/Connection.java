package server;
import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import webSocketMessages.userCommands.JoinPlayer;

public class Connection {
    private Session session;
    private String authToken;

    private GameService gameService;

    public Connection(Session session, String authToken) {
        this.session = session;
        this.authToken = authToken;
    }

    public static void sendError(Session session, String errorMessage) {
        try {
            session.getRemote().sendString("Error: " + errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame(JoinPlayer joinPlayer) {
        Integer gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();

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