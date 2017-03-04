/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.Square;
import chess.chessboard.pieces.Piece;
import java.awt.Point;

/**
 * Class representing an double move, which is a special move where a pawn moves
 * two squares on its first move.
 *
 * @author Nick Houser
 */
public class InitialPawnMove extends GenericMove {

    //the square that this move exposes to en passant
    public final Square enPassantTarget;

    /**
     * Constructor which calls the inherited constructor before saving the en
     * passant target.
     *
     * @param board the board on which the move will be made
     * @param mover the piece undergoing the move
     * @param targetPosition the location the piece is moving to
     */
    protected InitialPawnMove(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
        int yBehindMove = targetPosition.y - mover.color.forwardDirection().y();
        enPassantTarget = board.square(targetPosition.x, yBehindMove);
    }

    /**
     * The non-generic component of a regular move. Does not need to do anything
     * as the no other pieces are impacted and everything is handled by the
     * generic execution.
     */
    @Override
    protected void implementationExecute() {
    }

    /**
     * The non-generic component of a regular move. Does not need to do anything
     * as the no other pieces are impacted and everything is handled by the
     * generic reversion.
     */
    @Override
    protected void implementationRevert() {
    }
}
