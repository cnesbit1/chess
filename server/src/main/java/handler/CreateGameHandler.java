package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.NoGameException;
import exceptions.NoUserException;
import exceptions.WrongPasswordException;
import model.AuthData;
import model.UserData;
import responses.ErrorResponse;
import responses.GameID;
import responses.ResponseMessage;
import server.ResponseException;
import service.GameService;
import service.UserService;
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
            System.out.println(gameID);
            return "{ gameID: " + gameID + " }";
//            return gson.toJson(new GameID(gameID));
        }
        catch (NoGameException e) {
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
