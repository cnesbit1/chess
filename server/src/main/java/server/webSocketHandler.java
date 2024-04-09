package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.*;

@WebSocket
public class webSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        var conn = connectionManager.getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER:
                    JoinPlayer joinPlayer = gson.fromJson(msg, JoinPlayer.class);
                    conn.joinGame(joinPlayer);
                    break;
                case JOIN_OBSERVER:
                    JoinObserver joinObserver = gson.fromJson(msg, JoinObserver.class);
                    conn.observeGame(msg);
                    break;
                case MAKE_MOVE:
                    MakeMove makeMove = gson.fromJson(msg, MakeMove.class);
                    conn.makeMove(msg);
                    break;
                case LEAVE:
                    Leave leave = gson.fromJson(msg, Leave.class);
                    conn.leaveGame(msg);
                    break;
                case RESIGN:
                    Resign resign = gson.fromJson(msg, Resign.class);
                    conn.resignGame(msg);
                    break;
                default:
                    break;
            }
        } else Connection.sendError(session, "unknown user");
    }
}
