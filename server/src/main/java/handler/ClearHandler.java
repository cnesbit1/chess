package handler;

import com.google.gson.Gson;
import responses.ResponseMessage;
import exceptions.ResponseException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public static Object handle(Request req, Response res, ClearService clearService) throws ResponseException {
        clearService.clearApplication();
        res.status(200);
        return new Gson().toJson(new ResponseMessage("Success: Database cleared."));
    }
}
