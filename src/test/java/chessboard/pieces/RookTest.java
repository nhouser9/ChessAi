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

public class RookTest {

    @Test
    public void validMoves_preventsNonStraightMoves() {
        int testInitialX = 1;
        int testInitialY = 4;
        Rook testRook = new Rook(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testRook.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                if (col != testInitialX && row != testInitialY) {
                    GenericMove testMove = MoveFactory.create(fakeBoard, testRook, col, row);
                    assertFalse(moves.contains(testMove));
                }
                if (col == testInitialX && row == testInitialY) {
                    GenericMove testMove = MoveFactory.create(fakeBoard, testRook, col, row);
                    assertFalse(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsStraightMoves() {
        int testInitialX = 3;
        int testInitialY = 6;
        Rook testRook = new Rook(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testRook.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                if (col == testInitialX || row == testInitialY) {
                    if (col == testInitialX && row == testInitialY) {
                        continue;
                    }
                    GenericMove testMove = MoveFactory.create(fakeBoard, testRook, col, row);
                    assertTrue(moves.contains(testMove));
                }
            }
        }
    }
}
