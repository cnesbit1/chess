package dataAccess;
import database.MemoryDatabase;
import database.MySQLDatabase;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.Map;

public class UserDAO {
    private database.MySQLDatabase mySQLDatabase;

    public UserDAO(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public void clear() {
        mySQLDatabase.clearUsers();
    }

    public UserData getUser(String username) throws DataAccessException {
        return mySQLDatabase.getUser(username);
    }
    public void createUser(UserData user) throws DataAccessException, SQLException {
        mySQLDatabase.createUser(user);
    }

    public Map<String, UserData> getAllUsers() {
        return mySQLDatabase.getAllUsers();
    }

}
