package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.MySQLDatabase;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.userDAO = new UserDAO(mySQLDatabase);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        this.userDAO.clear();
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData userData = new UserData("testusername", "testpassword", "testemail");
        userDAO.createUser(userData);

        assertEquals(userData, this.userDAO.getUser("testusername"));
    }

    @Test
    public void getUserFailure() throws DataAccessException {
        assertNull(this.userDAO.getUser("username"));
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        UserData userData = new UserData("testusername", "testpassword", "testemail");
        userDAO.createUser(userData);

        UserData finalUserData = userDAO.getUser("testusername");

        assertEquals(finalUserData, userData);
    }

    @Test
    public void createUserFailure() throws DataAccessException {
        UserData userData = new UserData("testusername", "testpassword", "testemail");
        userDAO.createUser(userData);

        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(userData);
        });
    }

    @Test
    public void getAllUsersSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("username1", "password1", "email@aol.com"));
        userDAO.createUser(new UserData("username2", "password2", "email2@aol.com"));
        userDAO.createUser(new UserData("username3", "password3", "email3@aol.com"));
        userDAO.createUser(new UserData("username4", "password4", "email4@aol.com"));
        userDAO.createUser(new UserData("username5", "password5", "email5@aol.com"));
        userDAO.createUser(new UserData("username6", "password6", "email6@aol.com"));

        assertNotNull(this.userDAO.getAllUsers());
    }

    @Test
    public void getAllUsersEmpty() throws DataAccessException {
        userDAO.createUser(new UserData("username1", "password1", "email@aol.com"));
        userDAO.createUser(new UserData("username2", "password2", "email2@aol.com"));
        userDAO.createUser(new UserData("username3", "password3", "email3@aol.com"));
        userDAO.createUser(new UserData("username4", "password4", "email4@aol.com"));
        userDAO.createUser(new UserData("username5", "password5", "email5@aol.com"));
        userDAO.createUser(new UserData("username6", "password6", "email6@aol.com"));

        userDAO.clear();

        assertTrue(this.userDAO.getAllUsers().isEmpty());
    }

    @Test
    public void clear() throws DataAccessException {
        UserData userData = new UserData("testusername", "testpassword", "testemail");
        userDAO.createUser(userData);

        userDAO.clear();

        UserData finalUserData = userDAO.getUser("testusername");

        assertNull(finalUserData);
    }
}
