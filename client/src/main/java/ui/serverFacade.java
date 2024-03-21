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


    public serverFacade(String serverUrl, int port) {
        this.serverUrl = serverUrl;
        this.port = port;
    }

    public AuthData register(String username, String password, String email) {
        return null;
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