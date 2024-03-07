import dataAccess.DataAccessException;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
Server server = new Server();
        var port = server.run(0);
        System.out.println("Started HTTP server on port: " + port);
    }
}