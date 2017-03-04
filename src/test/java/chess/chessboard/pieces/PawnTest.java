/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.MockBoard;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.notation.History;
import java.awt.Point;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class PawnTest {

    @Test
    public void validMoves_allowsSingleNorthMoves_forWhitePawns() {
        int initialX = 3;
        int initialY = 4;
        Pawn testPawn = new Pawn(Color.WHITE, initialX, initialY);
        Point target = new Point(initialX, initialY + Direction.NORTH.y());

        Board fakeBoard = MockBoard.create();

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

        Board fakeBoard = MockBoard.create();

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
        Piece blocker = new StubPiece(Color.BLACK, initialX, initialY + moveDir.y());
        Point target = new Point(initialX, initialY + moveDir.y());

        Board fakeBoard = MockBoard.create();

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(blocker);
        when(fakeBoard.square(initialX, initialY + moveDir.y())).thenReturn(fakeOccupied);

        when(fakeBoard.history()).thenReturn(mock(History.class));

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

        Board fakeBoard = MockBoard.create();

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsMovesOffColumn_whenNothingToCaptureExists() {
        int initialX = 2;
        int initialY = 6;
        Pawn testPawn = new Pawn(Color.BLACK, initialX, initialY);

        Board fakeBoard = MockBoard.create();

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
        Piece enemyEast = new StubPiece(Color.BLACK, enemyEastX, enemyEastY);

        int enemyWestX = initialX + Direction.NORTHWEST.x();
        int enemyWestY = initialY + Direction.NORTHWEST.y();
        Piece enemyWest = new StubPiece(Color.BLACK, enemyWestX, enemyWestY);

        Board fakeBoard = MockBoard.create();

        Square fakeOccupied1 = mock(Square.class);
        when(fakeOccupied1.occupant()).thenReturn(enemyEast);
        when(fakeBoard.square(enemyEastX, enemyEastY)).thenReturn(fakeOccupied1);
        Square fakeOccupied2 = mock(Square.class);
        when(fakeOccupied2.occupant()).thenReturn(enemyWest);
        when(fakeBoard.square(enemyWestX, enemyWestY)).thenReturn(fakeOccupied2);

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
        Piece enemy = new StubPiece(Color.WHITE, enemyX, enemyY);

        Board fakeBoard = MockBoard.create();

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(enemy);
        when(fakeBoard.square(enemyX, enemyY)).thenReturn(fakeOccupied);

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

        Board fakeBoard = MockBoard.create();

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

        Board fakeBoard = MockBoard.create();

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsMoveOffBoard() {
        Pawn testPawn = new Pawn(Color.WHITE, 0, 0);

        Board fakeBoard = MockBoard.create();

        assertTrue(testPawn.validMoves(fakeBoard).isEmpty());
    }

    @Test
    public void validMoves_allowsEnPassant() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 4);
        Pawn testCapture = new Pawn(Color.WHITE, 2, 6);

        Board fakeBoard = MockBoard.create();

        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.history().last()).thenReturn(fakeHistory);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, 2, 5);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertTrue(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsEnPassant_whenTargetLastMoveWasCapture() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 4);
        Pawn testCapture = new Pawn(Color.WHITE, 3, 5);

        Board fakeBoard = MockBoard.create();

        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.history().last()).thenReturn(fakeHistory);

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

        Board fakeBoard = MockBoard.create();

        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(mock(Piece.class));
        when(fakeBoard.square(initialX, initialY - 1)).thenReturn(fakeOccupied);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, target.x, target.y);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void validMoves_preventsEnPassant_whenNotOnCorrectRank() {
        Pawn testPawn = new Pawn(Color.BLACK, 1, 3);
        Pawn testCapture = new Pawn(Color.WHITE, 2, 6);

        Board fakeBoard = MockBoard.create();

        Point captureMoveTo = new Point(2, 4);
        GenericMove fakeHistory = MoveFactory.create(fakeBoard, testCapture, captureMoveTo.x, captureMoveTo.y);
        testCapture.setPosition(captureMoveTo);
        when(fakeBoard.history().last()).thenReturn(fakeHistory);

        GenericMove testMove = MoveFactory.create(fakeBoard, testPawn, 2, 5);
        List<GenericMove> moves = testPawn.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }
}
