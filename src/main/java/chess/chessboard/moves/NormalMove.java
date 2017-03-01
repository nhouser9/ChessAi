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
 * Class representing a normal move made from one space to another that impacts
 * no other pieces.
 *
 * @author Nick Houser
 */
public class NormalMove extends GenericMove {

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected NormalMove(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
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
