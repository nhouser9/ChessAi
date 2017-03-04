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
 * Class representing an en passant move, which is a special move where a pawn
 * can capture an enemy whose first move takes it past the pawn in question.
 *
 * @author Nick Houser
 */
public class EnPassant extends GenericMove {

    //the enemy pawn that will be captured
    public final Piece captured;

    /**
     * Constructor which calls the inherited constructor before calculating and
     * storing the target of this en passant.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected EnPassant(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
        int captureY = targetPosition.y - mover.color.forwardDirection().y();
        captured = board.square(targetPosition.x, captureY).occupant();
    }

    /**
     * The non-generic component of an en passant move. Removes the captured
     * piece.
     */
    @Override
    protected void implementationExecute() {
        board.square(captured.position().x, captured.position().y).setOccupant(null);
    }

    /**
     * The non-generic component of an en passant reversion. Restores the
     * captured piece.
     */
    @Override
    protected void implementationRevert() {
        board.square(captured.position().x, captured.position().y).setOccupant(captured);
    }
}
