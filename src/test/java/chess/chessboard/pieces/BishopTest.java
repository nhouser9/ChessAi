/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BishopTest {

    @Test
    public void validMoves_preventsNonDiagonalMoves() {
        int testInitialX = 5;
        int testInitialY = 4;
        Bishop testBishop = new Bishop(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testBishop.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                GenericMove testMove = MoveFactory.create(fakeBoard, testBishop, col, row);
                if (deltaX == 0 && deltaY == 0) {
                    assertFalse(moves.contains(testMove));
                }
                if (Math.abs(deltaX) != Math.abs(deltaY)) {
                    assertFalse(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsDiagonalMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        Bishop testBishop = new Bishop(Color.BLACK, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(any(int.class), any(int.class))).thenReturn(null);

        List<GenericMove> moves = testBishop.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                GenericMove testMove = MoveFactory.create(fakeBoard, testBishop, col, row);
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }
                if (Math.abs(deltaX) == Math.abs(deltaY)) {
                    assertTrue(moves.contains(testMove));
                }
            }
        }
    }
}
