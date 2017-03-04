/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Queen;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoubledPawnsTest {

    @Test
    public void count_returnsOne_whenPawnsDoubled() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Pawn testDouble = new Pawn(Color.BLACK, 0, 1);
        Board fakeBoard = mock(Board.class);

        Square fakeSquare1 = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare1);
        when(fakeSquare1.occupant()).thenReturn(testPawn);

        Square fakeSquare2 = mock(Square.class);
        when(fakeBoard.square(0, 1)).thenReturn(fakeSquare2);
        when(fakeSquare2.occupant()).thenReturn(testDouble);

        assertTrue(rule.count(testPawn, fakeBoard) == 1);
    }

    @Test
    public void count_returnsZero_whenNoOtherPawnsExist() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Board fakeBoard = mock(Board.class);

        Square fakeEmpty = mock(Square.class);
        when(fakeEmpty.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeEmpty);

        Square fakeSquare = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare);
        when(fakeSquare.occupant()).thenReturn(testPawn);

        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void count_returnsZero_whenEnemyPawnsShareFile() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Pawn testDouble = new Pawn(Color.WHITE, 0, 1);
        Board fakeBoard = mock(Board.class);

        Square fakeEmpty = mock(Square.class);
        when(fakeEmpty.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeEmpty);

        Square fakeSquare1 = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare1);
        when(fakeSquare1.occupant()).thenReturn(testPawn);

        Square fakeSquare2 = mock(Square.class);
        when(fakeBoard.square(0, 1)).thenReturn(fakeSquare2);
        when(fakeSquare2.occupant()).thenReturn(testDouble);

        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void count_returnsZero_whenFriendlyPieceSharesFile() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Queen testDouble = new Queen(Color.BLACK, 0, 1);
        Board fakeBoard = mock(Board.class);

        Square fakeEmpty = mock(Square.class);
        when(fakeEmpty.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeEmpty);

        Square fakeSquare1 = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare1);
        when(fakeSquare1.occupant()).thenReturn(testPawn);

        Square fakeSquare2 = mock(Square.class);
        when(fakeBoard.square(0, 1)).thenReturn(fakeSquare2);
        when(fakeSquare2.occupant()).thenReturn(testDouble);

        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void value_returnsANegative() {
        DoubledPawns rule = new DoubledPawns();
        assertTrue(rule.value() < 0);
    }
}
