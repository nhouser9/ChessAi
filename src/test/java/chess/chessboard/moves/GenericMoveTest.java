/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.pieces.FakePiece;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GenericMoveTest {

    @Test
    public void equals_returnsFalse_whenPassedAnObjectOfWrongType() {
        Piece fakeMover = mock(Piece.class);
        when(fakeMover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new FakeGenericMove(null, fakeMover, null);
        assertFalse(test.equals(fakeMover));
    }

    @Test
    public void equals_returnsFalse_whenPassedDifferentPieces() {
        Piece mover = new Pawn(Color.BLACK, 0, 0);
        Piece notEqual = new Pawn(Color.WHITE, 0, 0);
        Point moveTo = new Point(1, 1);
        when(mover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new FakeGenericMove(null, mover, moveTo);
        GenericMove testEquals = new FakeGenericMove(null, notEqual, moveTo);
        assertFalse(test.equals(testEquals));
    }

    @Test
    public void revert_returnsFalse_whenNotEqualToLastMove() {
        Board fakeBoard = mock(Board.class);
        Piece mover = mock(Piece.class);
        GenericMove fakeLastMove = new FakeGenericMove(fakeBoard, mover, new Point(0, 0));
        when(fakeBoard.lastMove()).thenReturn(fakeLastMove);

        GenericMove test = new FakeGenericMove(fakeBoard, mover, new Point(1, 1));
        assertFalse(test.revert());
    }

    @Test
    public void execute_executesOnThePassedBoard_whenPassedABoard() {
        Board original = mock(Board.class);
        Board argument = mock(Board.class);

        Piece fakeMover = new FakePiece(Color.WHITE, 0, 0);
        GenericMove test = new FakeGenericMove(original, fakeMover, new Point(5, 5));

        List<GenericMove> fakeValidMoves = new LinkedList<>();
        fakeValidMoves.add(test);
        when(original.validMoves()).thenReturn(fakeValidMoves);
        when(argument.validMoves()).thenReturn(fakeValidMoves);
        when(original.occupant(0, 0)).thenReturn(fakeMover);
        when(argument.occupant(0, 0)).thenReturn(fakeMover);

        test.changeBoard(argument);
        test.execute();

        verify(argument).setOccupant(any(int.class), any(int.class), any(Piece.class));
        verify(original, times(0)).setOccupant(any(int.class), any(int.class), any(Piece.class));
    }

    @Test
    public void revert_revertsOnThePassedBoard_afterExecutePassedABoard() {
        Board original = mock(Board.class);
        Board argument = mock(Board.class);

        Piece fakeMover = new FakePiece(Color.WHITE, 0, 0);
        GenericMove test = new FakeGenericMove(original, fakeMover, new Point(5, 5));

        List<GenericMove> fakeValidMoves = new LinkedList<>();
        fakeValidMoves.add(test);
        when(original.validMoves()).thenReturn(fakeValidMoves);
        when(argument.validMoves()).thenReturn(fakeValidMoves);
        when(original.occupant(0, 0)).thenReturn(fakeMover);
        when(argument.occupant(0, 0)).thenReturn(fakeMover);

        test.changeBoard(argument);
        test.execute();
        test.revert();

        verify(argument).setOccupant(any(int.class), any(int.class), any(Piece.class));
        verify(original, times(0)).setOccupant(any(int.class), any(int.class), any(Piece.class));
    }
}
