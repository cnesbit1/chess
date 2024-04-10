package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.*;

import exceptions.NoAuthException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

// Implementation for MySQL Database
public class MySQLDatabase implements DataAccess {

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
                authtoken VARCHAR(256) PRIMARY KEY NOT NULL UNIQUE,
                username VARCHAR(256) NOT NULL
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

    public MySQLDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var connection = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error" + e);
        }

    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DataAccessException("User creation failed: No rows affected");
                }
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

    // Methods for games
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

    public Collection<GameData> listGames() throws DataAccessException {
        HashMap<Integer, GameData> listOfGames = new HashMap<>();
        var statement = "SELECT gameid, whiteusername, blackusername, gamename, chessgame FROM games";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int gameid = resultSet.getInt("gameid");
                        String whiteusername = resultSet.getString("whiteusername");
                        String blackusername = resultSet.getString("blackusername");
                        String gamename = resultSet.getString("gamename");
                        ChessGame game = new Gson().fromJson(resultSet.getString("chessgame"), ChessGame.class);
                        listOfGames.put(gameid, new GameData(gameid, whiteusername, blackusername, gamename, game));
                    }
                }
            }
            return listOfGames.values();
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    // Methods for auths
    public AuthData createAuth(String username) throws DataAccessException {
        var statement = "INSERT INTO auth (authtoken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);

        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
            return auth;
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException, NoAuthException {
        var statement = "SELECT authtoken, username FROM auth WHERE authtoken = ?";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new AuthData(resultSet.getString("authtoken"), resultSet.getString("username"));
                    }
                    else {
                        throw new NoAuthException();
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
        catch (NoAuthException e) {
            throw new NoAuthException();
        }
    }

        public void deleteAuth(String authToken) throws DataAccessException, NoAuthException {
        var statement = "DELETE FROM auth WHERE authtoken = ?";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new NoAuthException();
                }
            }
        }
        catch (SQLException e) {
            ;throw new DataAccessException("Database Error");
        }
        catch (NoAuthException e) {
            throw new NoAuthException();
        }
    }

    // Additional helper and testing methods
    public boolean userExistsInGame(String username, int gameID, String clientColor) throws DataAccessException {
        GameData gameData = this.getGame(gameID);
        if (gameData == null) {
            return false;
        }
        if ("white".equalsIgnoreCase(clientColor)) {
            return gameData.whiteUsername() != null;
        }
        if ("black".equalsIgnoreCase(clientColor)) {
            return gameData.blackUsername() != null;
        }
        return true;
    }

    public void updateGame(String username, int gameID, String clientColor) throws DataAccessException {
        String column;
        String existingUsername;

        // Determine the column to update based on the clientColor
        if ("white".equalsIgnoreCase(clientColor)) {
            column = "whiteusername";
        } else {
            column = "blackusername";
        }

        // Query to check the existing username in whiteusername column
        String selectStatement = "SELECT whiteusername FROM games WHERE gameid = ?";

        // Update statement
        String updateStatement = String.format("UPDATE games SET %s = ? WHERE gameid = ?", column);

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement selectPreparedStatement = connection.prepareStatement(selectStatement)) {
                selectPreparedStatement.setInt(1, gameID);
                ResultSet resultSet = selectPreparedStatement.executeQuery();
                if (resultSet.next()) {
                    existingUsername = resultSet.getString("whiteusername");
                    // Check if there's an existing username in whiteusername column
                    if ("whiteusername".equals(column) && existingUsername != null) {
                        // Do not perform update if there's an existing username
                        return;
                    }
                }
            }

            // Proceed with the update
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public void updateFullGame(GameData gameData) throws DataAccessException {
        String updateStatement = "UPDATE games SET whiteusername = ?, blackusername = ?, gamename = ?, chessgame = ? WHERE gameid = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
            preparedStatement.setString(1, gameData.whiteUsername());
            preparedStatement.setString(2, gameData.blackUsername());
            preparedStatement.setString(3, gameData.gameName());
            preparedStatement.setString(4, new Gson().toJson(gameData.game()));
            preparedStatement.setInt(5, gameData.gameID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public Map<Integer, GameData> getAllGames() throws DataAccessException {
        HashMap<Integer, GameData> listOfGames = new HashMap<>();
        var statement = "SELECT gameid, whiteusername, blackusername, gamename, chessgame FROM games";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int gameid = resultSet.getInt("gameid");
                        String whiteusername = resultSet.getString("whiteusername");
                        String blackusername = resultSet.getString("blackusername");
                        String gamename = resultSet.getString("gamename");
                        ChessGame game = new Gson().fromJson(resultSet.getString("chessgame"), ChessGame.class);
                        listOfGames.put(gameid, new GameData(gameid, whiteusername, blackusername, gamename, game));
                    }
                }
            }
            return listOfGames;
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public Map<String, UserData> getAllUsers() throws DataAccessException {
        HashMap<String, UserData> listOfUsers = new HashMap<>();
        var statement = "SELECT username, password, email FROM users";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");
                        listOfUsers.put(username, new UserData(username, password, email));
                    }
                }
            }
            return listOfUsers;
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public Map<String, AuthData> getAllAuths() throws DataAccessException {
        HashMap<String, AuthData> listofAuths = new HashMap<>();
        var statement = "SELECT username, authtoken FROM auth";
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String authToken = resultSet.getString("authtoken");
                        listofAuths.put(authToken, new AuthData(authToken, username));
                    }
                }
            }
            return listofAuths;
        }
        catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    // Methods to clear database
    public void clear() throws DataAccessException {
        clearAuths();
        clearUsers();
        clearGames();
    }
    public void clearGames() throws DataAccessException {
        clearTable("games", 2);
    }
    public void clearUsers() throws DataAccessException {
        clearTable("users", 0);
    }
    public void clearAuths() throws DataAccessException {
        clearTable("auth", 1);
    }

    private void clearTable(String table, int index) throws DataAccessException {
        var statement = String.format("DROP TABLE IF EXISTS %s",table);
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = connection.prepareStatement(createStatements[index])) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            ;throw new DataAccessException("Database Error" + e);
        }
    }
}