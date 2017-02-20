/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PieceTest {

    @Test
    public void constructor_storesPassedColor() {
        Color initialColor = Color.BLACK;
        Piece testPiece = new FakePiece(initialColor, 1, 1);
        assertEquals(initialColor, testPiece.color);
    }

    @Test
    public void constructor_initializesPosition() {
        int testInitialX = 3;
        int testInitialY = 5;
        Piece testPiece = new FakePiece(Color.WHITE, testInitialX, testInitialY);
        assertEquals(testPiece.position(), new Point(testInitialX, testInitialY));
    }

    @Test
    public void addDirectionalMoves_addsAllMovesInPassedDirection_whenNoBlockersExist() {
        List<Point> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        Piece testPiece = new FakePiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            int col = initialCol + dir.x();
            int row = initialRow + dir.y();
            while (Board.inBounds(col, row)) {
                assertTrue(potentialMoves.contains(new Point(col, row)));
                col = col + dir.x();
                row = row + dir.y();
            }
        }
    }

    @Test
    public void addDirectionalMoves_doesNotAddCurrentLocation() {
        List<Point> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        Piece testPiece = new FakePiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        potentialMoves.clear();
        testPiece.addDirectionalMoves(potentialMoves, fakeBoard, Direction.NORTH);

        assertFalse(potentialMoves.contains(new Point(initialCol, initialRow)));
    }

    @Test
    public void addDirectionalMoves_doesNotAddMovesPastBlocker_whenOneExists() {
        List<Point> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new FakePiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new FakePiece(Color.BLACK, blockerX, blockerY);
            when(fakeBoard.occupant(blockerX, blockerY)).thenReturn(testBlocker);
            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            int col = initialCol + dir.x();
            int row = initialRow + dir.y();
            for (int squaresAway = 1; squaresAway < blockerOffset; squaresAway++) {
                assertTrue(potentialMoves.contains(new Point(col, row)));
                col = col + dir.x();
                row = row + dir.y();
            }
            col = col + dir.x();
            row = row + dir.y();
            while (Board.inBounds(col, row)) {
                assertFalse(potentialMoves.contains(new Point(col, row)));
                col = col + dir.x();
                row = row + dir.y();
            }
        }
    }

    @Test
    public void addDirectionalMoves_allowsCollision_whenBlockerIsEnemy() {
        List<Point> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new FakePiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new FakePiece(Color.WHITE, blockerX, blockerY);
            when(fakeBoard.occupant(blockerX, blockerY)).thenReturn(testBlocker);
            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            assertTrue(potentialMoves.contains(new Point(blockerX, blockerY)));
        }
    }

    @Test
    public void addDirectionalMoves_preventsCollision_whenBlockerIsNotEnemy() {
        List<Point> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new FakePiece(Color.WHITE, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new FakePiece(Color.WHITE, blockerX, blockerY);
            when(fakeBoard.occupant(blockerX, blockerY)).thenReturn(testBlocker);
            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            assertFalse(potentialMoves.contains(new Point(blockerX, blockerY)));
        }
    }

    @Test
    public void addIfValid_doesNotAdd_whenTargetOutOfBounds() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE, 0);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, true, true);
        assertFalse(potentialMoves.contains(target));
    }

    @Test
    public void addIfValid_doesNotAdd_whenTargetOccupiedByFriend() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE, 0);
        Piece blocker = new FakePiece(Color.BLACK, target.x, target.y);
        when(fakeBoard.occupant(target.x, target.y)).thenReturn(blocker);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, true, true);
        assertFalse(potentialMoves.contains(target));
    }

    @Test
    public void addIfValid_addsEmptyTarget_whenPassedEmptyFlag() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE - 1, 0);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, true, false);
        assertTrue(potentialMoves.contains(target));
    }

    @Test
    public void addIfValid_doesNotAddEmptyTarget_whenNotPassedEmptyFlag() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE - 1, 0);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, false, false);
        assertFalse(potentialMoves.contains(target));
    }

    @Test
    public void addIfValid_addsEnemyTarget_whenPassedEnemyFlag() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(0, 0);
        Piece testEnemy = new FakePiece(Color.WHITE, target.x, target.y);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(testEnemy);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, false, true);
        assertTrue(potentialMoves.contains(target));
    }

    @Test
    public void addIfValid_doesNotAddEnemyTarget_whenNotPassedEnemyFlag() {
        List<Point> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new FakePiece(Color.BLACK, 4, 4);
        Point target = new Point(0, 0);
        Piece testEnemy = new FakePiece(Color.WHITE, target.x, target.y);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(testEnemy);
        testPiece.addIfValid(potentialMoves, fakeBoard, target, false, false);
        assertFalse(potentialMoves.contains(target));
    }

    @Test
    public void hasMoved_returnsFalseInitially() {
        Piece testPiece = new FakePiece(Color.WHITE, 0, 0);
        assertFalse(testPiece.hasMoved());
    }

    @Test
    public void hasMoved_returnsTrue_afterMoved() {
        Piece testPiece = new FakePiece(Color.WHITE, 0, 0);
        testPiece.addMoveCount();
        assertTrue(testPiece.hasMoved());
    }

    @Test
    public void equals_returnsFalse_whenClassesDoNotMatch() {
        Piece testPiece = new FakePiece(Color.BLACK, 0, 0);
        String testOtherClass = "test";
        assertFalse(testPiece.equals(testOtherClass));
    }

    @Test
    public void equals_returnsFalse_whenMovedStatusDoesNotMatch() {
        Piece testPiece = new FakePiece(Color.BLACK, 0, 0);
        Piece testOther = new FakePiece(Color.BLACK, 0, 0);
        testOther.addMoveCount();
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsFalse_whenColorDoesNotMatch() {
        Piece testPiece = new FakePiece(Color.BLACK, 0, 0);
        Piece testOther = new FakePiece(Color.WHITE, 0, 0);
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsFalse_whenPositionDoesNotMatch() {
        Piece testPiece = new FakePiece(Color.BLACK, 0, 0);
        Piece testOther = new FakePiece(Color.BLACK, 1, 0);
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsTrue_forIdenticalObjects() {
        Piece testPiece = new FakePiece(Color.BLACK, 0, 0);
        Piece testOther = new FakePiece(Color.BLACK, 0, 0);
        assertTrue(testPiece.equals(testOther));
    }
}
