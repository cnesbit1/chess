package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class webSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        var conn = connectionManager.getConnection(command.getAuthString(), session);
        if (conn != null) {
//            switch (command.getCommandType()) {
//                case JOIN_PLAYER -> join(conn, msg);
//                case JOIN_OBSERVER -> observe(conn, msg);
//                case MAKE_MOVE -> move(conn, msg);
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
//            }
        } else Connection.sendError(session, "unknown user");
    }
}
