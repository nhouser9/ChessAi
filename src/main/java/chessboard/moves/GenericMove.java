/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.moves;

import chessboard.Board;
import chessboard.pieces.Piece;
import java.awt.Point;

/**
 *
 * @author Nick Houser
 */
public abstract class GenericMove {

    //general immutable move info
    public final Piece piece;
    public final Point from;
    public final Point to;

    //the board on which the move will be made
    private final Board board;

    public GenericMove(Board board, Piece mover, Point targetPosition) {
        this.board = board;
        piece = mover;
        from = mover.position();
        to = targetPosition;
    }

    public boolean execute() {
        if (!board.validMoves().contains(this)) {
            return false;
        }

        updateBoard();
        board.passTurn();
        //board.addToHistory(this);
        board.resetValidMoves();
        return true;
    }

    /**
     * Helper method which updates the board and the Piece that was moved to
     * reflect new game state following this move. Calls implementationExecute
     * to ensure special cases like en passant are handled.
     */
    private void updateBoard() {
        board.setOccupant(from.x, from.y, null);
        board.setOccupant(to.x, to.y, piece);
        piece.setPosition(to);
        piece.addMoveCount();
        implementationExecute();
    }

    public boolean revert() {
        //if (board.lastMove() != this) {
        //return false;
        //}

        revertBoard();
        board.passTurn();
        board.removeLastMove();
        board.resetValidMoves();
        return true;
    }

    /**
     * Helper method which updates the board and the Piece that was moved to
     * reflect the game state prior to this move. Calls implementationRevert to
     * ensure special cases like en passant are handled.
     */
    private void revertBoard() {
        board.setOccupant(to.x, to.y, null);
        board.setOccupant(from.x, from.y, piece);
        piece.setPosition(from);
        piece.subMoveCount();
        implementationRevert();
    }

    public abstract GenericMove copyOf();

    protected abstract void implementationExecute();

    protected abstract void implementationRevert();

    /**
     * Override for the equals method for comparing to other moves.
     *
     * @param other the Object to compare this to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Move) {
            GenericMove otherMove = (GenericMove) other;

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

    /**
     * Method which gets a key for storing this in a hash data structure.
     *
     * @return a key for storing this in a hash data structure
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.from.x;
        hash = 59 * hash + this.from.y;
        hash = 59 * hash + this.to.x;
        hash = 59 * hash + this.to.y;
        return hash;
    }
}
