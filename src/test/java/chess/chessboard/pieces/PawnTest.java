/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.awt.Point;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PawnTest {

    @Test
    public void validMoves_allowsSingleNorthMoves_forWhitePawns() {
        int initialX = 3;
        int initialY = 4;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.NORTH.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove));
    }

    @Test
    public void validMoves_allowsSingleSouthMoves_forBlackPawns() {
        int initialX = 3;
        int initialY = 4;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.SOUTH.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsSingleMoveForward_whenBlocked() {
        int initialX = 3;
        int initialY = 4;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);
        Direction moveDir = Direction.NORTH;
        Piece blocker = new FakePiece(Color.BLACK, initialX, initialY + moveDir.y());
        Point target = new Point(initialX, initialY + moveDir.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(initialX, initialY + moveDir.y())).thenReturn(blocker);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsBackwardMoves() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.NORTH.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsMovesOffColumn_whenNothingToCaptureExists() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
            GenericMove testMove1 = MoveFactory.create(fakeBoard, testPawn, initialX + 1, row);
            GenericMove testMove2 = MoveFactory.create(fakeBoard, testPawn, initialX - 1, row);
            assertFalse(moves.contains(testMove1));
            assertFalse(moves.contains(testMove2));
        }
    }

    @Test
    public void validMoves_allowsDiagonalCapture() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        int enemyEastX = initialX + Direction.NORTHEAST.x();
        int enemyEastY = initialY + Direction.NORTHEAST.y();
        Piece enemyEast = new FakePiece(Color.BLACK, enemyEastX, enemyEastY);

        int enemyWestX = initialX + Direction.NORTHWEST.x();
        int enemyWestY = initialY + Direction.NORTHWEST.y();
        Piece enemyWest = new FakePiece(Color.BLACK, enemyWestX, enemyWestY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(enemyEastX, enemyEastY)).thenReturn(enemyEast);
        when(fakeBoard.occupant(enemyWestX, enemyWestY)).thenReturn(enemyWest);

        GenericMove testMove1 = MoveFactory.create(fakeBoard, testPawn, enemyEastX, enemyEastY);
        GenericMove testMove2 = MoveFactory.create(fakeBoard, testPawn, enemyEastX, enemyEastY);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove1));
        assertTrue(moves.contains(testMove2));
    }

    @Test
    public void validMoves_preventsNonDiagonalCapture() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);
        int enemyX = initialX + 2;
        int enemyY = initialY + Direction.NORTHEAST.y();
        Piece enemy = new FakePiece(Color.WHITE, enemyX, enemyY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(enemyX, enemyY)).thenReturn(enemy);

        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
            GenericMove testMove1 = MoveFactory.create(fakeBoard, testPawn, initialX + 1, row);
            GenericMove testMove2 = MoveFactory.create(fakeBoard, testPawn, initialX - 1, row);
            assertFalse(moves.contains(testMove1));
            assertFalse(moves.contains(testMove2));
        }
    }

    @Test
    public void validMoves_allowsDoubleMoveForward_whenThisHasNotMoved() {
        int initialX = 4;
        int initialY = Board.SQUARES_PER_SIDE - 2;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, initialX, initialY + (2 * Direction.NORTH.y()));
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsDoubleMoveForward_whenThisHasMoved() {
        int initialX = 4;
        int initialY = Board.SQUARES_PER_SIDE - 3;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);
        Point target = new Point(initialX, initialY + (2 * Direction.NORTH.y()));

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsMoveOffBoard() {
        Pawn testPawn = new Pawn(Color.WHITE, 0, 0);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        assertTrue(testPawn.validMoves(fakeBoard).isEmpty());
    }

    @Test
    public void validMoves_allowsEnPassant() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 4);
        Pawn testCapture = new Pawn(Color.WHITE, 2, 6);

        Board fakeBoard = mock(Board.class);
        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.lastMove()).thenReturn(fakeHistory);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, 2, 5);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsEnPassant_whenTargetLastMoveWasCapture() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 4);
        Pawn testCapture = new Pawn(Color.WHITE, 3, 5);

        Board fakeBoard = mock(Board.class);
        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.lastMove()).thenReturn(fakeHistory);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, 2, 5);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsDoubleMove_whenBlocked() {
        int initialX = 6;
        int initialY = Board.SQUARES_PER_SIDE - 2;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);
        Point target = new Point(initialX, initialY + (2 * Direction.NORTH.y()));

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(initialX, initialY - 1)).thenReturn(mock(Piece.class));

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsEnPassant_whenNotOnCorrectRank() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 3);
        Pawn testCapture = new Pawn(Color.WHITE, 2, 6);

        Board fakeBoard = mock(Board.class);
        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.lastMove()).thenReturn(fakeHistory);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, 2, 5);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }
}
