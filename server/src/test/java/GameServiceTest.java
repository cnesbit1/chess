import static org.junit.jupiter.api.Assertions.*;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import database.MemoryDatabase;
import exceptions.UsernameTakenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import exceptions.NoGameException;
import exceptions.NoAuthException;
import exceptions.BadTeamColorException;
import model.AuthData;
import model.GameData;
import service.GameService;
import service.UserService;

import java.util.Collection;

public class GameServiceTest {
    private GameService gameService;
    private UserService userService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        this.gameDAO = new GameDAO(memoryDatabase);
        this.authDAO = new AuthDAO(memoryDatabase);
        this.userDAO = new UserDAO(memoryDatabase);
        this.gameService = new GameService(gameDAO, authDAO);
        this.userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    public void cleanUp() {
        this.gameDAO.clear();
        this.authDAO.clear();
    }

    @Test
    public void testCreateGameSuccess() throws NoGameException, UsernameTakenException, DataAccessException, NoAuthException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();

        String gameName = "TestGame";
        int gameId = gameService.createGame(authToken, gameName);

        assertTrue(gameId > 0);
    }

    @Test
    public void testCreateGameNoAuth() throws UsernameTakenException, DataAccessException, NoAuthException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        this.authDAO.deleteAuth(authToken);

        String gameName = "TestGame";
        assertThrows(NoAuthException.class, () -> {
            gameService.createGame(authToken, gameName);
        });

    }

    @Test
    public void testListGamesSuccess() throws NoAuthException, UsernameTakenException, DataAccessException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();

        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));

        assertThrows(NoAuthException.class, () -> {
            gameService.listGames(authToken);
        });
    }

//    @Test
//    public void testJoinGame_Success() throws NoGameException, NoAuthException, BadTeamColorException {
//        // Arrange
//        String authToken = "authToken";
//        int gameId = 123;
//        String clientColor = "white";
//        AuthData authData = new AuthData(authToken, "username");
//        GameData gameData = new GameData(gameId, null, null, "TestGame", null);
//
//        when(authDAO.getAuth(authToken)).thenReturn(authData);
//        when(gameDAO.getGame(gameId)).thenReturn(gameData);
//        when(gameDAO.userExists(authData.getUsername(), gameId, clientColor)).thenReturn(false);
//
//        // Act
//        gameService.joinGame(authToken, gameId, clientColor);
//
//        // Assert: No exception should be thrown
//    }
}