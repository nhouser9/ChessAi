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
 * Class representing a capture, which is a move where a piece moves onto
 * another piece and removes it from the board.
 *
 * @author Nick Houser
 */
public class Capture extends GenericMove {

    //the enemy pawn that will be captured
    public final Piece captured;

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected Capture(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
        captured = board.occupant(to.x, to.y);
    }

    /**
     * The non-generic component of a promotion. Does not need to do anything as
     * the captured piece will be cleaned up by the generic execution.
     */
    @Override
    protected void implementationExecute() {
    }

    /**
     * The non-generic component of a promotion reversion. Does not need to do
     * anything as the promoted piece will be cleaned up by the generic
     * reversion.
     */
    @Override
    protected void implementationRevert() {
        board.setOccupant(captured.position().x, captured.position().y, captured);
    }
}
