import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import database.MemoryDatabase;
import exceptions.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        // Arrange
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        UserData userData = new UserData(username, password, email);
        AuthData authData = new AuthData("authToken", username);

        // Act
        AuthData result = userService.register(username, password, email);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username());
    }

    @Test
    public void testRegisterUsernameTaken() throws UsernameTakenException, DataAccessException {
        // Arrange
        String username = "existingUser";
        String password = "password123";
        String email = "existing@example.com";
        UserData userData = new UserData(username, password, email);
        userService.register(username, password, email);

        // Act & Assert
        assertThrows(UsernameTakenException.class, () -> {
            userService.register(username, password, email);
        });
    }
//
//    @Test
//    public void testLoginSuccess() throws DataAccessException, WrongPasswordException, NoUserException {
//        // Arrange
//        String username = "testUser";
//        String password = "password123";
//        UserData userData = new UserData(username, password, "test@example.com");
//        AuthData authData = new AuthData("authToken", username);
//
//        when(userDAO.getUser(username)).thenReturn(userData);
//        when(authDAO.createAuth(username)).thenReturn(authData);
//
//        // Act
//        AuthData result = userService.login(username, password);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(username, result.getUsername());
//    }
//
//    @Test
//    public void testLoginWrongPassword() throws DataAccessException, NoUserException {
//        // Arrange
//        String username = "testUser";
//        String password = "password123";
//        UserData userData = new UserData(username, password, "test@example.com");
//
//        when(userDAO.getUser(username)).thenReturn(userData);
//
//        // Act & Assert
//        assertThrows(WrongPasswordException.class, () -> {
//            userService.login(username, "wrongPassword");
//        });
//    }
//
//    @Test
//    public void testLoginNoUser() throws DataAccessException {
//        // Arrange
//        String username = "nonExistingUser";
//
//        when(userDAO.getUser(username)).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(NoUserException.class, () -> {
//            userService.login(username, "password123");
//        });
//    }
//
    @Test
    public void testLogoutSuccess() throws NoAuthException, NoUserException, DataAccessException, WrongPasswordException, UsernameTakenException {

        AuthData authData = userService.register("username", "password", "email@aol.com");
        // AuthData authData = userService.login("username", "password");
        String authToken = authData.authToken();
        userService.logout(authToken);

        assertThrows(NoUserException.class, () -> {
            this.authDAO.getAuth(authToken);
        });

    }
}