package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.database.MySQLDatabase;
import exceptions.NoAuthException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySQLDatabase mySQLDatabase = new MySQLDatabase();
        this.gameDAO = new GameDAO(mySQLDatabase);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        this.gameDAO.clear();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        GameData gameData = new GameData(123, "whiteusername", "blackusername", "gamename", new ChessGame());
        gameDAO.createGame(gameData);

        GameData finalGameData = gameDAO.getGame(123);

        assertEquals(gameData.gameID(), finalGameData.gameID());
    }

    @Test
    public void createGameFailure() throws DataAccessException {
        GameData gameData = new GameData(123, "whiteusername", "blackusername", "gamename", new ChessGame());
        gameDAO.createGame(gameData);

        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(gameData);
        });
    }

    @Test
    public void getGameSuccess() throws DataAccessException, NoAuthException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "whiteusername", "blackusername", "gamename", game);
        gameDAO.createGame(gameData);

        assertEquals(gameData.gameID(), this.gameDAO.getGame(123).gameID());

    }

    @Test
    public void getGameFailure() throws DataAccessException, NoAuthException {
        assertNull(this.gameDAO.getGame(123));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException, NoAuthException {
        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));


        assertNotNull(this.gameDAO.listGames());
    }

    @Test
    public void listGamesEmpty() throws DataAccessException {
        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));

        this.gameDAO.clear();

        assertTrue(this.gameDAO.listGames().isEmpty());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        GameData gameData = new GameData(123, null, null, "chessname", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.joinGame("username", 123, "white");

        assertEquals(gameDAO.getGame(123).whiteUsername(), "username");
    }

    @Test
    public void joinGameFailure() throws DataAccessException {
        GameData gameData = new GameData(123, "whiteusername", null, "chessname", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.joinGame("username", 123, "white");

        assertEquals(gameDAO.getGame(123).whiteUsername(), "whiteusername");
    }

    @Test
    public void userExistsSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(123, null, "firstBlackName", "game1", new ChessGame()));
        assertTrue(gameDAO.userExists("testusername", 123, "black"));
    }

    @Test
    public void userExistsFailure() throws DataAccessException {
        assertFalse(gameDAO.userExists("testusername", 123, "white"));
    }

    @Test
    public void getAllGamesSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));


        assertNotNull(this.gameDAO.getAllGames());
    }

    @Test
    public void getAllGamesEmpty() throws DataAccessException {
        gameDAO.createGame(new GameData(123, "firstWhiteName", "firstBlackName", "game1", new ChessGame()));
        gameDAO.createGame(new GameData(124, "secondWhiteName", "secondBlackName", "game2", new ChessGame()));
        gameDAO.createGame(new GameData(125, "thirdWhiteName", "thirdBlackName", "game3", new ChessGame()));
        gameDAO.createGame(new GameData(126, "fourthWhiteName", "fourthBlackName", "game4", new ChessGame()));
        gameDAO.createGame(new GameData(127, "fifthWhiteName", "fifthBlackName", "game5", new ChessGame()));
        gameDAO.createGame(new GameData(128, "sixthWhiteName", "sixthBlackName", "game6", new ChessGame()));

        gameDAO.clear();

        assertTrue(this.gameDAO.getAllGames().isEmpty());
    }

    @Test
    public void clear() throws DataAccessException {
        GameData gameData = new GameData(123, "whiteusername", "blackusername", "gamename", new ChessGame());
        gameDAO.createGame(gameData);

        gameDAO.clear();

        GameData finalGameData = gameDAO.getGame(123);
        assertNull(finalGameData);
    }
}
