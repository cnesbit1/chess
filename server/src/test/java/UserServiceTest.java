import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import database.MemoryDatabase;
import exceptions.*;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        this.userDAO = new UserDAO(memoryDatabase);
        this.authDAO = new AuthDAO(memoryDatabase);
        this.userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    public void cleanUp() {
        this.userDAO.clear();
        this.authDAO.clear();
    }

    @Test
    public void testRegisterSuccess() throws DataAccessException, UsernameTakenException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData result = userService.register(username, password, email);

        assertNotNull(result);
        assertEquals(username, result.username());
    }

    @Test
    public void testRegisterUsernameTaken() throws UsernameTakenException, DataAccessException {
        String username = "existingUser";
        String password = "password123";
        String email = "existing@example.com";
        userService.register(username, password, email);

        assertThrows(UsernameTakenException.class, () -> {
            userService.register(username, password, email);
        });
    }

    @Test
    public void testLoginSuccess() throws DataAccessException, WrongPasswordException, NoUserException, UsernameTakenException, NoAuthException {
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
    public void testLoginWrongPassword() throws DataAccessException, NoUserException, UsernameTakenException, NoAuthException, WrongPasswordException {
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
    public void testLogoutSuccess() throws NoAuthException, NoUserException, DataAccessException, WrongPasswordException, UsernameTakenException {

        AuthData authData = userService.register("username", "password", "email@aol.com");
        String authToken = authData.authToken();

        assertDoesNotThrow(() -> {
            userService.logout(authToken);
        });
    }

    @Test
    public void testLogoutNoAuth() throws NoAuthException, DataAccessException, UsernameTakenException {

        AuthData authData = userService.register("username", "password", "email@aol.com");
        String authToken = authData.authToken();
        userService.logout(authToken);
        assertThrows(NoAuthException.class, () -> {
            userService.logout(authToken);
        });
    }
}