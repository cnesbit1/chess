package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import exceptions.NoAuthException;
import exceptions.UsernameTakenException;
import exceptions.WrongPasswordException;
import exceptions.NoUserException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {

    private final UserDAO userAccess;
    private final AuthDAO authAccess;



    public UserService(UserDAO userAccess, AuthDAO authAccess) {
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException, UsernameTakenException {
        if (this.userAccess.getUser(username) == null) {
            UserData userData = new UserData(username, password, email);
            userAccess.createUser(userData);
            return authAccess.createAuth(username);
        }
        else {
            throw new UsernameTakenException();
        }
    }

    public AuthData login(String username, String password) throws DataAccessException, WrongPasswordException, NoUserException {
        UserData userData = this.userAccess.getUser(username);
        if (userData != null) {
            if (Objects.equals(userData.password(), password)) {
                return authAccess.createAuth(username);
            }
            throw new WrongPasswordException();
        }
        else {
            throw new NoUserException();
        }
    }

    public void logout(String authToken) throws NoAuthException {
        this.authAccess.deleteAuth(authToken);
    }
}
