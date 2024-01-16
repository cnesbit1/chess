package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;

    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (getPieceType()) {
            case KING:
                return getKingMoves(board, myPosition);
            case QUEEN:
                return getQueenMoves(board, myPosition);
            case BISHOP:
                return getBishopMoves(board, myPosition);
            case ROOK:
                return getRookMoves(board, myPosition);
            case KNIGHT:
                return getKnightMoves(board, myPosition);
            case PAWN:
                return getPawnMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for king moves
        return new ArrayList<>();
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for queen moves
        return new ArrayList<>();
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        addDiagonalMoves(board, validMoves, myPosition,row - 1, col - 1, -1, -1);
        addDiagonalMoves(board, validMoves, myPosition,row + 1, col + 1, 1,1);
        addDiagonalMoves(board, validMoves, myPosition,row - 1, col + 1, -1, 1);
        addDiagonalMoves(board, validMoves, myPosition,row + 1, col - 1, 1, -1);

        return validMoves;
    }

    private void addDiagonalMoves(ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition, int row, int col, int hor, int ver) {
        while (isValidPosition(row, col)) {
            ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));

            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            }

            if (pieceAtPosition != null) {
                break;
            }

            col = col + ver;
            row = row + hor;
        }
    }

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for rook moves
        return new ArrayList<>();
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for knight moves
        return new ArrayList<>();
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for pawn moves
        return new ArrayList<>();
    }

    private boolean isValidPosition(int row, int col) {
        return row > 0 && row < 8 && col > 0 && col < 8;
    }

    public String toString() {
        String actual_color = pieceColor.name();
        String actual_type = type.name();
        return String.format("-%s, %s-", actual_color, actual_type);
    }
}
