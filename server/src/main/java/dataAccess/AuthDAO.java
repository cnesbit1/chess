package dataAccess;

import dataAccess.database.DataAccess;
import dataAccess.database.MySQLDatabase;
import exceptions.NoAuthException;
import model.AuthData;

import java.util.Map;

public class AuthDAO {

    private DataAccess database;

    public AuthDAO(DataAccess database) {
        this.database = database;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return this.database.createAuth(username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException, NoAuthException {
        return this.database.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException, NoAuthException {
        this.database.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException {
        this.database.clearAuths();
    }

    public Map<String, AuthData> getAllAuths() throws DataAccessException {
        return this.database.getAllAuths();
    }
}
