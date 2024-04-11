package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.severMessages.Error;
import java.io.IOException;

public class Connection {
    private Session session;

    private String authToken;


    public Connection(Session session, String authToken) {
        this.session = session;
        this.authToken = authToken;
    }

    public static void sendError(Session session, String errorMessage) {
        try {
            String totalErrorMessage = "Error: " + errorMessage;
            Error error = new Error(totalErrorMessage);
            Gson gson = new Gson();
            String errorJSON = gson.toJson(error);
            session.getRemote().sendString(errorJSON);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}