package server;
import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    private Session session;
    private String authToken;

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

    // Define other methods for handling game actions, such as joining, observing, making moves, leaving, resigning
    public void joinGame(String message) {
        // Logic to handle a player joining the game
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