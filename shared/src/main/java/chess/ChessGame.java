package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard gameBoard;

    public boolean gameComplete;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
        this.gameComplete = false;
    }

    public ChessGame(TeamColor turn, ChessBoard gameBoard) {
        this.turn = turn;
        this.gameBoard = gameBoard;
    }

    public void endGame() {
        this.gameComplete = true;
    }

    public boolean isGameComplete() {
        return this.gameComplete;
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
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.gameBoard = board; }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return this.gameBoard; }

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
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) { return Collections.emptyList(); }
        this.turn = piece.getTeamColor();

        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> filteredMoves = new HashSet<>();
        filterMoves(possibleMoves, filteredMoves);
        return filteredMoves;
    }

    private void filterMoves(Collection<ChessMove> possibleMoves, Collection<ChessMove> filteredMoves) {
        ChessGame tempGame;
        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = gameBoard.copy();
            try {
                tempGame = new ChessGame(turn, tempBoard);
                tempGame.makeMove(move);
                if (!tempGame.isInCheck(turn)) { filteredMoves.add(move); }
            }
            catch (InvalidMoveException ignored) {}
        }
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

        if (!isValidMove(start, end, piece, move)) { throw new InvalidMoveException("Invalid move for the piece."); }

        simulateBoardWithMove(start, end, piece, move);
        updateBoardWithMove(start, end, piece, move);
        switchTurn(getTeamTurn());
    }

    private void simulateBoardWithMove(ChessPosition start, ChessPosition end, ChessPiece piece, ChessMove move) throws InvalidMoveException {
        ChessBoard temp = gameBoard.copy();
        temp.addPiece(start, null);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.WHITE && end.getRow() == 8) {
            temp.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.BLACK && end.getRow() == 1) {
            temp.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else { temp.addPiece(end, piece); }
        ChessGame tempGame = new ChessGame(turn, temp);
        if (tempGame.isInCheck(turn)) { throw new InvalidMoveException("My king is left in check"); }
    }

    private void updateBoardWithMove(ChessPosition start, ChessPosition end, ChessPiece piece, ChessMove move) {
        gameBoard.addPiece(start, null);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.WHITE && end.getRow() == 8) {
            gameBoard.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.BLACK && end.getRow() == 1) {
            gameBoard.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else { gameBoard.addPiece(end, piece); }
    }

    private boolean isValidMove(ChessPosition start, ChessPosition end, ChessPiece piece, ChessMove move) {
        if (piece == null) { return false; }
        if (piece.getTeamColor() != getTeamTurn()) { return false; }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, start);
        return possibleMoves != null && possibleMoves.contains(move);
    }

    private void switchTurn(TeamColor color) {
        if (color == TeamColor.WHITE) { setTeamTurn(TeamColor.BLACK); }
        else { setTeamTurn(TeamColor.WHITE); };
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_pos = null;
        ChessPiece check_king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        for (int x = 1; x <=8; x++) {
            for (int y = 1; y <=8; y++) {
                ChessPiece curr_piece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition curr_pos = new ChessPosition(x, y);

                if (curr_piece != null && curr_piece.equals(check_king)) { king_pos = curr_pos; }
            }
        }


        for (int x = 1; x <=8; x++) {
            for (int y = 1; y <=8; y++) {
                ChessPiece curr_piece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition curr_pos = new ChessPosition(x, y);

                if (curr_piece != null && curr_piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> pieceMoves = curr_piece.pieceMoves(gameBoard, curr_pos);
                    allMoves.addAll(pieceMoves);
                }
            }
        }

        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(king_pos)) { return true; }
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
        if (!isInCheck(teamColor)) { return false; }
        return isInMate(teamColor);
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
        return isInMate(teamColor);
    }

    private boolean isInMate(TeamColor teamColor) {
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                ChessPiece currPiece = gameBoard.getPiece(new ChessPosition(x, y));
                ChessPosition currPos = new ChessPosition(x, y);

                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = currPiece.pieceMoves(gameBoard, currPos);
                    ChessGame newGame;

                    for (ChessMove move : pieceMoves) {
                        ChessBoard temp = gameBoard.copy(); // Create a temporary copy of the board
                        try {
                            newGame = new ChessGame(teamColor, temp);
                            newGame.makeMove(move);
                        }
                        catch (InvalidMoveException e) { continue; }

                        if (!newGame.isInCheck(teamColor)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
