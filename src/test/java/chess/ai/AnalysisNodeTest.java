/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.ai.AnalysisNode;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
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
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        assertTrue(test.children().contains(child));
    }

    @Test
    public void addChild_setsParentOfChild() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        assertTrue(child.parent() == test);
    }

    @Test
    public void removeChild_removesAChild() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.addChild(child);
        test.removeChild(child);

        assertFalse(test.children().contains(child));
    }

    @Test
    public void worstCaseValue_returnsThePositionValue_whenNoChildrenExist() {
        Board fakeBoard = mock(Board.class);
        Pawn testPawn = new Pawn(Color.WHITE, 0, 0);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Square fakeOccupied = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeOccupied);
        when(fakeOccupied.occupant()).thenReturn(testPawn);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);

        assertTrue(test.worstCaseValue() == testPawn.materialValue() + testPawn.positionalValue(fakeBoard));
    }

    @Test
    public void constructor_setsWorstCaseChildToNull() {
        Board fakeBoard = mock(Board.class);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(new Pawn(Color.WHITE, 0, 0)).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeOccupied);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);

        assertTrue(null == test.worstCaseChild());
    }

    @Test
    public void setWorstCaseChild_setsTheChild_whenChildIsNull() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        AnalysisNode child = new AnalysisNode(fakeBoard, null);

        test.setWorstCaseChild(child);
        assertTrue(test.worstCaseChild() == child);
    }

    @Test
    public void setWorstCaseChild_setsTheChild_whenOldChildIsWorse() {
        Color moveColor = Color.BLACK;

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(new Pawn(moveColor, 0, 0)).thenReturn(null);
        when(fakeBoard.square(0, 0)).thenReturn(fakeOccupied);

        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode worst = new AnalysisNode(fakeBoard, null);
        AnalysisNode best = new AnalysisNode(fakeBoard, null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        test.setWorstCaseChild(worst);
        test.setWorstCaseChild(best);
        assertTrue(test.worstCaseChild() == best);
    }

    @Test
    public void setWorstCaseChild_doesNotSetTheChild_whenOldChildIsBetter() {
        Color moveColor = Color.WHITE;

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(new Pawn(moveColor, 0, 0)).thenReturn(null);
        when(fakeBoard.square(0, 0)).thenReturn(fakeOccupied);

        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode worst = new AnalysisNode(fakeBoard, null);
        AnalysisNode best = new AnalysisNode(fakeBoard, null);

        AnalysisNode test = new AnalysisNode(fakeBoard, null);
        test.setWorstCaseChild(best);
        test.setWorstCaseChild(worst);
        assertTrue(test.worstCaseChild() == best);
    }

    @Test
    public void betterThan_returnsFalse_whenArgumentIsBetter() {
        Color moveColor = Color.WHITE;

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(new Pawn(moveColor, 0, 0)).thenReturn(null);
        when(fakeBoard.square(0, 0)).thenReturn(fakeOccupied);

        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode worst = new AnalysisNode(fakeBoard, null);
        AnalysisNode best = new AnalysisNode(fakeBoard, null);

        assertFalse(worst.betterThan(best));
    }

    @Test
    public void betterThan_returnsTrue_whenArgumentIsWorse() {
        Color moveColor = Color.BLACK;

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(new Pawn(moveColor, 0, 0)).thenReturn(null);
        when(fakeBoard.square(0, 0)).thenReturn(fakeOccupied);

        when(fakeBoard.activePlayer()).thenReturn(moveColor);

        AnalysisNode worst = new AnalysisNode(fakeBoard, null);
        AnalysisNode best = new AnalysisNode(fakeBoard, null);

        assertTrue(best.betterThan(worst));
    }
}
