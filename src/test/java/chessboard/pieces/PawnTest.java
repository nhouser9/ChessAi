/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import chessboard.moves.Move;
import java.awt.Point;
import java.util.LinkedList;
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

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(target));
    }

    @Test
    public void validMoves_allowsSingleSouthMoves_forBlackPawns() {
        int initialX = 3;
        int initialY = 4;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.SOUTH.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(target));
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

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(target));
    }

    @Test
    public void validMoves_preventsBackwardMoves() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.NORTH.y());

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(target));
    }

    @Test
    public void validMoves_preventsMovesOffColumn_whenNothingToCaptureExists() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
            assertFalse(moves.contains(new Point(initialX + 1, row)));
            assertFalse(moves.contains(new Point(initialX - 1, row)));
        }
    }

    @Test
    public void validMoves_allowsDiagonalCapture() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        int enemyEastX = initialX + Direction.NORTHEAST.x();
        int enemyEasyY = initialY + Direction.NORTHEAST.y();
        Piece enemyEast = new FakePiece(Color.BLACK, enemyEastX, enemyEasyY);

        int enemyWestX = initialX + Direction.NORTHWEST.x();
        int enemyWestY = initialY + Direction.NORTHWEST.y();
        Piece enemyWest = new FakePiece(Color.BLACK, enemyWestX, enemyWestY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(enemyEastX, enemyEasyY)).thenReturn(enemyEast);
        when(fakeBoard.occupant(enemyWestX, enemyWestY)).thenReturn(enemyWest);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(new Point(enemyEastX, enemyEasyY)));
        assertTrue(moves.contains(new Point(enemyWestX, enemyWestY)));
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

        List<Point> moves = testPawn.validMoves(fakeBoard);
        for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
            assertFalse(moves.contains(new Point(initialX + 1, row)));
            assertFalse(moves.contains(new Point(initialX - 1, row)));
        }
    }

    @Test
    public void validMoves_allowsDoubleMoveForward_whenThisHasNotMoved() {
        int initialX = 4;
        int initialY = Board.SQUARES_PER_SIDE - 2;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(new Point(initialX, initialY + (2 * Direction.NORTH.y()))));
    }

    @Test
    public void validMoves_preventsDoubleMoveForward_whenThisHasMoved() {
        int initialX = 4;
        int initialY = Board.SQUARES_PER_SIDE - 3;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(new Point(initialX, initialY + (2 * Direction.NORTH.y()))));
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
        Move fakeHistory = new Move(fakeBoard, testCapture, captureMoveTo);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.lastMove()).thenReturn(fakeHistory);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(new Point(2, 5)));
    }

    @Test
    public void validMoves_preventsEnPassant_whenTargetLastMoveWasCapture() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 4);
        Pawn testCapture = new Pawn(Color.WHITE, 3, 5);

        Board fakeBoard = mock(Board.class);
        Point captureMoveTo = new Point(2, 4);
        Move fakeHistory = new Move(fakeBoard, testCapture, captureMoveTo);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.lastMove()).thenReturn(fakeHistory);

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(new Point(2, 5)));
    }

    @Test
    public void validMoves_preventsDoubleMove_whenBlocked() {
        int initialX = 6;
        int initialY = Board.SQUARES_PER_SIDE - 2;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(initialX, initialY - 1)).thenReturn(mock(Piece.class));

        List<Point> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(new Point(initialX, initialY + (2 * Direction.NORTH.y()))));
    }
}
