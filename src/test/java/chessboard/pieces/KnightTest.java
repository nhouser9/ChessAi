/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.moves.GenericMove;
import chessboard.moves.MoveFactory;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KnightTest {

    @Test
    public void validMoves_preventsNonLShapedMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        Knight testKnight = new Knight(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testKnight.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = Math.abs(col - testInitialX);
                int deltaY = Math.abs(row - testInitialY);
                GenericMove testMove = MoveFactory.create(fakeBoard, testKnight, col, row);
                if ((deltaX != 2 || deltaY != 1) && (deltaX != 1 || deltaY != 2)) {
                    assertFalse(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsLShapedMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        Knight testKnight = new Knight(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testKnight.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = Math.abs(col - testInitialX);
                int deltaY = Math.abs(row - testInitialY);
                GenericMove testMove = MoveFactory.create(fakeBoard, testKnight, col, row);
                if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
                    assertTrue(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validModes_preventsCollisionWithNonEnemies() {
        int testInitialX = 5;
        int testInitialY = 6;
        Knight testKnight = new Knight(Color.WHITE, testInitialX, testInitialY);
        int blockerX = testInitialX + 2;
        int blockerY = testInitialY + 1;
        Piece testBlocker = new FakePiece(Color.WHITE, blockerX, blockerY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);
        when(fakeBoard.occupant(blockerX, blockerY)).thenReturn(testBlocker);

        GenericMove testMove = MoveFactory.create(fakeBoard, testKnight, blockerX, blockerY);
        List<GenericMove> moves = testKnight.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }
}
