package database;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import java.sql.SQLException;

import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.Map;

// Implementation for MySQL Database
public class MySQLDatabase {

    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(256) PRIMARY KEY NOT NULL UNIQUE,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS auth (
                username VARCHAR(256) NOT NULL,
                authtoken VARCHAR(256) PRIMARY KEY NOT NULL UNIQUE,
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS games (
              gameid INT PRIMARY KEY NOT NULL UNIQUE,
              whiteusername VARCHAR(256),
              blackusername VARCHAR(256),
              gamename VARCHAR(256) NOT NULL,
              chessgame JSON NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public MySQLDatabase() throws DataAccessException, SQLException, ResponseException {
        DatabaseManager.createDatabase();
        try (var connection = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Database Error");
        }

    }

    public void createUser(UserData userData) throws DataAccessException, SQLException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM users WHERE username = ?";
        try (var connection = DatabaseManager.getConnection()) {

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }

        return null;
    }

    public void createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (gameid, whiteusername, blackusername, gamename, chessgame) VALUES (?, ?, ?, ?, ?)";

        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {

                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());
                preparedStatement.setString(5, new Gson().toJson(game.game()));

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteusername, blackusername, gamename, chessgame FROM games WHERE gameid = ?";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        ChessGame game = new Gson().fromJson(resultSet.getString("chessgame"), ChessGame.class);
                        return new GameData(resultSet.getInt("gameid"), resultSet.getString("whiteusername"), resultSet.getString("blackusername"), resultSet.getString("gamename"), game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }

        return null;
    }

    public Collection<GameData> listGames() {
        return null;
    }

    public void updateGame(String username, int gameID, String clientColor) {
    }

    public static AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    public static AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT username, authtoken FROM auth WHERE authtoken = ?";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new AuthData(resultSet.getString("username"), resultSet.getString("authtoken"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
        return null;
    }

        public static void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authtoken = ?";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            ;throw new DataAccessException("Database Error");
        }

    }

    public boolean userExistsInGame(String username, int gameID, String clientColor) {
        return true;
    }

    public void clearGames() {
    }
    public void clearUsers() {
    }
    public static void clearAuths() {
    }

    public void clear() throws DataAccessException {
        clearAuths();
        clearUsers();
        clearGames();
    }

    public Map<Integer, GameData> getAllGames() {
        return null;
    }

    public Map<String, UserData> getAllUsers() {
        return null;
    }

    public static Map<String, AuthData> getAllAuths() {
        return null;
    }
}