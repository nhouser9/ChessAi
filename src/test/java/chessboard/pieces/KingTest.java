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
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KingTest {

    @Test
    public void validMoves_preventsNonAdjacentMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testKing.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    assertFalse(moves.contains(new Point(col, row)));
                }
                if (Math.abs(deltaX) >= 2 && Math.abs(deltaY) >= 2) {
                    assertFalse(moves.contains(new Point(col, row)));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsAdjacentMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testKing.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }
                if (Math.abs(deltaX) < 2 && Math.abs(deltaY) < 2) {
                    assertTrue(moves.contains(new Point(col, row)));
                }
            }
        }
    }

    @Test
    public void validMoves_preventsCollisionWithNonEnemies() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);
        Direction blockerDir = Direction.EAST;
        int blockerX = testInitialX + blockerDir.x();
        int blockerY = testInitialY + blockerDir.y();
        Piece testBlocker = new FakePiece(Color.WHITE, blockerX, blockerY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(blockerX, blockerY)).thenReturn(testBlocker);

        List<Point> moves = testKing.validMoves(fakeBoard);
        assertFalse(moves.contains(new Point(blockerX, blockerY)));
    }

    @Test
    public void validMoves_allowsCastlingLeft_whenBoardAllows() {
        Point initialPoint = new Point(4, 0);
        King testKing = new King(Color.BLACK, initialPoint.x, initialPoint.y);
        Point target = new Point(initialPoint.x - 2, initialPoint.y);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.canCastle(any(Direction.class), any(Color.class))).thenReturn(true);

        List<Point> moves = testKing.validMoves(fakeBoard);
        assertTrue(moves.contains(target));
    }
}
