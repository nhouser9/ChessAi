/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import java.util.List;

/**
 * Fake implementation of Piece for unit testing.
 */
public class StubPiece extends Piece {

    public StubPiece(Color color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public void addPositionalRules() {
    }

    @Override
    public double materialValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GenericMove> validMoves(Board board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Piece copy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected char fenChar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
