package ui;

import com.google.gson.Gson;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class serverFacade {

    private final String serverUrl;
    private final int port;

    private connectionHTTP conn;


    public serverFacade(int port, connectionHTTP conn) {
        this.serverUrl = conn.baseURL;
        this.port = port;
        this.conn = conn;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        // Prepare request data
        String requestData = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\" }";

        // Send POST request
        String response = conn.sendPostRequest("/user", requestData);

        // Process response
        System.out.println("Server Response: " + response);

        Gson gson = new Gson();
        return gson.fromJson(response, AuthData.class);
    }

    public AuthData login(String username, String password) {
        return null;
    }

    public void logout(String authToken) {
    }

    public Collection<GameData> listGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName) {
        return 0;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {
    }

}