package dataAccess;
import dataAccess.database.DataAccess;
import dataAccess.database.DatabaseManager;
import dataAccess.database.MySQLDatabase;
import model.UserData;

import java.util.Map;

public class UserDAO {
    private DataAccess database;

    public UserDAO(DataAccess database) {
        this.database = database;
    }

    public void clear() throws DataAccessException {
        this.database.clearUsers();
    }

    public UserData getUser(String username) throws DataAccessException {
        return this.database.getUser(username);
    }
    public void createUser(UserData user) throws DataAccessException{
        this.database.createUser(user);
    }

    public Map<String, UserData> getAllUsers() throws DataAccessException {
        return this.database.getAllUsers();
    }

}
