package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Scanner;

public abstract class AbstractREPL implements NotificationHandler {
    public static final Scanner scanner = new Scanner(System.in);
    public SwitchUILoop programLoop;
    public ServerFacade serverWrapper;
    public boolean switchUI = false;

    public String username;
    public AbstractREPL(SwitchUILoop programLoop, ServerFacade serverWrapper) {
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

    public String displayColHeaders(boolean whiteAtBottom) {
        StringBuilder columnsBuilder = new StringBuilder("    ");

        char colStartLabel = whiteAtBottom ? 'a' : 'h';
        char colEndLabel = whiteAtBottom ? 'h' : 'a';
        int colLabelStep = whiteAtBottom ? 1 : -1;

        for (char col = colStartLabel; col != colEndLabel + colLabelStep; col += colLabelStep) {
            columnsBuilder.append(" ").append(col).append(" ");
        }

        return columnsBuilder.toString();
    }

    public void displayChessboard(ChessBoard chessBoard, boolean whiteAtBottom) {
        String columns = displayColHeaders(whiteAtBottom);
        System.out.println(columns);
        displayRows(chessBoard, whiteAtBottom);
        System.out.println(columns);
    }

    private void displayRows(ChessBoard chessBoard, boolean whiteAtBottom) {
        int rowStart = whiteAtBottom ? 7 : 0;
        int rowEnd = whiteAtBottom ? -1 : 8;
        int rowStep = whiteAtBottom ? -1 : 1;

        for (int row = rowStart; row != rowEnd; row += rowStep) {
            String displayRow = "  " + (row + 1) + " ";
            System.out.print(displayRow);
            displayRow(chessBoard, row, whiteAtBottom);
            System.out.println(displayRow);
        }
    }

    private void displayRow(ChessBoard chessBoard, int row, boolean whiteAtBottom) {
        int colStart = whiteAtBottom ? 0 : 7;
        int colEnd = whiteAtBottom ? 8 : -1;
        int colStep = whiteAtBottom ? 1 : -1;

        for (int col = colStart; col != colEnd; col += colStep) {
            ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));
            String pieceString = piece != null ? getPieceRepresentation(piece) : " ";
            String textColor = getPieceColor(pieceString);
            String backgroundColor = (row + col) % 2 == 0 ? "\u001B[40m" : "\u001B[47m";

            System.out.print(backgroundColor + textColor + " " + pieceString + " \u001B[0m");
        }
    }

    private String getPieceRepresentation(ChessPiece piece) {
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();

        String pieceRepresentation;
        switch (pieceType) {
            case PAWN:
                pieceRepresentation = "p";
                break;
            case ROOK:
                pieceRepresentation = "r";
                break;
            case KNIGHT:
                pieceRepresentation = "n";
                break;
            case BISHOP:
                pieceRepresentation = "b";
                break;
            case QUEEN:
                pieceRepresentation = "q";
                break;
            case KING:
                pieceRepresentation = "k";
                break;
            default:
                pieceRepresentation = " ";
                break;
        }

        return teamColor == ChessGame.TeamColor.WHITE ? pieceRepresentation.toLowerCase() : pieceRepresentation.toUpperCase();
    }

    private String getPieceColor(String piece) {
        return Character.isUpperCase(piece.charAt(0)) ? "\u001B[34m" : "\u001B[31m";
    }
    public String[][] populateStartingChessboard() {
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
