package server;

import database.MemoryDatabase;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;

import database.MySQLDatabase;
import exceptions.NoAuthException;
import exceptions.ResponseException;
import handler.LogoutHandler;
import handler.LoginHandler;
import handler.ListGamesHandler;
import handler.CreateGameHandler;
import handler.JoinGameHandler;
import handler.RegisterHandler;
import handler.ClearHandler;

import service.ClearService;
import service.UserService;
import service.GameService;

import spark.*;

public class Server {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    private database.MySQLDatabase mySQLDatabase;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        this.mySQLDatabase = new MySQLDatabase();
        this.userDAO = new UserDAO(this.mySQLDatabase);
        this.authDAO = new AuthDAO(this.mySQLDatabase);
        this.gameDAO = new GameDAO(this.mySQLDatabase);

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerEndpoints() {
        // Define your endpoints here
        Spark.delete("/db", this::clearApplication);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearApplication(Request req, Response res) throws ResponseException {
        return ClearHandler.handle(req, res, new ClearService(this.authDAO, this.userDAO, this.gameDAO));
    }

    private Object registerUser(Request req, Response res) throws ResponseException, DataAccessException {
        return RegisterHandler.handle(req, res, new UserService(this.userDAO, this.authDAO));
    }

    private Object login(Request req, Response res) throws ResponseException, DataAccessException {
        return LoginHandler.handle(req, res, new UserService(this.userDAO, this.authDAO));
    }

    private Object logout(Request req, Response res) throws ResponseException, DataAccessException, NoAuthException {
        return LogoutHandler.handle(req, res, new UserService(this.userDAO, this.authDAO));
    }

    private Object listGames(Request req, Response res) throws ResponseException, DataAccessException {
        return ListGamesHandler.handle(req, res, new GameService(this.gameDAO, this.authDAO));
    }

    private Object createGame(Request req, Response res) throws ResponseException, DataAccessException {
        return CreateGameHandler.handle(req, res, new GameService(this.gameDAO, this.authDAO));
    }

    private Object joinGame(Request req, Response res) throws ResponseException, DataAccessException {
        return JoinGameHandler.handle(req, res, new GameService(this.gameDAO, this.authDAO));
    }
}
