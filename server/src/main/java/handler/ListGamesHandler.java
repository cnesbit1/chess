package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.NoAuthException;
import exceptions.NoGameException;
import model.GameData;
import responses.ErrorResponse;
import responses.ListGames;
import server.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler {
    public static Object handle(Request req, Response res, GameService gameService) throws ResponseException, DataAccessException {
        try {
            Gson gson = new Gson();
            String authToken = req.headers("authorization");

            Collection<GameData> games = gameService.listGames(authToken);
            res.status(200);
            return gson.toJson(new ListGames(games));        }
        catch (NoAuthException e) {
            // Handle other errors with a generic description
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: no matching game name"));
        }
        catch (Exception e) {
            // Handle other errors with a generic description
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: description"));
        }
    }
}
