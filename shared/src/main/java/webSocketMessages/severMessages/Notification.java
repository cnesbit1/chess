package webSocketMessages.severMessages;

public class Notification extends ServerMessage {
    private final String message;
    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
