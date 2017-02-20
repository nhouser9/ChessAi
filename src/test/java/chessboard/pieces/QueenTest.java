/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import java.awt.Point;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueenTest {

    @Test
    public void validMoves_preventsNonLinearMoves() {
        int testInitialX = 5;
        int testInitialY = 4;
        Queen testQueen = new Queen(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testQueen.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    assertFalse(moves.contains(new Point(col, row)));
                }
                if (Math.abs(deltaX) != Math.abs(deltaY) && col != testInitialX && row != testInitialY) {
                    assertFalse(moves.contains(new Point(col, row)));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsLinearMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        Queen testQueen = new Queen(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<Point> moves = testQueen.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }
                if (Math.abs(deltaX) == Math.abs(deltaY) || col == testInitialX || row == testInitialY) {
                    assertTrue(moves.contains(new Point(col, row)));
                }
            }
        }
    }
}
