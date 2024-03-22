package clientTests;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import model.AuthData;
import responses.ListGames;
import ui.connectionHTTP;
import ui.serverFacade;

class ServerFacadeTest {
    private Server server;
    private serverFacade serverWrapper;
    private connectionHTTP conn;

    @BeforeEach
    public void init() throws Exception {
        this.server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        conn = new connectionHTTP(null, "localhost", port);
        this.serverWrapper = new serverFacade(port, conn);
        this.serverWrapper.conn.sendDeleteRequest("/db", null);
    }

    @AfterEach
    public void stopServer() throws Exception {
        this.server.stop();
    }

    @Test
    public void testRegisterPositive() throws Exception {
        AuthData authData = this.serverWrapper.register("testuser5", "testpassword5", "test@example.com5");
        assertNotNull(authData);
    }

    @Test
    public void testRegisterNegative() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        assertThrows(Exception.class, () -> serverWrapper.register("testuser", "testpassword", "test@example.com"));
    }

    @Test
    public void testLoginPositive() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        assertNotNull(authData2);
    }

    @Test
    public void testLoginPositive2() throws Exception {
        assertThrows(Exception.class, () -> serverWrapper.login("testuser", "testpassword"));
    }

    @Test
    public void testLogoutPositive() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login("testuser", "testpassword");
        assertDoesNotThrow(() -> serverWrapper.logout(authData2.authToken()));
    }
    @Test
    public void testLogoutFailure() throws Exception {
        assertThrows(Exception.class, () -> serverWrapper.logout(" "));
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        int gameID = serverWrapper.createGame(authData2.authToken(), "testgamename");
        assertNotNull(gameID);
    }

    @Test
    public void testCreateGameFailure() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        assertThrows(Exception.class, () -> serverWrapper.createGame(null, "testgamename"));
    }

    @Test
    public void testListGamesSuccess() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        serverWrapper.createGame(authData2.authToken(), "testgamename");
        serverWrapper.createGame(authData2.authToken(), "testgamename2");
        serverWrapper.createGame(authData2.authToken(), "testgamename3");
        var games = serverWrapper.listGames(authData2.authToken());
        assertNotNull(games);
    }
    @Test
    public void testListGamesFailure() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        serverWrapper.createGame(authData2.authToken(), "testgamename");
        serverWrapper.createGame(authData2.authToken(), "testgamename2");
        serverWrapper.createGame(authData2.authToken(), "testgamename3");
        assertThrows(Exception.class, () -> serverWrapper.listGames(null));
    }

    @Test
    public void testJoinGameSuccess() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        int gameID = serverWrapper.createGame(authData2.authToken(), "testgamename");
        assertDoesNotThrow(() -> serverWrapper.joinGame(authData2.authToken(), gameID, "white"));
    }

    @Test
    public void testJoinGameFailure() throws Exception {
        AuthData authData = serverWrapper.register("testuser", "testpassword", "test@example.com");
        AuthData authData2 = serverWrapper.login(authData.username(), "testpassword");
        serverWrapper.createGame(authData2.authToken(), "testgamename");
        serverWrapper.createGame(authData2.authToken(), "testgamename2");
        serverWrapper.createGame(authData2.authToken(), "testgamename3");
        assertThrows(Exception.class, () -> serverWrapper.joinGame(null, 1, "white"));
    }
}
