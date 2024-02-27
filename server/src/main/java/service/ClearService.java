package service;
import dataAccess.*;
import exceptions.ResponseException;

public class ClearService {

    private final AuthDAO authAccess;

    private final UserDAO userAccess;

    private final GameDAO gameAccess;

    public ClearService(AuthDAO authAccess, UserDAO userAccess, GameDAO gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }
    public void clearApplication() throws ResponseException {
        this.authAccess.clear();
        this.userAccess.clear();
        this.gameAccess.clear();
    }
}
