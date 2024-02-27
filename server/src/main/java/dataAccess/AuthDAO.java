package dataAccess;

import database.MemoryDatabase;
import exceptions.NoAuthException;
import model.AuthData;

public class AuthDAO {

    private MemoryDatabase memoryDatabase;

    public AuthDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public AuthData createAuth(String username) {
        return memoryDatabase.createAuth(username);
    }

    public AuthData getAuth(String authToken) {
        return memoryDatabase.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws NoAuthException {
        memoryDatabase.deleteAuth(authToken);
    }

    public void clear() {
        memoryDatabase.clearAuths();
    }
}
