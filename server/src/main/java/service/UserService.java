package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;

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

//    public AuthData login() {}

//
//    public void getAuth() {
//        authAccess.getAuth();
//    }
//
//    public void deleteAuth() {
//        authAccess.deleteAuth();
//    }
}
