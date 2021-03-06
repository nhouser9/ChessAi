/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Queen;
import java.awt.Point;

/**
 * Class representing a promotion, which is a special move where a pawn moves to
 * the last rank and becomes the piece of its owner's choice.
 *
 * @author Nick Houser
 */
public class Promotion extends GenericMove {

    //the enemy pawn that will be captured
    public final Piece captured;

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected Promotion(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
        captured = board.square(to.x, to.y).occupant();
    }

    /**
     * The non-generic component of a promotion. Replaces the pawn with a
     * promoted piece.
     */
    @Override
    protected void implementationExecute() {
        Piece promotion = new Queen(getPiece().color, to.x, to.y);
        board.square(to.x, to.y).setOccupant(promotion);
    }

    /**
     * The non-generic component of a promotion reversion. Does not need to do
     * anything as the promoted piece will be cleaned up by the generic
     * reversion.
     */
    @Override
    protected void implementationRevert() {
        if (captured != null) {
            board.square(captured.position().x, captured.position().y).setOccupant(captured);
        }
    }
}
