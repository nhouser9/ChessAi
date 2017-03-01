/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.ai.AnalysisNode;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.pieces.Pawn;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalysisNodeTest {

    @Test
    public void addChild_addsAChild() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        assertTrue(test.children().contains(child));
    }

    @Test
    public void addChild_setsParentOfChild() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        assertTrue(child.parent() == test);
    }

    @Test
    public void removeChild_removesAChild() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        test.removeChild(child);

        assertFalse(test.children().contains(child));
    }

    @Test
    public void constructor_setsValueToPositionValue() {
        Board fakeBoard = mock(Board.class);
        Pawn testPawn = new Pawn(Color.WHITE, 0, 0);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(0, 0)).thenReturn(testPawn);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        test.resetWorstCaseValue();

        assertTrue(test.worstCaseValue() == testPawn.materialValue() + testPawn.positionalValue(fakeBoard));
    }

    @Test
    public void constructor_setsWorstCaseChildToNull() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class)))
                .thenReturn(new Pawn(Color.WHITE, 0, 0))
                .thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);

        assertTrue(null == test.worstCaseChild());
    }

    @Test
    public void setWorstCaseChild_setsTheChild_whenChildIsNull() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        double expected = 1.1;

        test.setWorstCaseValue(expected);
        assertTrue(test.worstCaseValue() == expected);
    }

    @Test
    public void setWorstCaseChild_setsTheChild_whenOldChildIsWorse() {
        Color moveColor = Color.BLACK;

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        double worse = -2.5;
        double expected = -23.22;

        test.setWorstCaseValue(worse);
        test.setWorstCaseValue(expected);
        assertTrue(test.worstCaseValue() == expected);
    }

    @Test
    public void setWorstCaseChild_doesNotSetTheChild_whenOldChildIsBetter() {
        Color moveColor = Color.WHITE;

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        double expected = 43.1;
        double worse = -2.22;

        test.setWorstCaseValue(expected);
        test.setWorstCaseValue(worse);
        assertTrue(test.worstCaseValue() == expected);
    }
}
