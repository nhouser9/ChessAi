/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

/**
 * Enumeration of the possible colors of chess peices.
 *
 * @author Nick Houser
 */
public enum Color {
    BLACK {
        @Override
        public Direction forwardDirection() {
            return Direction.SOUTH;
        }

        @Override
        public Direction[] forwardDiagonals() {
            return new Direction[]{Direction.SOUTHEAST, Direction.SOUTHWEST};
        }

        @Override
        public Color enemy() {
            return Color.WHITE;
        }

        @Override
        public int homeRow() {
            return 0;
        }
    },
    WHITE {
        @Override
        public Direction forwardDirection() {
            return Direction.NORTH;
        }

        @Override
        public Direction[] forwardDiagonals() {
            return new Direction[]{Direction.NORTHEAST, Direction.NORTHWEST};
        }

        @Override
        public Color enemy() {
            return Color.BLACK;
        }

        @Override
        public int homeRow() {
            return Board.SQUARES_PER_SIDE - 1;
        }
    };

    /**
     * Returns the forward direction for this color, used for calculating pawn
     * movement.
     *
     * @return the forward Direction for this color
     */
    public abstract Direction forwardDirection();

    /**
     * Returns the forward diagonals for this color, used for calculating pawn
     * movement.
     *
     * @return the two forward diagonal Directions for this color
     */
    public abstract Direction[] forwardDiagonals();

    /**
     * Returns the opposite of this color.
     *
     * @return the opposing color
     */
    public abstract Color enemy();

    /**
     * Returns the row on which this color pieces begin the game.
     *
     * @return the row on which this color pieces begin
     */
    public abstract int homeRow();

    /**
     * Returns the queen row for this color's pieces by moving to the opposite
     * color's home row.
     *
     * @return the row on which this color pawns become queens
     */
    public int queenRow() {
        return enemy().homeRow();
    }

    /**
     * Returns the row on which this color pawns begin the game by moving
     * forward one from the home row for pieces.
     *
     * @return the row on which pawns of this color begin
     */
    public int pawnRow() {
        return homeRow() + forwardDirection().y();
    }
}
