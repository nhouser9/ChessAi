/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.moves.GenericMove;
import java.util.List;

/**
 * Fake implementation of Piece for unit testing.
 */
public class FakePiece extends Piece {

    public FakePiece(Color color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void addPositionalRules() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double materialValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GenericMove> validMoves(Board board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
