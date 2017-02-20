/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.moves;

import chessboard.Board;
import chessboard.pieces.King;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import java.awt.Point;

/**
 * Class representing a move made in the game, used for storing history and
 * calculating whether players can castle or capture en passant.
 *
 * @author Nick Houser
 */
public class Move {

    //final variables describing the details of the move
    public final Piece piece;
    public final Point from;
    public final Piece target;
    public final Point to;
    public final boolean enPassant;
    public final boolean castle;

    //the board on which the move will be made
    private final Board board;

    /**
     * Constructor which saves variables.
     *
     * @param board the game board on which the move is being made
     * @param mover the Piece that made the move
     * @param targetPosition the point the move will end at
     */
    public Move(Board board, Piece mover, Point targetPosition) {
        this.board = board;

        piece = mover;
        from = mover.position();
        to = targetPosition;

        enPassant = isEnPassant(board);
        castle = isCastle();

        if (enPassant) {
            target = board.occupant(to.x, to.y - mover.color.forwardDirection().y());
        } else {
            target = board.occupant(to.x, to.y);
        }
    }

    /**
     * Makes this move.
     */
    public void execute() {

    }

    /**
     * Reverts this move.
     */
    public void revert() {

    }

    /**
     * Method which determines if this move was en passant.
     *
     * @param board
     * @return
     */
    private boolean isEnPassant(Board board) {
        if (!(piece instanceof Pawn)) {
            return false;
        }

        if (from.x == to.x) {
            return false;
        }

        if (board.occupant(to.x, to.y) != null) {
            return false;
        }

        return true;
    }

    /**
     * Method which determines if this move was a castle.
     */
    private boolean isCastle() {
        if (!(piece instanceof King)) {
            return false;
        }

        return (Math.abs(from.x - to.x) > 1);
    }

    /**
     * Override for the equals method for comparing to other moves.
     *
     * @param other the Object to compare this to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Move) {
            Move otherMove = (Move) other;

            if (!otherMove.from.equals(from)) {
                return false;
            }

            if (!otherMove.to.equals(to)) {
                return false;
            }

            if (!otherMove.piece.equals(piece)) {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.from.x;
        hash = 59 * hash + this.from.y;
        hash = 59 * hash + this.to.x;
        hash = 59 * hash + this.to.y;
        return hash;
    }

    /**
     * Method which returns a copy of this Move.
     *
     * @param board the board used to determine whether this is a special move.
     * @return a copy of this move
     */
    public Move copyOf(Board board) {
        return new Move(board, piece.copyOf(), new Point(to.x, to.y));
    }
}
