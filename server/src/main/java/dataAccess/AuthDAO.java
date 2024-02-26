package dataAccess;

import database.MemoryDatabase;
import model.AuthData;

public class AuthDAO {

    private MemoryDatabase memoryDatabase;

    public AuthDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public AuthData createAuth(String username) {
        return memoryDatabase.createAuth(username);
    }
//
//    public void getAuth() {}
//
//    public void deleteAuth() {}

    public void clear() {
        memoryDatabase.clearAuths();
    }
}
