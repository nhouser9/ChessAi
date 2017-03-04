/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.notation;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.MockBoard;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Queen;
import chess.chessboard.pieces.StubPiece;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoryTest {

    @Test
    public void last_returnsTheLastMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);

        GenericMove move1 = testBoard.validMoves().get(3);
        move1.execute();
        assertTrue(testBoard.history().last() == move1);

        GenericMove move2 = testBoard.validMoves().get(5);
        move2.execute();
        assertTrue(testBoard.history().last() == move2);
    }

    @Test
    public void undo_removesTheLastMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);

        GenericMove move = testBoard.validMoves().get(3);
        move.execute();
        move.revert();

        assertTrue(testBoard.history().last() == null);
    }

    @Test
    public void last_returnsNull_initially() {
        History test = new History();
        assertTrue(test.last() == null);
    }

    @Test
    public void fullMoves_increments_afterBlackMove() {
        History test = new History();
        GenericMove fakeMove = mock(GenericMove.class);
        when(fakeMove.getPiece()).thenReturn(new StubPiece(Color.BLACK, 0, 0));
        test.add(fakeMove);
        assertTrue(test.fullMoveClock() == 2);
    }

    @Test
    public void fullMoves_doesNotIncrement_afterWhiteMove() {
        History test = new History();
        GenericMove fakeMove = mock(GenericMove.class);
        when(fakeMove.getPiece()).thenReturn(new StubPiece(Color.WHITE, 0, 0));
        test.add(fakeMove);
        assertTrue(test.fullMoveClock() == 1);
    }

    @Test
    public void fullMoves_startsAtOne() {
        History test = new History();
        assertTrue(test.fullMoveClock() == 1);
    }

    @Test
    public void fullMoves_decrements_afterBlackMoveReversion() {
        History test = new History();
        GenericMove fakeMove = mock(GenericMove.class);
        when(fakeMove.getPiece()).thenReturn(new StubPiece(Color.BLACK, 0, 0));
        test.add(fakeMove);
        test.undo();
        assertTrue(test.fullMoveClock() == 1);
    }

    @Test
    public void fullMoves_doesNotDecrement_afterWhiteMoveReversion() {
        History test = new History();
        GenericMove fakeMove = mock(GenericMove.class);
        when(fakeMove.getPiece()).thenReturn(new StubPiece(Color.WHITE, 0, 0));
        test.add(fakeMove);
        test.undo();
        assertTrue(test.fullMoveClock() == 1);
    }

    @Test
    public void halfMoveClock_startsAtZero() {
        History test = new History();
        assertTrue(test.halfMoveClock() == 0);
    }

    @Test
    public void halfMoveClock_increments_afterANonCaptureNonPawnMove() {
        History test = new History();
        Piece mover = new Queen(Color.BLACK, 0, 0);
        Board fakeBoard = MockBoard.create();
        GenericMove move = MoveFactory.create(fakeBoard, mover, 1, 1);
        test.add(move);
        assertTrue(test.halfMoveClock() == 1);
    }

    @Test
    public void halfMoveClock_doesNotIncrement_afterPawnMove() {
        History test = new History();
        Piece mover = new Pawn(Color.BLACK, 0, 1);
        Board fakeBoard = MockBoard.create();
        GenericMove move = MoveFactory.create(fakeBoard, mover, 0, 2);
        test.add(move);
        assertTrue(test.halfMoveClock() == 0);
    }

    @Test
    public void halfMoveClock_resets_afterPawnMove() {
        History test = new History();

        Piece mover = new Queen(Color.BLACK, 0, 0);
        Board fakeBoard = MockBoard.create();
        GenericMove move = MoveFactory.create(fakeBoard, mover, 1, 1);
        test.add(move);

        mover = new Pawn(Color.BLACK, 0, 1);
        move = MoveFactory.create(fakeBoard, mover, 0, 2);
        test.add(move);

        assertTrue(test.halfMoveClock() == 0);
    }

    @Test
    public void halfMoveClock_doesNotIncrement_afterCapture() {
        History test = new History();
        Piece mover = new Queen(Color.BLACK, 0, 0);
        Board fakeBoard = MockBoard.create();
        Square occupied = mock(Square.class);
        when(occupied.occupant()).thenReturn(new Pawn(Color.WHITE, 1, 1));
        when(fakeBoard.square(1, 1)).thenReturn(occupied);
        GenericMove move = MoveFactory.create(fakeBoard, mover, 1, 1);
        test.add(move);
        assertTrue(test.halfMoveClock() == 0);
    }
}
