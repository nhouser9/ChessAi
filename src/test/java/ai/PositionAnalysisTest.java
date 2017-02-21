/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.pieces.King;
import chessboard.pieces.Rook;
import java.awt.Point;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionAnalysisTest {

    @Test
    public void constructor_addsMaterialAndPositionalValues() {
        Rook testRook = new Rook(Color.WHITE, 0, 0);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(testRook);

        PositionAnalysis test = new PositionAnalysis(fakeBoard);

        assertTrue(test.value == testRook.positionalValue(fakeBoard) + testRook.materialValue());
    }

    @Test
    public void constructor_valuesCheckmate() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        King fakeKing = mock(King.class);
        when(fakeKing.threatened(fakeBoard)).thenReturn(true);
        when(fakeKing.position()).thenReturn(new Point(0, 0));
        when(fakeKing.validMoves(fakeBoard)).thenReturn(new LinkedList<>());
        when(fakeBoard.findKing(any(Color.class))).thenReturn(fakeKing);

        assertTrue(new PositionAnalysis(fakeBoard).value == PositionAnalysis.VALUE_CHECKMATE);
    }
}
