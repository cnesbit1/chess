package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.MySQLDatabase;
import exceptions.*;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.userDAO = new UserDAO(mySQLDatabase);
        this.authDAO = new AuthDAO(mySQLDatabase);
        this.userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        this.userDAO.clear();
        this.authDAO.clear();
    }

    @Test
    public void testRegisterSuccess() throws DataAccessException, UsernameTakenException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData result = userService.register(username, password, email);

        assertNotNull(result);
        assertEquals(username, result.username());
    }

    @Test
    public void testRegisterUsernameTaken() throws UsernameTakenException, DataAccessException, SQLException {
        String username = "existingUser";
        String password = "password123";
        String email = "existing@example.com";
        userService.register(username, password, email);

        assertThrows(UsernameTakenException.class, () -> {
            userService.register(username, password, email);
        });
    }

    @Test
    public void testLoginSuccess() throws DataAccessException, WrongPasswordException, NoUserException, UsernameTakenException, NoAuthException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        this.authDAO.deleteAuth(authToken);

        AuthData result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(username, result.username());
    }

    @Test
    public void testLoginWrongPassword() throws DataAccessException, NoUserException, UsernameTakenException, NoAuthException, WrongPasswordException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        this.authDAO.deleteAuth(authToken);

        assertThrows(WrongPasswordException.class, () -> {
            userService.login(username, "wrongPassword");
        });
    }

    @Test
    public void testLoginNoUser() throws DataAccessException {
        assertThrows(NoUserException.class, () -> {
            userService.login("username", "password123");
        });
    }

    @Test
    public void testLogoutSuccess() throws NoAuthException, NoUserException, DataAccessException, WrongPasswordException, UsernameTakenException, SQLException {

        AuthData authData = userService.register("username", "password", "email@aol.com");
        String authToken = authData.authToken();

        assertDoesNotThrow(() -> {
            userService.logout(authToken);
        });
    }

    @Test
    public void testLogoutNoAuth() throws NoAuthException, DataAccessException, UsernameTakenException, SQLException {

        AuthData authData = userService.register("username", "password", "email@aol.com");
        String authToken = authData.authToken();
        userService.logout(authToken);
        assertThrows(NoAuthException.class, () -> {
            userService.logout(authToken);
        });
    }
}