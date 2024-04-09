package ui;

public class switchUILoop {
    public enum states {
        SIGNEDOUT,

        SIGNEDIN,

        INGAME,

        QUIT
    }

    states currentState;
    public signedInUI loggedInUI;

    public signedOutUI loggedOutUI;

    public inGameUI gameplayUI;

    public switchUILoop(int port) {
        currentState = states.SIGNEDOUT;
        connectionHTTP conn = new connectionHTTP(null, "localhost", port);
        connectionWebSocket webConn = new connectionWebSocket(null, "localhost", port);
        serverFacade serverWrapper = new serverFacade(conn, webConn);
        loggedInUI = new signedInUI(this, serverWrapper);
        loggedOutUI = new signedOutUI(this, serverWrapper);
        gameplayUI = new inGameUI(this, serverWrapper);
    }

    public void run() {
        while (true) {
            boolean shouldQuit = false;
            switch (currentState) {
                case SIGNEDOUT:
                    loggedOutUI.run();
                    if (currentState == states.QUIT) {
                        shouldQuit = true;
                    }
                    break;
                case SIGNEDIN:
                    loggedInUI.run();
                    if (currentState == states.QUIT) {
                        currentState = states.SIGNEDIN;
                    }
                    break;
                case INGAME:
                    gameplayUI.run();
                    if (currentState == states.QUIT) {
                        currentState = states.INGAME;
                    }
                    break;
            }
            if (shouldQuit) { break; }
        }
        System.out.println("Exiting...");
    }

    public void quitProgram() { currentState = states.QUIT; }

    public void switchToSignedIn() {
        currentState = states.SIGNEDIN;
    }

    public void switchToInGame() {
        currentState = states.INGAME;
    }

    public void switchToSignedOut() {
        currentState = states.SIGNEDOUT;
    }
}
