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
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (isValidPosition(row, col + 1)) {
            addKingMoves(board, myPosition, validMoves, row, col + 1);
        } if (isValidPosition(row, col - 1)) {
            addKingMoves(board, myPosition, validMoves, row, col - 1);
        } if (isValidPosition(row - 1, col + 1)) {
            addKingMoves(board, myPosition, validMoves, row - 1, col + 1);
        } if (isValidPosition(row - 1, col)) {
            addKingMoves(board, myPosition, validMoves, row - 1, col);
        } if (isValidPosition(row - 1, col - 1)) {
            addKingMoves(board, myPosition, validMoves, row - 1, col - 1);
        } if (isValidPosition(row + 1, col + 1)) {
            addKingMoves(board, myPosition, validMoves, row + 1, col + 1);
        } if (isValidPosition(row + 1, col)) {
            addKingMoves(board, myPosition, validMoves, row + 1, col);
        } if (isValidPosition(row + 1, col - 1)) {
            addKingMoves(board, myPosition, validMoves, row + 1, col - 1);
        }

        return validMoves;
    }

    private void addKingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));
        if (pieceAtPosition == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
        } else if (pieceAtPosition.getTeamColor() != this.getTeamColor()) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
        }
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for queen moves
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        addLongMoves(board, validMoves, myPosition,row - 1, col - 1, -1, -1);
        addLongMoves(board, validMoves, myPosition,row + 1, col + 1, 1,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col - 1, 1, -1);
        addLongMoves(board, validMoves, myPosition,row - 1, col + 1, -1, 1);
        addLongMoves(board, validMoves, myPosition, row, col - 1, 0, -1);
        addLongMoves(board, validMoves, myPosition, row, col + 1, 0,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col, 1, 0);
        addLongMoves(board, validMoves, myPosition,row - 1, col, -1, 0);

        return validMoves;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        addLongMoves(board, validMoves, myPosition,row - 1, col - 1, -1, -1);
        addLongMoves(board, validMoves, myPosition,row + 1, col + 1, 1,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col - 1, 1, -1);
        addLongMoves(board, validMoves, myPosition,row - 1, col + 1, -1, 1);


        return validMoves;
    }
    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for rook moves
        Collection<ChessMove> validMoves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        addLongMoves(board, validMoves, myPosition, row, col - 1, 0, -1);
        addLongMoves(board, validMoves, myPosition, row, col + 1, 0,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col, 1, 0);
        addLongMoves(board, validMoves, myPosition,row - 1, col, -1, 0);

        return validMoves;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for knight moves
        return new ArrayList<>();
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        // ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()) + 1);
        // Implement logic for pawn moves
        return new ArrayList<>();
    }
    private void addLongMoves(ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition, int row, int col, int hor, int ver) {
        while (isValidPosition(row, col)) {
            ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));

            if (pieceAtPosition == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                col = col + ver;
                row = row + hor;
            } else if (pieceAtPosition.getTeamColor() != this.getTeamColor()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            } else { // Your own TEAM, same COLOR
                break;
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row > 0 && row <= 8 && col > 0 && col <= 8;
    }

    public String toString() {
        String actual_color = pieceColor.name();
        String actual_type = type.name();
        return String.format("-%s, %s-", actual_color, actual_type);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        ChessPiece that = (ChessPiece) obj;

        return pieceColor == that.pieceColor && type == that.type;
    }
}
