package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard gameBoard;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
    }

    public ChessGame(TeamColor turn, ChessBoard gameBoard) {
        this.turn = turn;
        this.gameBoard = gameBoard;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> curr_moves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition end = move.getEndPosition();
        ChessPosition start = move.getStartPosition();
        ChessPiece piece = gameBoard.getPiece(start);

        if (!isValidMove(start, end, piece, move)) {
            throw new InvalidMoveException("Invalid move for the piece.");
        }

        ChessBoard temp = gameBoard.copy();
        temp.addPiece(start, null);
        temp.addPiece(end, piece);
        ChessGame tempGame = new ChessGame(turn, temp);

        if (tempGame.isInCheck(turn)) { throw new InvalidMoveException("My king is left in check"); }

        gameBoard.addPiece(start, null);
        gameBoard.addPiece(end, piece);

        if (this.turn == TeamColor.WHITE) { this.turn = TeamColor.BLACK; }
        else { this.turn = TeamColor.WHITE; };
    }

    private boolean isValidMove(ChessPosition start, ChessPosition end, ChessPiece piece, ChessMove move) {
        if (piece == null) {
            return false;
        }

        if (piece.getTeamColor() != this.turn) { return false; }

        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, start);
        return possibleMoves != null && possibleMoves.contains(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPiece> king = null;
        ChessPosition king_pos = null;
        ChessPiece check_king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);

        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();

        for (int x = 1; x <=8; x++) {
            for (int y = 1; y <=8; y++) {
                ChessPiece curr_piece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition curr_pos = new ChessPosition(x, y);
                if (curr_piece != null && curr_piece.equals(check_king)) {
                    king_pos = curr_pos;
                }
                if (curr_piece != null && curr_piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> pieceMoves = curr_piece.pieceMoves(gameBoard, curr_pos);
                    allMoves.addAll(pieceMoves);
                }
            }
        }

        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(king_pos)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false; // If not in check, not in checkmate
        }

        // Iterate through all possible moves and check if any move gets the team out of check
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                ChessPiece currPiece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition currPos = new ChessPosition(x, y);

                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = currPiece.pieceMoves(gameBoard, currPos);

                    // Check each move to see if it takes the team out of check
                    for (ChessMove move : pieceMoves) {
                        ChessBoard temp = gameBoard.copy(); // Create a temporary copy of the board
                        try {
                            ChessGame newGame = new ChessGame(teamColor, temp);
                            newGame.makeMove(move);
                             // Make the move on the temporary board
                        } catch (InvalidMoveException e) {
                            // Move is invalid, continue to the next move
                            continue;
                        }

                        // If the team is not in check after making the move, it's not in checkmate
                        if (!isInCheck(teamColor)) {
                            return false;
                        }
                    }
                }
            }
        }

        // If no move can take the team out of check, it's in checkmate
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) { return false; }

        // Iterate through all possible moves and check if any move gets the team out of check
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                ChessPiece currPiece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition currPos = new ChessPosition(x, y);

                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = currPiece.pieceMoves(gameBoard, currPos);

                    // Check each move to see if it takes the team out of check
                    for (ChessMove move : pieceMoves) {
                        ChessBoard temp = gameBoard.copy(); // Create a temporary copy of the board
                        try {
                            ChessGame newGame = new ChessGame(teamColor, temp);
                            newGame.makeMove(move);
                            // Make the move on the temporary board
                        } catch (InvalidMoveException e) {
                            // Move is invalid, continue to the next move
                            continue;
                        }

                        // If the team is not in check after making the move, it's not in checkmate
                        if (!isInCheck(teamColor)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }
}
