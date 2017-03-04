/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class InitialPawnMoveTest {

    @Test
    public void constructor_savesTheEnPassantTarget() {
        Piece mover = new Pawn(Color.WHITE, 4, 6);

        List<Piece> initial = new LinkedList<>();
        initial.add(mover);
        Board board = new Board(initial, Color.WHITE);

        InitialPawnMove test = new InitialPawnMove(board, mover, new Point(4, 4));

        assertTrue(test.enPassantTarget == board.square(4, 5));
    }
}
