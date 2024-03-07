import dataAccess.DataAccessException;
import dataAccess.database.MySQLDatabase;
import exceptions.ResponseException;
import model.UserData;
import server.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws DataAccessException {
//        MySQLDatabase mySQLDatabase = new MySQLDatabase();
//        mySQLDatabase.createUser(new UserData("username", "password", "email"));

        Server server = new Server();
        var port = server.run(0);
        System.out.println("Started HTTP server on port: " + port);
    }
}