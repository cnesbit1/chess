package server;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import dataAccess.AuthDAO;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.severMessages.Notification;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Collection<Connection>> connections = new ConcurrentHashMap<>();
    private final AuthDAO authDAO;

    public ConnectionManager(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public Connection getConnection(String authString) {
        try {
            for (Collection<Connection> connectionCollection : connections.values()) {
                for (Connection conn : connectionCollection) {
                    if (conn.getAuthToken().equals(authString)) {
                        return conn;
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    public void add(Integer gameID, String authToken, Session session) {
        Connection conn = new Connection(session, authToken);
        connections.get(gameID).add(conn);
    }

    public void remove(Integer gameID, String authToken) {
        var gameConnections = connections.get(gameID);
        gameConnections.removeIf(conn -> conn.getAuthToken().equals(authToken));
    }

    public void broadcast(String excludeAuthToken, String notificationJson, Integer gameID) throws IOException {
        for (var c : connections.get(gameID)) {
            if (c.getSession().isOpen()) {
                if (!c.getAuthToken().equals(excludeAuthToken)) {
                    c.send(notificationJson);
                }
            }
        }
    }

    public void broadcastGame(String notificationJson, Integer gameID) throws IOException {
        for (var c : connections.get(gameID)) {
            if (c.getSession().isOpen()) {
                c.send(notificationJson);
            }
        }
    }
}
