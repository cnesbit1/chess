package ui;

public class SwitchUILoop {
    public enum States {
        SIGNEDOUT,

        SIGNEDIN,

        INGAME,

        QUIT
    }

    States currentState;
    public SignedInUI loggedInUI;

    public SignedOutUI loggedOutUI;

    public InGameUI gameplayUI;

    public SwitchUILoop(int port) {
        currentState = States.SIGNEDOUT;
        ConnectionHTTP conn = new ConnectionHTTP(null, "localhost", port);
        ConnectionWebSocket webConn = new ConnectionWebSocket(null, "localhost", port, null, null);
        ServerFacade serverWrapper = new ServerFacade(conn, webConn);
        loggedInUI = new SignedInUI(this, serverWrapper);
        loggedOutUI = new SignedOutUI(this, serverWrapper);
        gameplayUI = new InGameUI(this, serverWrapper);
        webConn.setNotificationGameplayHandler(gameplayUI);
        webConn.setNotificationJoinGameHandler(loggedInUI);
    }

    public void run() {
        while (true) {
            boolean shouldQuit = false;
            switch (currentState) {
                case SIGNEDOUT:
                    loggedOutUI.run();
                    if (currentState == States.QUIT) {
                        shouldQuit = true;
                    }
                    break;
                case SIGNEDIN:
                    loggedInUI.run();
                    if (currentState == States.QUIT) {
                        currentState = States.SIGNEDIN;
                    }
                    break;
                case INGAME:
                    gameplayUI.run();
                    if (currentState == States.QUIT) {
                        currentState = States.INGAME;
                    }
                    break;
            }
            if (shouldQuit) { break; }
        }
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void quitProgram() { currentState = States.QUIT; }

    public void switchToSignedIn() {
        currentState = States.SIGNEDIN;
    }

    public void switchToInGame() {
        currentState = States.INGAME;
    }

    public void switchToSignedOut() {
        currentState = States.SIGNEDOUT;
    }
}
