/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class PieceTest {

    @Test
    public void constructor_storesPassedColor() {
        Color initialColor = Color.BLACK;
        Piece testPiece = new StubPiece(initialColor, 1, 1);
        assertEquals(initialColor, testPiece.color);
    }

    @Test
    public void constructor_initializesPosition() {
        int testInitialX = 3;
        int testInitialY = 5;
        Piece testPiece = new StubPiece(Color.WHITE, testInitialX, testInitialY);
        assertEquals(testPiece.position(), new Point(testInitialX, testInitialY));
    }

    @Test
    public void addDirectionalMoves_addsAllMovesInPassedDirection_whenNoBlockersExist() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        Piece testPiece = new StubPiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            int col = initialCol + dir.x();
            int row = initialRow + dir.y();
            while (Board.inBounds(col, row)) {
                GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, col, row);
                assertTrue(potentialMoves.contains(testMove));
                col = col + dir.x();
                row = row + dir.y();
            }
        }
    }

    @Test
    public void addDirectionalMoves_doesNotAddCurrentLocation() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        Piece testPiece = new StubPiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        potentialMoves.clear();
        testPiece.addDirectionalMoves(potentialMoves, fakeBoard, Direction.NORTH);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, initialCol, initialRow);
        assertFalse(potentialMoves.contains(testMove));
    }

    @Test
    public void addDirectionalMoves_doesNotAddMovesPastBlocker_whenOneExists() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new StubPiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new StubPiece(Color.BLACK, blockerX, blockerY);

            Square fakeOccupied = mock(Square.class);
            when(fakeOccupied.occupant()).thenReturn(testBlocker);
            when(fakeBoard.square(blockerX, blockerY)).thenReturn(fakeOccupied);

            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            int col = initialCol + dir.x();
            int row = initialRow + dir.y();
            for (int squaresAway = 1; squaresAway < blockerOffset; squaresAway++) {
                GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, col, row);
                assertTrue(potentialMoves.contains(testMove));
                col = col + dir.x();
                row = row + dir.y();
            }
            col = col + dir.x();
            row = row + dir.y();
            while (Board.inBounds(col, row)) {
                GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, col, row);
                assertFalse(potentialMoves.contains(testMove));
                col = col + dir.x();
                row = row + dir.y();
            }
        }
    }

    @Test
    public void addDirectionalMoves_allowsCollision_whenBlockerIsEnemy() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new StubPiece(Color.BLACK, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new StubPiece(Color.WHITE, blockerX, blockerY);

            Square fakeOccupied = mock(Square.class);
            when(fakeOccupied.occupant()).thenReturn(testBlocker);
            when(fakeBoard.square(blockerX, blockerY)).thenReturn(fakeOccupied);

            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, blockerX, blockerY);
            assertTrue(potentialMoves.contains(testMove));
        }
    }

    @Test
    public void addDirectionalMoves_preventsCollision_whenBlockerIsNotEnemy() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        int initialCol = 4;
        int initialRow = 3;
        int blockerOffset = 2;
        Piece testPiece = new StubPiece(Color.WHITE, initialCol, initialRow);
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        for (Direction dir : Direction.values()) {
            potentialMoves.clear();
            int blockerX = initialCol + (blockerOffset * dir.x());
            int blockerY = initialRow + (blockerOffset * dir.y());
            Piece testBlocker = new StubPiece(Color.WHITE, blockerX, blockerY);

            Square fakeOccupied = mock(Square.class);
            when(fakeOccupied.occupant()).thenReturn(testBlocker);
            when(fakeBoard.square(blockerX, blockerY)).thenReturn(fakeOccupied);

            testPiece.addDirectionalMoves(potentialMoves, fakeBoard, dir);

            GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, blockerX, blockerY);
            assertFalse(potentialMoves.contains(testMove));
        }
    }

    @Test
    public void addIfValid_doesNotAdd_whenTargetOutOfBounds() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE, 0);
        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, true, true);
        assertFalse(potentialMoves.contains(testMove));
    }

    @Test
    public void addIfValid_doesNotAdd_whenTargetOccupiedByFriend() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE, 0);
        Piece blocker = new StubPiece(Color.BLACK, target.x, target.y);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(blocker);
        when(fakeBoard.square(target.x, target.y)).thenReturn(fakeOccupied);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, true, true);
        assertFalse(potentialMoves.contains(testMove));
    }

    @Test
    public void addIfValid_addsEmptyTarget_whenPassedEmptyFlag() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE - 1, 0);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, false, true);
        assertTrue(potentialMoves.contains(testMove));
    }

    @Test
    public void addIfValid_doesNotAddEmptyTarget_whenNotPassedEmptyFlag() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(Board.SQUARES_PER_SIDE - 1, 0);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, false, false);
        assertFalse(potentialMoves.contains(testMove));
    }

    @Test
    public void addIfValid_addsEnemyTarget_whenPassedEnemyFlag() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(0, 0);
        Piece testEnemy = new StubPiece(Color.WHITE, target.x, target.y);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(testEnemy);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeOccupied);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, true, false);
        assertTrue(potentialMoves.contains(testMove));
    }

    @Test
    public void addIfValid_doesNotAddEnemyTarget_whenNotPassedEnemyFlag() {
        List<GenericMove> potentialMoves = new ArrayList<>();
        Board fakeBoard = mock(Board.class);
        Piece testPiece = new StubPiece(Color.BLACK, 4, 4);
        Point target = new Point(0, 0);
        Piece testEnemy = new StubPiece(Color.WHITE, target.x, target.y);

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(testEnemy);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeOccupied);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPiece, target.x, target.y);
        MoveFactory.addIfValid(potentialMoves, fakeBoard, testPiece, target.x, target.y, false, false);
        assertFalse(potentialMoves.contains(testMove));
    }

    @Test
    public void hasMoved_returnsFalseInitially() {
        Piece testPiece = new StubPiece(Color.WHITE, 0, 0);
        assertFalse(testPiece.hasMoved());
    }

    @Test
    public void hasMoved_returnsTrue_afterMoved() {
        Piece testPiece = new StubPiece(Color.WHITE, 0, 0);
        testPiece.addMoveCount();
        assertTrue(testPiece.hasMoved());
    }

    @Test
    public void equals_returnsFalse_whenClassesDoNotMatch() {
        Piece testPiece = new StubPiece(Color.BLACK, 0, 0);
        String testOtherClass = "test";
        assertFalse(testPiece.equals(testOtherClass));
    }

    @Test
    public void equals_returnsFalse_whenMovedStatusDoesNotMatch() {
        Piece testPiece = new StubPiece(Color.BLACK, 0, 0);
        Piece testOther = new StubPiece(Color.BLACK, 0, 0);
        testOther.addMoveCount();
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsFalse_whenColorDoesNotMatch() {
        Piece testPiece = new StubPiece(Color.BLACK, 0, 0);
        Piece testOther = new StubPiece(Color.WHITE, 0, 0);
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsFalse_whenPositionDoesNotMatch() {
        Piece testPiece = new StubPiece(Color.BLACK, 0, 0);
        Piece testOther = new StubPiece(Color.BLACK, 1, 0);
        assertFalse(testPiece.equals(testOther));
    }

    @Test
    public void equals_returnsTrue_forIdenticalObjects() {
        Piece testPiece = new StubPiece(Color.BLACK, 0, 0);
        Piece testOther = new StubPiece(Color.BLACK, 0, 0);
        assertTrue(testPiece.equals(testOther));
    }

    @Test
    public void copyOf_copiesMovedStatus() {
        Piece testPiece = new Bishop(Color.BLACK, 0, 0);
        testPiece.addMoveCount();
        assertTrue(testPiece.copyOf().hasMoved());
    }
}
