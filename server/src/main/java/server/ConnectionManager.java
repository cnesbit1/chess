package server;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Connection getConnection(String authString, Session session) {
        // Check if the connection for the given authString already exists
        Connection conn = connections.get(authString);
        if (conn == null) {
            // If not, create a new connection and store it
            conn = new Connection(session, authString);
            connections.put(authString, conn);
        }
        return conn;
    }
}
