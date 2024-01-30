package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() { return row; }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() { return col; }

    public String toString() {
        return String.format("(%s, %s)", row, col);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessPosition chessPos = (ChessPosition) obj;

        return row == chessPos.row && col == chessPos.col;
    }

    public int hashCode() {
        return Objects.hash(row, col);
    }
}
