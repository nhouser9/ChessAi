/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.MockBoard;
import chess.chessboard.Square;
import chess.chessboard.notation.History;
import chess.chessboard.pieces.StubPiece;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GenericMoveTest {

    @Test
    public void equals_returnsFalse_whenPassedAnObjectOfWrongType() {
        Piece fakeMover = mock(Piece.class);
        when(fakeMover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new StubGenericMove(null, fakeMover, null);
        assertFalse(test.equals(fakeMover));
    }

    @Test
    public void equals_returnsFalse_whenPassedDifferentPieces() {
        Piece mover = new Pawn(Color.BLACK, 0, 0);
        Piece notEqual = new Pawn(Color.WHITE, 0, 0);
        Point moveTo = new Point(1, 1);
        when(mover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new StubGenericMove(null, mover, moveTo);
        GenericMove testEquals = new StubGenericMove(null, notEqual, moveTo);
        assertFalse(test.equals(testEquals));
    }

    @Test
    public void revert_returnsFalse_whenNotEqualToLastMove() {
        Board fakeBoard = mock(Board.class);
        Piece mover = mock(Piece.class);
        GenericMove fakeLastMove = new StubGenericMove(fakeBoard, mover, new Point(0, 0));

        History fakeHistory = mock(History.class);
        when(fakeHistory.last()).thenReturn(fakeLastMove);
        when(fakeBoard.history()).thenReturn(fakeHistory);

        GenericMove test = new StubGenericMove(fakeBoard, mover, new Point(1, 1));
        assertFalse(test.revert());
    }

    @Test
    public void execute_executesOnThePassedBoard_whenPassedABoard() {
        Board original = MockBoard.create();
        Board argument = MockBoard.create();

        Piece fakeMover = new StubPiece(Color.WHITE, 0, 0);
        GenericMove test = new StubGenericMove(original, fakeMover, new Point(5, 5));

        List<GenericMove> fakeValidMoves = new LinkedList<>();
        fakeValidMoves.add(test);
        when(original.validMoves()).thenReturn(fakeValidMoves);
        when(argument.validMoves()).thenReturn(fakeValidMoves);

        Square fakeOccupiedOriginal = mock(Square.class);
        when(fakeOccupiedOriginal.occupant()).thenReturn(fakeMover);
        when(original.square(0, 0)).thenReturn(fakeOccupiedOriginal);

        Square fakeOccupiedArgument = mock(Square.class);
        when(fakeOccupiedArgument.occupant()).thenReturn(fakeMover);
        when(argument.square(0, 0)).thenReturn(fakeOccupiedArgument);

        test.changeBoard(argument);
        test.execute();

        verify(fakeOccupiedArgument).setOccupant(null);
        verify(fakeOccupiedOriginal, times(0)).setOccupant(any(Piece.class));
        verify(fakeOccupiedOriginal, times(0)).setOccupant(null);
    }

    @Test
    public void revert_revertsOnThePassedBoard_afterExecutePassedABoard() {
        Board original = MockBoard.create();
        Board argument = MockBoard.create();

        Piece fakeMover = new StubPiece(Color.WHITE, 0, 0);
        GenericMove test = new StubGenericMove(original, fakeMover, new Point(5, 5));

        List<GenericMove> fakeValidMoves = new LinkedList<>();
        fakeValidMoves.add(test);
        when(original.validMoves()).thenReturn(fakeValidMoves);
        when(argument.validMoves()).thenReturn(fakeValidMoves);

        Square fakeOccupiedOriginal = mock(Square.class);
        when(fakeOccupiedOriginal.occupant()).thenReturn(fakeMover);
        when(original.square(0, 0)).thenReturn(fakeOccupiedOriginal);

        Square fakeOccupiedArgument = mock(Square.class);
        when(fakeOccupiedArgument.occupant()).thenReturn(fakeMover);
        when(argument.square(0, 0)).thenReturn(fakeOccupiedArgument);

        test.changeBoard(argument);
        test.execute();
        test.revert();

        verify(fakeOccupiedArgument).setOccupant(null);
        verify(fakeOccupiedOriginal, times(0)).setOccupant(any(Piece.class));
        verify(fakeOccupiedOriginal, times(0)).setOccupant(null);
    }
}
