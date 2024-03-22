package ui;

import java.util.Objects;

public class inGameUI extends abstractREPL {
    public inGameUI(switchUILoop programLoop, serverFacade serverWrapper) { super(programLoop, serverWrapper); }
    @Override
    public void processInput(String input) {
        try {
            if (Objects.equals(input, "quit")) { throw new Exception(); }
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                programLoop.switchToSignedIn();
                changeUI();
            }
            else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("That wasn't a valid choice, try again.");
        }
    }
    @Override
    public void printHelp() {
        System.out.println("Viewing Gameplay:");
        System.out.println("We will show you two orientations for the game board.");
        System.out.println("The board with A1 at the bottom left is white's perspective.");
        System.out.println("THe board with A1 at the top right is black's perspective.");
        System.out.println();
    }
    @Override
    public void showUIPrompt() {
        System.out.println("Give an input of 'help' for more possible instructions.");
        System.out.println("Black's Perspective:");
        displayChessboard(false);

        System.out.println();

        System.out.println("White's Perspective:");
        displayChessboard(true);
    }
    private void displayChessboard(boolean whiteAtBottom) {
        String[][] board = populateStartingChessboard();
        String columns = displayColHeaders(whiteAtBottom);
        System.out.println(columns);
        displayRows(board, whiteAtBottom);
        System.out.println(columns);
    }
    private String displayColHeaders(boolean whiteAtBottom) {
        StringBuilder columnsBuilder = new StringBuilder("    ");

        char colStartLabel = whiteAtBottom ? 'a' : 'h';
        char colEndLabel = whiteAtBottom ? 'h' : 'a';
        int colLabelStep = whiteAtBottom ? 1 : -1;

        for (char col = colStartLabel; col != colEndLabel + colLabelStep; col += colLabelStep) {
            columnsBuilder.append(" ").append(col).append(" ");
        }

        return columnsBuilder.toString();
    }
    private void displayRows(String[][] board, boolean whiteAtBottom) {
        int rowStart = whiteAtBottom ? 7 : 0;
        int rowEnd = whiteAtBottom ? -1 : 8;
        int rowStep = whiteAtBottom ? -1 : 1;

        for (int row = rowStart; row != rowEnd; row += rowStep) {
            String displayRow = "  " + (row + 1) + " ";
            System.out.print(displayRow);
            displayRow(board, row, whiteAtBottom);
            System.out.println(displayRow);
        }
    }
    private void displayRow(String[][] board, int row, boolean whiteAtBottom) {
        int colStart = whiteAtBottom ? 0 : 7;
        int colEnd = whiteAtBottom ? 8 : -1;
        int colStep = whiteAtBottom ? 1 : -1;

        for (int col = colStart; col != colEnd; col += colStep) {
            String piece = board[row][col];
            String textColor;
            if (Character.isUpperCase(piece.charAt(0))) {
                textColor = "\u001B[34m";
            }
            else {
                textColor = "\u001B[31m";
            }
            String backgroundColor = (row + col) % 2 == 0 ? "\u001B[40m" : "\u001B[47m";

            System.out.print(backgroundColor + textColor + " " + piece + " \u001B[0m");
        }
    }
    private String[][] populateStartingChessboard() {
        String[][] board = new String[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = " ";
            }
        }

        board[7][0] = "R";
        board[7][1] = "N";
        board[7][2] = "B";
        board[7][3] = "Q";
        board[7][4] = "K";
        board[7][5] = "B";
        board[7][6] = "N";
        board[7][7] = "R";
        for (int col = 0; col < 8; col++) {
            board[6][col] = "P";
        }

        board[0][0] = "r";
        board[0][1] = "n";
        board[0][2] = "b";
        board[0][3] = "q";
        board[0][4] = "k";
        board[0][5] = "b";
        board[0][6] = "n";
        board[0][7] = "r";
        for (int col = 0; col < 8; col++) {
            board[1][col] = "p";
        }

        return board;
    }
}
