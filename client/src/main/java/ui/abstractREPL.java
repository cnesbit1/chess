package ui;

import com.google.gson.Gson;

import java.util.Scanner;

public abstract class abstractREPL {
    public static final Scanner scanner = new Scanner(System.in);
    public static final Gson gson = new Gson();

    public switchUILoop programLoop;

    public serverFacade serverWrapper;

    public boolean switchUI = false;

    public abstractREPL(switchUILoop programLoop,serverFacade serverWrapper) {
        this.programLoop = programLoop;
        this.serverWrapper = serverWrapper;
    }

    public void run() {
        while (true) {
            showUIPrompt();
            System.out.println();
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                programLoop.quitProgram();
                break;
            }

            if (input.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }

            processInput(input);

            if (switchUI) break;
        }
    }

    public abstract void processInput(String input);

    public abstract void printHelp();

    public abstract void showUIPrompt();

    public void changeUI() { switchUI = true; }

}
