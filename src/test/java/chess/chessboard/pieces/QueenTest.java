/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
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
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        List<GenericMove> moves = testQueen.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    GenericMove testMove = MoveFactory.create(fakeBoard, testQueen, col, row);
                    assertFalse(moves.contains(testMove));
                }
                if (Math.abs(deltaX) != Math.abs(deltaY) && col != testInitialX && row != testInitialY) {
                    GenericMove testMove = MoveFactory.create(fakeBoard, testQueen, col, row);
                    assertFalse(moves.contains(testMove));
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
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        List<GenericMove> moves = testQueen.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }
                if (Math.abs(deltaX) == Math.abs(deltaY) || col == testInitialX || row == testInitialY) {
                    GenericMove testMove = MoveFactory.create(fakeBoard, testQueen, col, row);
                    assertTrue(moves.contains(testMove));
                }
            }
        }
    }
}
