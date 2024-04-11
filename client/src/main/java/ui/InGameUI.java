package ui;

import chess.*;
import com.google.gson.Gson;
import model.GameData;
import webSocketMessages.severMessages.Error;
import webSocketMessages.severMessages.LoadGame;
import webSocketMessages.severMessages.Notification;
import webSocketMessages.severMessages.ServerMessage;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;

import java.util.Objects;

public class InGameUI extends AbstractREPL {

    private enum OptionsUI {
        REDRAWCHESSBOARD(1, "redraw the chess board"),
        LEAVE(2, "leave"),
        MAKEMOVE(3, "make a move"),
        RESIGN(4, "resign"),
        HIGHLIGHTLEGALMOVES(5, "highlight legal moves");
        private final int number;
        private final String description;
        OptionsUI(int number, String description) {
            this.number = number;
            this.description = description;
        }
    }

    public LoadGame loadGame;
    public Notification notification;
    public Error error;

    public InGameUI(SwitchUILoop programLoop, ServerFacade serverWrapper) { super(programLoop, serverWrapper); }
    @Override
    public void processInput(String input) {
        try {
            if (Objects.equals(input, "quit")) { throw new Exception(); }

            int choice = Integer.parseInt(input);
            if (choice == 1) {
                GameData gameData = this.loadGame.getGame();
                ChessGame game = gameData.game();
                ChessBoard chessBoard = game.getBoard();
                showChessboard(gameData, chessBoard);
            }
            else if (choice == 2) {
                Leave leave = new Leave(serverWrapper.conn.authToken, loadGame.getGame().gameID());
                Gson gson = new Gson();
                String leaveJson = gson.toJson(leave);
                serverWrapper.webConn.sendMessage(leaveJson);
                programLoop.switchToSignedIn();
                changeUI();
            }
            else if (choice == 3) {
                System.out.println("Input Row Number for Piece to be Moved:");
                int row = Integer.parseInt(scanner.nextLine());
                System.out.println("Input Column Letter for Piece to be Moved:");
                String colValue = scanner.nextLine();
                int col = convertLettertoIndex(colValue);;
                System.out.println("Input Row Number for Piece Destination:");
                int rowDest = Integer.parseInt(scanner.nextLine());
                System.out.println("Input Column Letter for Piece Destination:");
                String colValueD = scanner.nextLine();
                int colDest = convertLettertoIndex(colValueD);;

                ChessPosition startPosition = new ChessPosition(row, col);
                ChessPosition endPosition = new ChessPosition(rowDest, colDest);

                GameData gameData = this.loadGame.getGame();
                int promotion = 8;
                if (serverWrapper.username.equalsIgnoreCase("black")) {
                    promotion = 1;
                }
                else if (serverWrapper.username.equalsIgnoreCase("white")) {
                    promotion = 8;
                }

                String promotionPiece = null;
                ChessPiece.PieceType type = null;
                if (rowDest == promotion) {
                    System.out.println("Input Promotion Type for Move");
                    promotionPiece = scanner.nextLine();
                    type = convertStringToType(promotionPiece);
                }
                ChessMove move = new ChessMove(startPosition, endPosition, type);

                MakeMove makeMove = new MakeMove(serverWrapper.conn.authToken, gameData.gameID(), move);
                Gson gson = new Gson();
                String makeMoveJson = gson.toJson(makeMove);
                serverWrapper.webConn.sendMessage(makeMoveJson);

            }
            else if (choice == 4) {
                Resign resign = new Resign(serverWrapper.conn.authToken, loadGame.getGame().gameID());
                Gson gson = new Gson();
                String resignJson = gson.toJson(resign);
                serverWrapper.webConn.sendMessage(resignJson);
            }
            else if (choice == 5) {
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

    public int convertLettertoIndex(String colValue) throws Exception {
        return switch (colValue) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new Exception();
        };
    }

    public ChessPiece.PieceType convertStringToType(String promotionPiece) throws Exception {
        return switch (promotionPiece) {
            case "Queen" -> ChessPiece.PieceType.QUEEN;
            case "Rook" -> ChessPiece.PieceType.ROOK;
            case "Bishop" -> ChessPiece.PieceType.BISHOP;
            case "Knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new Exception();
        };
    }
    @Override
    public void printHelp() {
        System.out.println("Redraw Chess Board: update the display of the chessboard.");
        System.out.println("Leave: removes the user from the game.");
        System.out.println("Make Move: allows the user to move a chess piece.");
        System.out.println("Resign: concede the game to the opponent");
        System.out.println("Highlight Legal Moves: show all possible legal moves for piece");
        System.out.println();
    }
    @Override
    public void showUIPrompt() {
        System.out.println("Give an input of 'help' for more possible instructions.");
        for (OptionsUI option : OptionsUI.values()) {
            System.out.println("Enter " + option.number + " to " + option.description + ".");
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        if (serverMessage instanceof LoadGame) {
            LoadGame loadGame = (LoadGame) serverMessage;
            this.loadGame = loadGame;
            GameData game = loadGame.getGame();
            ChessGame chessGame = game.game();
            ChessBoard chessBoard = chessGame.getBoard();
            if (chessBoard.checkEmpty()) {
                chessBoard.resetBoard();

                System.out.println();
                System.out.println("Successful Game Joined");
                System.out.println();
            }

            this.showChessboard(game, chessBoard);

            programLoop.switchToInGame();
            changeUI();
        }
        if (serverMessage instanceof Notification) {
            Notification notification = (Notification) serverMessage;
            this.notification = notification;
            String message = notification.getMessage();
            System.out.println();
            System.out.println(message);
            System.out.println();
        }
        if (serverMessage instanceof Error) {
            Error error = (Error) serverMessage;
            this.error = error;
            String message = error.getErrorMessage();
            System.out.println();
            System.out.println(message);
            System.out.println();
        }
    }
    public void showChessboard(GameData game, ChessBoard chessBoard) {

        // Print the current board
        if (serverWrapper.username.equalsIgnoreCase(game.whiteUsername())) {
            displayChessboard(chessBoard, true);
        }
        else if (serverWrapper.username.equalsIgnoreCase(game.blackUsername())) {
            displayChessboard(chessBoard, false);
        }
        else {
            displayChessboard(chessBoard, true);
        }

        System.out.println();
    }
}
