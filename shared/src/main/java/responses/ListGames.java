package responses;

import model.GameData;

import java.util.Collection;
import java.util.Map;

public class ListGames {
private final Collection<GameData> games;

public ListGames(Collection<GameData> gamesList) {
    this.games = gamesList;
}

    // Method to return a list of GameData values
public Collection<GameData> getGamesList() {
        return games;
    }
}
