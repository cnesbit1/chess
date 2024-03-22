package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import responses.GameID;
import responses.JoinRequestData;
import responses.ListGames;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Collection;

public class serverFacade {

    private final String serverUrl;
    private final int port;

    public connectionHTTP conn;


    public serverFacade(int port, connectionHTTP conn) {
        this.serverUrl = conn.baseURL;
        this.port = port;
        this.conn = conn;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        // Prepare request data
        String requestData = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\" }";

        // Send POST request
        String response = conn.sendPostRequest("/user", requestData, null);

        Gson gson = new Gson();
        return gson.fromJson(response, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        // Prepare request data
        String requestData = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        // Send POST request
        String response = conn.sendPostRequest("/session", requestData, null);

        Gson gson = new Gson();
        return gson.fromJson(response, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        // Prepare request data
        String requestData = authToken;

        // Send DELETE request
        String response = conn.sendDeleteRequest("/session", requestData);
    }

    public ListGames listGames(String authToken) throws Exception {
        String requestData = authToken;

        String response = conn.sendGetRequest("/game", requestData);

        // Deserialize the response JSON string into a Collection<GameData>
        ListGames games = new Gson().fromJson(response, ListGames.class);
        return games;
    }

    public int createGame(String authToken, String gameName) throws Exception {
        // Prepare request data
        GameData gameData = new GameData(null, null, null, gameName, null);
        String requestData = new Gson().toJson(gameData);

        // Send POST request
        String response = conn.sendPostRequest("/game", requestData, authToken);

        // Extract the gameID from the response
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        int gameID = jsonResponse.get("gameID").getAsInt();

        return gameID;
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws Exception {
        // Prepare request data
        JoinRequestData requestData = new JoinRequestData(playerColor, gameID);
        String data = new Gson().toJson(requestData);

        // Send POST request
        conn.sendPutRequest("/game", data, authToken);
    }

}