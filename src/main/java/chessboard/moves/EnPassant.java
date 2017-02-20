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
 *
 * @author Nick Houser
 */
public class EnPassant extends Move {

    public EnPassant(Board board, Piece mover, Point targetPosition) {
        super(board, mover, targetPosition);
    }

}
