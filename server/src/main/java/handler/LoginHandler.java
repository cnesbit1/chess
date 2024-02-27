package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exceptions.NoUserException;
import exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;
import responses.ErrorResponse;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;
import exceptions.WrongPasswordException;

public class LoginHandler {
    public static Object handle(Request req, Response res, UserService userService) throws ResponseException, DataAccessException {
        try {
            Gson gson = new Gson();
            UserData userData = gson.fromJson(req.body(), UserData.class);
            String username = userData.username();
            String password = userData.password();
            if (username == null || password == null ) {
                throw new IllegalArgumentException();
            }
            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException();
            }
            AuthData authData = userService.login(username, password);
            res.status(200);
            return gson.toJson(authData);
        } catch (WrongPasswordException | NoUserException e) {
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
