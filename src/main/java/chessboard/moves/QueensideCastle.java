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
 * Class representing a queenside castle, which is a special move where the king
 * castles with the rook on the queen's side of the board.
 *
 * @author Nick Houser
 */
public class QueensideCastle extends Castle {

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected QueensideCastle(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
    }

    /**
     * Returns the initial x position of the queenside rook
     *
     * @return the initial x position of the queenside rook
     */
    @Override
    protected int rookInitialX() {
        return 0;
    }

    /**
     * Returns the initial x position of the queenside rook
     *
     * @return the initial x position of the queenside rook
     */
    @Override
    protected int rookTargetX() {
        return 3;
    }
}
