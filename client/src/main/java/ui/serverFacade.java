package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;
import responses.JoinRequestData;
import responses.ListGames;


public class serverFacade {
    public connectionHTTP conn;
    public serverFacade(connectionHTTP conn) {
        this.conn = conn;
    }
    public AuthData register(String username, String password, String email) throws Exception {
        String requestData = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\" }";
        String response = conn.sendPostRequest("/user", requestData, null);
        Gson gson = new Gson();
        return gson.fromJson(response, AuthData.class);
    }
    public AuthData login(String username, String password) throws Exception {
        String requestData = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        String response = conn.sendPostRequest("/session", requestData, null);
        Gson gson = new Gson();
        return gson.fromJson(response, AuthData.class);
    }
    public void logout(String authToken) throws Exception {
        conn.sendDeleteRequest("/session", authToken);
    }
    public ListGames listGames(String authToken) throws Exception {
        String response = conn.sendGetRequest("/game", authToken);
        return new Gson().fromJson(response, ListGames.class);
    }
    public int createGame(String authToken, String gameName) throws Exception {
        GameData gameData = new GameData(null, null, null, gameName, null);
        String requestData = new Gson().toJson(gameData);
        String response = conn.sendPostRequest("/game", requestData, authToken);

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        return jsonResponse.get("gameID").getAsInt();
    }
    public void joinGame(String authToken, int gameID, String playerColor) throws Exception {
        JoinRequestData requestData = new JoinRequestData(playerColor, gameID);
        String data = new Gson().toJson(requestData);
        conn.sendPutRequest("/game", data, authToken);
    }
}