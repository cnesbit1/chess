package dataAccess;
import database.MemoryDatabase;
import model.AuthData;
import model.UserData;

public class UserDAO {
    private MemoryDatabase memoryDatabase;

    public UserDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clear() {
        memoryDatabase.clearUsers();
    }

    public UserData getUser(String username) throws DataAccessException {
        return memoryDatabase.getUser(username);
    }
    public void createUser(UserData user) throws DataAccessException {
        memoryDatabase.createUser(user);
    }

}
