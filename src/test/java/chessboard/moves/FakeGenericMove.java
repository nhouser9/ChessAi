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
 * Fake implementation of generic move class, for unit testing.
 *
 * @author Nick Houser
 */
public class FakeGenericMove extends GenericMove {

    public FakeGenericMove(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
    }

    @Override
    protected void implementationExecute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void implementationRevert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
