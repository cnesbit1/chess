package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.NoAuthException;
import responses.ErrorResponse;
import responses.GameID;
import exceptions.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;
import model.GameData;
public class CreateGameHandler {
    public static Object handle(Request req, Response res, GameService gameService) throws ResponseException, DataAccessException {
        try {
            Gson gson = new Gson();
            String authToken = req.headers("authorization");
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            int gameID = gameService.createGame(authToken, gameData.gameName());
            res.status(200);
//            return "{ gameID: " + gameID + " }";
            return gson.toJson(new GameID(gameID));
        }
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
