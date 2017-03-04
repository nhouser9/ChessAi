/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Rook;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
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

        Square fakeEmpty = mock(Square.class);
        when(fakeEmpty.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeEmpty);

        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(testRook);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare);

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

    @Test
    public void constructor_doesNotDetectCheckmate_validMovesExist() {
        Board fakeBoard = mock(Board.class);

        Square fakeEmpty = mock(Square.class);
        when(fakeEmpty.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeEmpty);

        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);

        King fakeKing = mock(King.class);
        when(fakeKing.threatened(fakeBoard)).thenReturn(true);
        when(fakeKing.position()).thenReturn(new Point(0, 0));
        when(fakeKing.validMoves(fakeBoard)).thenReturn(new LinkedList<>());
        when(fakeBoard.findKing(any(Color.class))).thenReturn(fakeKing);

        List<GenericMove> validMoves = new LinkedList<>();
        validMoves.add(MoveFactory.create(fakeBoard, fakeKing, 0, 0));
        when(fakeBoard.validMoves()).thenReturn(validMoves);

        assertTrue(new PositionAnalysis(fakeBoard).value != PositionAnalysis.VALUE_CHECKMATE);
    }
}
