/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.pieces.Pawn;
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
        when(fakeBoard.occupant(any(int.class), any(int.class)))
                .thenReturn(new Pawn(Color.WHITE, 0, 0))
                .thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);

        assertTrue(test.value == 1.0);
    }

    @Test
    public void constructor_setsWorstCaseValueToZero() {
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class)))
                .thenReturn(new Pawn(Color.WHITE, 0, 0))
                .thenReturn(null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);

        assertTrue(null == test.worstCaseValue());
    }
}
