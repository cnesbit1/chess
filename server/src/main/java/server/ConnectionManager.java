package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.api.Session;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, CopyOnWriteArraySet<Connection>> connections = new ConcurrentHashMap<>();

    public ConnectionManager() {}

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

    public Connection add(Integer gameID, String authToken, Session session) {
        Connection conn = new Connection(session, authToken);

        ConcurrentHashMap<Integer, CopyOnWriteArraySet<Connection>> gameConnections = connections;
        CopyOnWriteArraySet<Connection> connectionSet = gameConnections.get(gameID);

        if (connectionSet == null) {
            connectionSet = new CopyOnWriteArraySet<>();
            CopyOnWriteArraySet<Connection> existingSet = gameConnections.putIfAbsent(gameID, connectionSet);
            if (existingSet != null) {
                connectionSet = existingSet;
            }
        }

        connectionSet.add(conn);
        return conn;
    }

    public void remove(Integer gameID, String authToken) {
        var gameConnections = connections.get(gameID);
        gameConnections.removeIf(conn -> conn.getAuthToken().equals(authToken));
    }

    public void broadcast(String excludeAuthToken, String json, Integer gameID) throws IOException {
        for (var c : connections.get(gameID)) {
            if (c.getSession().isOpen()) {
                if (!c.getAuthToken().equals(excludeAuthToken)) {
                    c.send(json);
                }
            }
        }
    }

    public void broadcastGame(String json, Integer gameID) throws IOException {
        for (var c : connections.get(gameID)) {
            if (c.getSession().isOpen()) {
                c.send(json);
            }
        }
    }
}
