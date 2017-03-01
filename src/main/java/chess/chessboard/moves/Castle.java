/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.pieces.Piece;
import java.awt.Point;

/**
 * Class representing a castle, which is a special move where a king moves two
 * spaces and places a rook on its other side.
 *
 * @author Nick Houser
 */
public abstract class Castle extends GenericMove {

    //the Rook which is casling with the king
    public final Piece swap;

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected Castle(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
        swap = board.occupant(rookInitialX(), getPiece().color.homeRow());
    }

    /**
     * Abstract method which returns the initial x co-ordinate of the rook with
     * which the king is castling.
     *
     * @return the initial x co-ordinate of the rook with which the king is
     * castling
     */
    protected abstract int rookInitialX();

    /**
     * Abstract method which returns the target x co-ordinate of the rook with
     * which the king is castling.
     *
     * @return the target x co-ordinate of the rook with which the king is
     * castling
     */
    protected abstract int rookTargetX();

    /**
     * The non-generic component of a castle. Moves the rook to the opposite
     * side of the king.
     */
    @Override
    protected void implementationExecute() {
        board.setOccupant(rookInitialX(), getPiece().color.homeRow(), null);
        board.setOccupant(rookTargetX(), getPiece().color.homeRow(), swap);
        swap.setPosition(new Point(rookTargetX(), getPiece().color.homeRow()));
    }

    /**
     * The non-generic component of a castle reversion. Returns the rook to its
     * original position.
     */
    @Override
    protected void implementationRevert() {
        board.setOccupant(rookInitialX(), getPiece().color.homeRow(), swap);
        board.setOccupant(rookTargetX(), getPiece().color.homeRow(), null);
        swap.setPosition(new Point(rookInitialX(), getPiece().color.homeRow()));
    }
}
