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
 * Class representing a kingside castle, which is a special move where the king
 * castles with the rook on its side of the board.
 *
 * @author Nick Houser
 */
public class KingsideCastle extends Castle {

    /**
     * Constructor which calls the inherited constructor.
     *
     * @param board the game board on which the move will be made
     * @param mover the Piece undertaking the move
     * @param targetPosition the Point the mover is moving to
     */
    protected KingsideCastle(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
    }

    /**
     * Returns the initial x position of the kingside rook
     *
     * @return the initial x position of the kingside rook
     */
    @Override
    protected int rookInitialX() {
        return 7;
    }

    /**
     * Returns the initial x position of the kingside rook
     *
     * @return the initial x position of the kingside rook
     */
    @Override
    protected int rookTargetX() {
        return 5;
    }
}
