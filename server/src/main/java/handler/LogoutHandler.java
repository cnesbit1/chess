package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.NoAuthException;
import exceptions.NoUserException;
import exceptions.WrongPasswordException;
import model.AuthData;
import model.UserData;
import responses.ErrorResponse;
import responses.ResponseMessage;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;
import responses.ResponseMessage;

public class LogoutHandler {
    public static Object handle(Request req, Response res, UserService userService) throws ResponseException, DataAccessException, NoAuthException {
        try {
            Gson gson = new Gson();
            var out = req.headers("Authorization");
            if (out == null) { throw new NoAuthException(); }
            userService.logout(out);
            res.status(200);
            return new Gson().toJson(new ResponseMessage("Successful Logout"));
        } catch (NoAuthException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unathorized"));
        }
        catch (Exception e) {
            // Handle other errors with a generic description
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: description"));
        }
    }
}
