package ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

@ClientEndpoint
public class connectionWebSocket {

    private Session session;
    public String baseURL;
    public String authToken;

    public connectionWebSocket(String authToken, String host, int port) {
        try {
            this.authToken = authToken;
            this.baseURL = String.format("ws://%s:%s/connect", host, port);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(this.baseURL));

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Disconnected from server: " + closeReason.getReasonPhrase());
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message from server: " + message);
        // Handle incoming messages from the server
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

    public void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}