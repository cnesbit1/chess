package dataAccess;

import database.MemoryDatabase;
import database.MySQLDatabase;
import exceptions.NoAuthException;
import model.AuthData;

import java.util.Map;

public class AuthDAO {

    private MySQLDatabase mySQLDatabase;

    public AuthDAO(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return MySQLDatabase.createAuth(username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return MySQLDatabase.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws NoAuthException, DataAccessException {
        MySQLDatabase.deleteAuth(authToken);
    }

    public void clear() {
        MySQLDatabase.clearAuths();
    }

    public Map<String, AuthData> getAllAuths() {
        return MySQLDatabase.getAllAuths();
    }
}
