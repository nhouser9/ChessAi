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
 * Fake implementation of generic move class, for unit testing.
 *
 * @author Nick Houser
 */
public class StubGenericMove extends GenericMove {

    public StubGenericMove(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
    }

    @Override
    protected void implementationExecute() {
    }

    @Override
    protected void implementationRevert() {
    }

}
