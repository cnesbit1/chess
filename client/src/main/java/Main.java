import chess.*;
import server.Server;
import ui.switchUILoop;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);
        System.out.println("Started HTTP server on port: " + port);
        switchUILoop programLoop = new switchUILoop(port);
        programLoop.run();
        // End of Program
    }
}