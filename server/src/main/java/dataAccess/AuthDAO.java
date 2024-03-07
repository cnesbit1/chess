package dataAccess;

import dataAccess.database.MySQLDatabase;
import exceptions.NoAuthException;
import model.AuthData;

import java.util.Map;

public class AuthDAO {

    private MySQLDatabase mySQLDatabase;

    public AuthDAO(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return this.mySQLDatabase.createAuth(username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return this.mySQLDatabase.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException, NoAuthException {
        this.mySQLDatabase.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException {
        this.mySQLDatabase.clearAuths();
    }

    public Map<String, AuthData> getAllAuths() throws DataAccessException {
        return this.mySQLDatabase.getAllAuths();
    }
}
