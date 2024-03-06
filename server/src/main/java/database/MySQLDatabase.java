package database;

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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public MySQLDatabase() throws DataAccessException, SQLException, ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }

    }

    public void createUser(UserData userData) throws DataAccessException, SQLException {
        try (var connection = DatabaseManager.getConnection()) {

        }
        catch (SQLException | DataAccessException e) {
            System.out.println(e);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void createGame(GameData game) throws DataAccessException {

    }

    public GameData getGame(int gameID) throws DataAccessException {
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
        return null;
    }

    public static void deleteAuth(String authToken) throws DataAccessException {

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