package dataAccessTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.database.MySQLDatabase;
import exceptions.NoAuthException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.authDAO = new AuthDAO(mySQLDatabase);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        this.authDAO.clear();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        String username = "testUser";
        AuthData authData = this.authDAO.createAuth(username);
        assertNotNull(authData);
    }

    @Test
    public void createAuthFailure() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            this.authDAO.createAuth(null);
        });
    }

    @Test
    public void getAuthSuccess() throws DataAccessException, NoAuthException {
        String username = "testUser";
        AuthData initialAuthData = this.authDAO.createAuth(username);
        String authToken = initialAuthData.authToken();
        AuthData finalAuthData = this.authDAO.getAuth(authToken);
        assertNotNull(finalAuthData);

    }

    @Test
    public void getAuthFailure() throws DataAccessException, NoAuthException {
        assertThrows(NoAuthException.class, () -> {
            this.authDAO.getAuth("1234");
        });
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException, NoAuthException {
        String username = "testUser";
        AuthData authData = this.authDAO.createAuth(username);
        String authToken = authData.authToken();

        try {
            authDAO.deleteAuth(authToken);
            assert true;
        }
        catch (NoAuthException e) {
            assert false;
        }
    }

    @Test
    public void deleteAuthFailure() throws DataAccessException {
        String authToken = "1234";

        assertThrows(NoAuthException.class, () -> {
            authDAO.deleteAuth(authToken);
        });
    }

    @Test
    public void getAllAuthsSuccess() throws DataAccessException {
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        authDAO.createAuth("username3");
        authDAO.createAuth("username4");
        authDAO.createAuth("username5");
        authDAO.createAuth("username6");

        assertNotNull(this.authDAO.getAllAuths());
    }

    @Test
    public void getAllAuthsEmpty() throws DataAccessException {
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        authDAO.createAuth("username3");
        authDAO.createAuth("username4");
        authDAO.createAuth("username5");
        authDAO.createAuth("username6");

        authDAO.clear();

        assertTrue(this.authDAO.getAllAuths().isEmpty());
    }

    @Test
    public void clear() throws DataAccessException {
        AuthData authData = authDAO.createAuth("username1");
        authDAO.clear();

        assertThrows(NoAuthException.class, () -> {
            authDAO.getAuth(authData.authToken());
        });
    }
}
