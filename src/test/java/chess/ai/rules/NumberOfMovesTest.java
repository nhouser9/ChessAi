/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
import chess.chessboard.pieces.Rook;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberOfMovesTest {

    @Test
    public void count_returnsTheNumberOfValidMoves_forThePassedPiece() {
        NumberOfMoves rule = new NumberOfMoves();
        Board fakeBoard = mock(Board.class);

        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Rook testRook = new Rook(Color.BLACK, 0, 0);
        assertTrue(testRook.validMoves(fakeBoard).size() == rule.count(testRook, fakeBoard));
    }
}
