import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import database.MemoryDatabase;
import database.MySQLDatabase;
import model.GameData;
import model.UserData;
import org.junit.Before;
import org.junit.Test;

import exceptions.ResponseException;
import service.ClearService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private GameDAO gameDAO;

    private UserDAO userDAO;

    private AuthDAO authDAO;

    @Before
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.gameDAO = new GameDAO(mySQLDatabase);
        this.userDAO = new UserDAO(mySQLDatabase);
        this.authDAO = new AuthDAO(mySQLDatabase);
        this.clearService = new ClearService(authDAO, userDAO, gameDAO);
    }

    private void populateTestData() throws DataAccessException, SQLException {
        userDAO.createUser(new UserData("username1", "password1", "email@aol.com"));
        userDAO.createUser(new UserData("username2", "password2", "email2@aol.com"));
        userDAO.createUser(new UserData("username3", "password3", "email3@aol.com"));
        userDAO.createUser(new UserData("username4", "password4", "email4@aol.com"));
        userDAO.createUser(new UserData("username5", "password5", "email5@aol.com"));
        userDAO.createUser(new UserData("username6", "password6", "email6@aol.com"));

        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));

        authDAO.createAuth("username1");
        authDAO.createAuth("username1");
        authDAO.createAuth("username3");
        authDAO.createAuth("username4");
        authDAO.createAuth("username5");
        authDAO.createAuth("username6");
    }

    @Test
    public void testClearApplication() throws ResponseException {
        try {
            populateTestData();
            clearService.clearApplication();

            assertEquals(0, gameDAO.getAllGames().size());
            assertEquals(0, userDAO.getAllUsers().size());
            assertEquals(0, authDAO.getAllAuths().size());

        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
