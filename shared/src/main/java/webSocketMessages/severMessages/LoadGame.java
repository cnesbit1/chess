package webSocketMessages.severMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    private final ChessGame game;
    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }
}
