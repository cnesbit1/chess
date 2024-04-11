package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
    public PieceType getPieceType() { return type; }

    public String toString() {
        String actualColor = pieceColor.name();
        String actualType = type.name();
        return String.format("-%s, %s-", actualColor, actualType);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        ChessPiece that = (ChessPiece) obj;

        return pieceColor == that.pieceColor && type == that.type;
    }

    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        return switch (getPieceType()) {
            case KING -> getKingMoves(board, myPosition, row, col);
            case QUEEN -> getQueenMoves(board, myPosition, row, col);
            case BISHOP -> getBishopMoves(board, myPosition, row, col);
            case ROOK -> getRookMoves(board, myPosition, row, col);
            case KNIGHT -> getKnightMoves(board, myPosition, row, col);
            case PAWN -> getPawnMoves(board, myPosition, row, col);
        };
    }

    private boolean isValidPosition(int row, int col) {
        return row > 0 && row <= 8 && col > 0 && col <= 8;
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
            }
            else { break; }
        }
    }

    private void addShortMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));
        if (pieceAtPosition == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
        }
        else if (pieceAtPosition.getTeamColor() != this.getTeamColor()) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
        }
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        if (isValidPosition(row, col + 1)) {
            addShortMove(board, myPosition, validMoves, row, col + 1);
        } if (isValidPosition(row, col - 1)) {
            addShortMove(board, myPosition, validMoves, row, col - 1);
        } if (isValidPosition(row - 1, col + 1)) {
            addShortMove(board, myPosition, validMoves, row - 1, col + 1);
        } if (isValidPosition(row - 1, col)) {
            addShortMove(board, myPosition, validMoves, row - 1, col);
        } if (isValidPosition(row - 1, col - 1)) {
            addShortMove(board, myPosition, validMoves, row - 1, col - 1);
        } if (isValidPosition(row + 1, col + 1)) {
            addShortMove(board, myPosition, validMoves, row + 1, col + 1);
        } if (isValidPosition(row + 1, col)) {
            addShortMove(board, myPosition, validMoves, row + 1, col);
        } if (isValidPosition(row + 1, col - 1)) {
            addShortMove(board, myPosition, validMoves, row + 1, col - 1);
        }

        return validMoves;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getRookMoves(board, myPosition, row, col));
        validMoves.addAll(getBishopMoves(board, myPosition, row, col));

        return validMoves;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        addLongMoves(board, validMoves, myPosition,row - 1, col - 1, -1, -1);
        addLongMoves(board, validMoves, myPosition,row + 1, col + 1, 1,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col - 1, 1, -1);
        addLongMoves(board, validMoves, myPosition,row - 1, col + 1, -1, 1);


        return validMoves;
    }
    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        addLongMoves(board, validMoves, myPosition, row, col - 1, 0, -1);
        addLongMoves(board, validMoves, myPosition, row, col + 1, 0,1);
        addLongMoves(board, validMoves, myPosition,row + 1, col, 1, 0);
        addLongMoves(board, validMoves, myPosition,row - 1, col, -1, 0);

        return validMoves;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        if (isValidPosition(row - 2, col + 1)) {
            addShortMove(board, myPosition, validMoves, row - 2, col + 1);
        } if (isValidPosition(row - 2, col - 1)) {
            addShortMove(board, myPosition, validMoves, row - 2, col - 1);
        } if (isValidPosition(row + 2, col + 1)) {
            addShortMove(board, myPosition, validMoves, row + 2, col + 1);
        } if (isValidPosition(row + 2, col - 1)) {
            addShortMove(board, myPosition, validMoves, row + 2, col - 1);
        } if (isValidPosition(row + 1, col + 2)) {
            addShortMove(board, myPosition, validMoves, row + 1, col + 2);
        } if (isValidPosition(row - 1, col + 2)) {
            addShortMove(board, myPosition, validMoves, row - 1, col + 2);
        } if (isValidPosition(row + 1, col - 2)) {
            addShortMove(board, myPosition, validMoves, row + 1, col - 2);
        } if (isValidPosition(row - 1, col - 2)) {
            addShortMove(board, myPosition, validMoves, row - 1, col - 2);
        }

        return validMoves;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            getTeamPawnMoves(board, myPosition, validMoves, row, col, 1, 7, 2);
        }
        else {
            getTeamPawnMoves(board, myPosition, validMoves, row, col, -1, 2, 7);
        }
        return validMoves;
    }

    private void getTeamPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int dir, int promotionRow, int startingRow) {
        ChessPiece doubleFrontPiece = null;
        if (row == startingRow) {
            doubleFrontPiece = board.getPiece(new ChessPosition(row + dir*2, col));
        }

        boolean noPromotion = true;
        if (row == promotionRow) {
            noPromotion = false;
        }

        if (isValidPosition(row + dir, col)) {
            ChessPiece frontPiece = board.getPiece(new ChessPosition(row + dir, col));
            if (frontPiece == null) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col), PieceType.KNIGHT));
                }
                if (row == startingRow && doubleFrontPiece == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir*2, col), null));
                }
            }
        }
        if (isValidPosition(row + dir, col + 1)) {
            ChessPiece rightPiece = board.getPiece(new ChessPosition(row + dir, col + 1));
            if (rightPiece != null && rightPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col + 1), null));
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col + 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col + 1), PieceType.KNIGHT));
                }
            }
        }
        if (isValidPosition(row + dir, col - 1)) {
            ChessPiece leftPiece = board.getPiece(new ChessPosition(row + dir, col - 1));
            if (leftPiece != null && leftPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col - 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col - 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + dir, col - 1), PieceType.KNIGHT));
                }
            }
        }
    }
}