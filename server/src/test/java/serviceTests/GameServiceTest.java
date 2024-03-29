package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.MySQLDatabase;
import exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import model.AuthData;
import model.GameData;
import service.GameService;
import service.UserService;

import java.sql.SQLException;
import java.util.Collection;

public class GameServiceTest {
    private GameService gameService;
    private UserService userService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.gameDAO = new GameDAO(mySQLDatabase);
        this.authDAO = new AuthDAO(mySQLDatabase);
        this.userDAO = new UserDAO(mySQLDatabase);
        this.gameService = new GameService(gameDAO, authDAO);
        this.userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        this.gameDAO.clear();
        this.authDAO.clear();
        this.userDAO.clear();
    }

    @Test
    public void testCreateGameSuccess() throws UsernameTakenException, DataAccessException, NoAuthException, SQLException {
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
    public void testCreateGameNoAuth() throws UsernameTakenException, DataAccessException, SQLException, NoAuthException {
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
    public void testListGamesSuccess() throws NoAuthException, UsernameTakenException, DataAccessException, SQLException {
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

        Collection<GameData> games = gameService.listGames(authToken);
        assertNotNull(games);
    }

    @Test
    public void testListGamesNoAuth() throws UsernameTakenException, DataAccessException, SQLException, NoAuthException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        this.authDAO.deleteAuth(authToken);

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
    @Test
    public void testJoinGameSuccess() throws NoGameException, NoAuthException, BadTeamColorException, DataAccessException, UsernameTakenException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();

        gameDAO.createGame(new GameData(123, "firstWhiteName", null, "game1", new ChessGame()));
        gameService.joinGame(authToken, 123, "black");

        assertEquals("testUser", gameDAO.getGame(123).blackUsername());
    }

    @Test
    public void testJoinGameNoAuth() throws DataAccessException, UsernameTakenException, SQLException, NoAuthException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        authDAO.deleteAuth(authToken);

        gameDAO.createGame(new GameData(123, "firstWhiteName", null, "game1", new ChessGame()));
        assertThrows(NoAuthException.class, () -> {
            gameService.joinGame(authToken, 123, "black");
        });
    }

    @Test
    public void testJoinGameNoGame() throws DataAccessException, UsernameTakenException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();

        assertThrows(NoGameException.class, () -> {
            gameService.joinGame(authToken, 123, "black");
        });
    }

    @Test
    public void testJoinGameBadTeamColor() throws DataAccessException, UsernameTakenException, SQLException {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        AuthData authData = this.userService.register(username, password, email);
        String authToken = authData.authToken();
        gameDAO.createGame(new GameData(123, "firstWhiteName", null, "game1", new ChessGame()));


        assertThrows(BadTeamColorException.class, () -> {
            gameService.joinGame(authToken, 123, "white");
        });
    }
}