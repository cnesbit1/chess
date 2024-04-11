package ui;

import webSocketMessages.severMessages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage servermessage);
}
