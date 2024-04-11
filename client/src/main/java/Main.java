// import chess.*;
// import server.Server;
import ui.SwitchUILoop;

public class Main {
    public static void main(String[] args) {
//        Server server = new Server();
//        var port = server.run(0);
        System.out.println("Started HTTP server on port: " + 55879);
        SwitchUILoop programLoop = new SwitchUILoop(55879);
        programLoop.run();
        // End of Program
    }
}