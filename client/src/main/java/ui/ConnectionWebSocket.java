package ui;

import com.google.gson.Gson;
import webSocketMessages.severMessages.LoadGame;
import webSocketMessages.severMessages.Error;
import webSocketMessages.severMessages.Notification;
import webSocketMessages.severMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class ConnectionWebSocket {

    private Session session;
    public String baseURL;
    public String authToken;

    public boolean firstLoadGame;

    public NotificationHandler notificationGameplayHandler;
    public NotificationHandler notificationJoinGameHandler;

    public ConnectionWebSocket(String authToken, String host, int port, NotificationHandler notificationGameplayHandler, NotificationHandler notificationJoinGameHandler) {
        try {
            this.authToken = authToken;
            this.baseURL = String.format("ws://%s:%s/connect", host, port);
            this.notificationGameplayHandler = notificationGameplayHandler;
            this.notificationJoinGameHandler = notificationJoinGameHandler;
            this.firstLoadGame = true;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(this.baseURL));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public void setNotificationGameplayHandler(NotificationHandler notificationGameplayHandler) { this.notificationGameplayHandler = notificationGameplayHandler; }
    public void setNotificationJoinGameHandler(NotificationHandler notificationJoinGameHandler) { this.notificationJoinGameHandler = notificationJoinGameHandler; }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server using web socket");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Disconnected from server with web sockets: " + closeReason.getReasonPhrase());
        this.session = null;
    }

    public void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        Gson gson = new Gson();
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();

        switch (type) {
            case LOAD_GAME:
                LoadGame loadGame = gson.fromJson(message, LoadGame.class);
                notificationGameplayHandler.notify(loadGame);
                break;
            case NOTIFICATION:
                Notification notification = gson.fromJson(message, Notification.class);
                notificationGameplayHandler.notify(notification);
                break;
            case ERROR:
                Error error = gson.fromJson(message, Error.class);
                notificationGameplayHandler.notify(error);
                break;
            default:
                break;
        }
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}