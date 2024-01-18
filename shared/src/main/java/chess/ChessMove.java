package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;

    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public String toString() {
        String formatted_move = String.format("{%s to %s", startPosition, endPosition);
        if (promotionPiece != null) {
            formatted_move += String.format(" promoted to %s}", promotionPiece);
        }
        else {
            formatted_move += "}";
        }
        return formatted_move;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessMove chessMove = (ChessMove) obj;
        boolean bol = true;
        if (startPosition.getRow() != chessMove.startPosition.getRow() || startPosition.getColumn() != chessMove.startPosition.getColumn()) {
            bol = false;
        }
        if (endPosition.getRow() != chessMove.endPosition.getRow() || endPosition.getColumn() != chessMove.endPosition.getColumn()) {
            bol = false;
        }
        if (promotionPiece != chessMove.promotionPiece)
            bol = false;

        return bol;
    }

    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
