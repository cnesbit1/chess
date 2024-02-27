package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.BadTeamColorException;
import exceptions.NoAuthException;
import exceptions.NoGameException;
import responses.ErrorResponse;
import responses.JoinRequestData;
import responses.ResponseMessage;
import exceptions.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    public static Object handle(Request req, Response res, GameService gameService) throws ResponseException, DataAccessException {
        try {
            Gson gson = new Gson();
            String authToken = req.headers("authorization");
            JoinRequestData requestData = gson.fromJson(req.body(), JoinRequestData.class);

            gameService.joinGame(authToken, requestData.getGameID(), requestData.getPlayerColor());
            res.status(200);
            return new Gson().toJson(new ResponseMessage("Successful Logout"));
        }
        catch (NoAuthException e) {
            // Handle other errors with a generic description
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: no matching game name"));
        }
        catch (NoGameException e) {
            // Handle other errors with a generic description
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: no matching game name"));
        }
        catch (BadTeamColorException e) {
            // Handle other errors with a generic description
            res.status(403);
            return new Gson().toJson(new ErrorResponse("Error: bad team color"));
        }
        catch (Exception e) {
            // Handle other errors with a generic description
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: description"));
        }
    }
}
