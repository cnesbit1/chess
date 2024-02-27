package responses;

public class JoinRequestData {
    private final String playerColor;

    private final int gameID;

    public JoinRequestData(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
