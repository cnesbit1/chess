package dataAccess;
import dataAccess.database.MySQLDatabase;
import model.UserData;

import java.util.Map;

public class UserDAO {
    private MySQLDatabase mySQLDatabase;

    public UserDAO(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public void clear() throws DataAccessException {
        mySQLDatabase.clearUsers();
    }

    public UserData getUser(String username) throws DataAccessException {
        return mySQLDatabase.getUser(username);
    }
    public void createUser(UserData user) throws DataAccessException{
        mySQLDatabase.createUser(user);
    }

    public Map<String, UserData> getAllUsers() throws DataAccessException {
        return mySQLDatabase.getAllUsers();
    }

}
