package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;
import exceptions.UsernameTakenException;
import responses.ResponseMessage;
import responses.ErrorResponse;

public class RegisterHandler {
    public static Object handle(Request req, Response res, UserService userService) throws ResponseException, DataAccessException {
        try {
            Gson gson = new Gson();
            UserData userData = gson.fromJson(req.body(), UserData.class);
            String username = userData.username();
            String password = userData.password();
            String email = userData.email();
            if (username == null || password == null || email == null) { throw new IllegalArgumentException(); }
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) { throw new IllegalArgumentException(); }
            AuthData authData = userService.register(username, password, email);
            res.status(200);
            return gson.toJson(authData);
        }
        catch (IllegalArgumentException e) {
            // Handle bad request (e.g., missing or invalid fields)
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: invalid fields"));
        } catch (UsernameTakenException e) {
            // Handle case where username is already taken
            res.status(403);
            return new Gson().toJson(new ErrorResponse("Error: already taken"));
        } catch (Exception e) {
            // Handle other errors with a generic description
            res.status(500);
            return new Gson().toJson(new ErrorResponse("Error: description"));
        }
    }
}
