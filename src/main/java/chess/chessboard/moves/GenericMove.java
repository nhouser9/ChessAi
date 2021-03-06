/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Piece;
import java.awt.Point;

/**
 * Class representing a generic move which can be made on the board. Stores
 * information about the move and includes logic for executing and reverting it.
 *
 * @author Nick Houser
 */
public abstract class GenericMove {

    //general immutable move info
    private Piece piece;
    public final Point from;
    public final Point to;

    //the board on which the move will be made
    protected Board board;

    /**
     * Constructor which stores general move info and a reference to the game
     * board.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected GenericMove(Board board, Piece mover, Point targetPosition) {
        this.board = board;
        piece = mover;
        from = mover.position();
        to = targetPosition;
    }

    /**
     * Gets the Piece that is making this move.
     *
     * @return the Piece that is making this move
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Checks whether the move is valid, that is, whether it is on the board and
     * would not move onto an enemy.
     *
     * @return true if the move is valid, false otherwise
     */
    public boolean valid() {
        if (!Board.inBounds(to.x, to.y)) {
            return false;
        }

        Piece occupant = board.square(to.x, to.y).occupant();

        if (occupant == null) {
            return true;
        }

        if (occupant.color == piece.color) {
            return false;
        }

        return true;
    }

    /**
     * Method which checks whether a given move would allow the king to be
     * captured, which would make it illegal. Sets up the board as if the move
     * had been made then delegats the actual check to another method.
     *
     * @return true if the move is illegal because it allows capture of the
     * king, false otherwise
     */
    public boolean endangersKing() {
        updateBoard();

        King king = board.findKing(piece.color);

        boolean toReturn;
        if (king == null) {
            toReturn = false;
        } else {
            toReturn = king.threatened(board);
        }

        revertBoard();

        return toReturn;
    }

    /**
     * Changes the board on which the move will execute. This supports
     * multithreaded analysis by allowing copy boards to be analyzed then have
     * their results run on a main board.
     *
     * @param newBoard the new board to set.
     */
    public void changeBoard(Board newBoard) {
        board = newBoard;
        piece = newBoard.square(piece.position().x, piece.position().y).occupant();
    }

    /**
     * Makes the move, including adding the move to the board before updating
     * the active player, move history, and current valid moves. Returns whether
     * the operation was successful.
     *
     * @return true if the execution succeeded, false otherwise
     */
    public boolean execute() {
        if (!board.validMoves().contains(this)) {
            return false;
        }

        updateBoard();
        board.passTurn();
        board.history().add(this);
        board.resetValidMoves();
        return true;
    }

    /**
     * Reverts the move, including undoing the move on the board before updating
     * the active player, move history, and current valid moves. Returns whether
     * the operation was successful.
     *
     * @return true if the reversion succeeded, false otherwise
     */
    public boolean revert() {
        if (board.history().last() != this) {
            return false;
        }

        revertBoard();
        board.passTurn();
        board.history().undo();
        board.resetValidMoves();
        return true;
    }

    /**
     * Helper method which updates the board and the Piece that was moved to
     * reflect new game state following this move. Calls implementationExecute
     * to ensure special cases like en passant are handled.
     */
    private void updateBoard() {
        board.square(from.x, from.y).setOccupant(null);
        board.square(to.x, to.y).setOccupant(piece);
        piece.setPosition(to);
        piece.addMoveCount();
        implementationExecute();
    }

    /**
     * Helper method which updates the board and the Piece that was moved to
     * reflect the game state prior to this move. Calls implementationRevert to
     * ensure special cases like en passant are handled.
     */
    private void revertBoard() {
        board.square(to.x, to.y).setOccupant(null);
        board.square(from.x, from.y).setOccupant(piece);
        piece.setPosition(from);
        piece.subMoveCount();
        implementationRevert();
    }

    /**
     * Abstract method which handles special cases during move execution. One
     * example is ensuring the Rook is also moved during castling.
     */
    protected abstract void implementationExecute();

    /**
     * Abstract method which handles special cases during move reversion. One
     * example is replacing a queened pawn with a pawn when reverting a queening
     * move.
     */
    protected abstract void implementationRevert();

    /**
     * Override for the equals method for comparing to other moves.
     *
     * @param other the Object to compare this to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GenericMove)) {
            return false;
        }

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
        hash = 59 * hash + piece.hashCode();
        return hash;
    }

    /**
     * Provides a string representation of this move.
     *
     * @return a string representation of this move
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(piece.color);
        result.append(" ");
        result.append(piece.getClass().getSimpleName());
        result.append(" from (");
        result.append(from.x);
        result.append(",");
        result.append(from.y);
        result.append(") to (");
        result.append(to.x);
        result.append(",");
        result.append(to.y);
        result.append(")");
        return result.toString();
    }
}
