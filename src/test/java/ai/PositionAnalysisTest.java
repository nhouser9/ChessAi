/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.pieces.Bishop;
import chessboard.pieces.King;
import chessboard.pieces.Knight;
import chessboard.pieces.Pawn;
import chessboard.pieces.Queen;
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
    public void constructor_valuesPawns() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(new Pawn(Color.BLACK, 0, 0));

        assertTrue(new PositionAnalysis(fakeBoard).value == 0 - PositionAnalysis.VALUE_PAWN);
    }

    @Test
    public void constructor_valuesRooks() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(new Rook(Color.WHITE, 0, 0));

        assertTrue(new PositionAnalysis(fakeBoard).value == PositionAnalysis.VALUE_ROOK);
    }

    @Test
    public void constructor_valuesQueens() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(new Queen(Color.BLACK, 0, 0));
        when(fakeBoard.occupant(0, 1)).thenReturn(new Queen(Color.BLACK, 0, 1));

        assertTrue(new PositionAnalysis(fakeBoard).value == 2 * (0 - PositionAnalysis.VALUE_QUEEN));
    }

    @Test
    public void constructor_valuesKnights() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(new Knight(Color.WHITE, 0, 0));

        assertTrue(new PositionAnalysis(fakeBoard).value == PositionAnalysis.VALUE_KNIGHT);
    }

    @Test
    public void constructor_valuesBishops() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(new Bishop(Color.BLACK, 0, 0));

        assertTrue(new PositionAnalysis(fakeBoard).value == 0 - PositionAnalysis.VALUE_BISHOP);
    }

    @Test
    public void constructor_valuesCheckmate() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.getActivePlayer()).thenReturn(Color.BLACK);
        when(fakeBoard.kingThreatened(any(King.class))).thenReturn(true);
        King fakeKing = mock(King.class);
        when(fakeKing.position()).thenReturn(new Point(0, 0));
        when(fakeKing.validMoves(fakeBoard)).thenReturn(new LinkedList<>());
        when(fakeBoard.findKing(any(Color.class))).thenReturn(fakeKing);

        assertTrue(new PositionAnalysis(fakeBoard).value == PositionAnalysis.VALUE_CHECKMATE);
    }
}
