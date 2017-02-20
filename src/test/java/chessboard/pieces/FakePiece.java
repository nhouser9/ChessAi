/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import java.awt.Point;
import java.util.List;

/**
 * Fake implementation of Piece for unit testing.
 */
public class FakePiece extends Piece {

    public FakePiece(Color color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public List<Point> validMoves(Board board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
