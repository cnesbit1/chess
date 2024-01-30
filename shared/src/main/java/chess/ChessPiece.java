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
        return switch (getPieceType()) {
            case KING -> getKingMoves(board, myPosition);
            case QUEEN -> getQueenMoves(board, myPosition);
            case BISHOP -> getBishopMoves(board, myPosition);
            case ROOK -> getRookMoves(board, myPosition);
            case KNIGHT -> getKnightMoves(board, myPosition);
            case PAWN -> getPawnMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for king moves
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

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
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

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

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for pawn moves
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            whitePawnMoves(board, myPosition, validMoves, row, col);
        }
        else {
            blackPawnMoves(board, myPosition, validMoves, row, col);
        }

        return validMoves;
    }

    private void whitePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        ChessPiece doubleFrontPiece = null;
        if (row == 2) {
            doubleFrontPiece = board.getPiece(new ChessPosition(row + 2, col));
        }

        boolean noPromotion = true;
        if (row == 7) {
            noPromotion = false;
        }

        if (isValidPosition(row + 1, col + 1)) {
            ChessPiece rightPiece = board.getPiece(new ChessPosition(row + 1, col + 1));
            if (rightPiece != null && rightPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.KNIGHT));
                }
            }
        }
        if (isValidPosition(row + 1, col)) {
            ChessPiece frontPiece = board.getPiece(new ChessPosition(row + 1, col));
            if (frontPiece == null) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.KNIGHT));
                }
                if (row == 2 && doubleFrontPiece == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
                }
            }
        }
        if (isValidPosition(row + 1, col - 1)) {
            ChessPiece leftPiece = board.getPiece(new ChessPosition(row + 1, col - 1));
            if (leftPiece != null && leftPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.KNIGHT));
                }
            }
        }
    }

    private void blackPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        ChessPiece doubleFrontPiece = null;
        if (row == 7) {
            doubleFrontPiece = board.getPiece(new ChessPosition(row - 2, col));
        }

        boolean noPromotion = true;
        if (row == 2) {
            noPromotion = false;
        }

        if (isValidPosition(row - 1, col + 1)) {
            ChessPiece rightPiece = board.getPiece(new ChessPosition(row - 1, col + 1));
            if (rightPiece != null && rightPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.KNIGHT));
                }
            }
        }
        if (isValidPosition(row - 1, col)) {
            ChessPiece frontPiece = board.getPiece(new ChessPosition(row - 1, col));
            if (frontPiece == null) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.KNIGHT));
                }
                if (row == 7 && doubleFrontPiece == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
                }
            }
        }
        if (isValidPosition(row - 1, col - 1)) {
            ChessPiece leftPiece = board.getPiece(new ChessPosition(row - 1, col - 1));
            if (leftPiece != null && leftPiece.getTeamColor() != this.getTeamColor()) {
                if (noPromotion) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.KNIGHT));
                }
            }
        }
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

    private void addShortMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));
        if (pieceAtPosition == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
        } else if (pieceAtPosition.getTeamColor() != this.getTeamColor()) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
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

    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
