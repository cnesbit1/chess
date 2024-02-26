package dataAccess;

import database.MemoryDatabase;

public class GameDAO {

    private MemoryDatabase memoryDatabase;
    public GameDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

//    public void createGame() {}
//
//    public void getGame() {}
//
//    public void listGames() {}
//
//    public void updateGame() {}

    public void clear() {
        memoryDatabase.clearGames();
    }
}
