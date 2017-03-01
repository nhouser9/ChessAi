/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.ai.rules.DoubledPawns;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Queen;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoubledPawnsTest {

    @Test
    public void count_returnsOne_whenPawnsDoubled() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Pawn testDouble = new Pawn(Color.BLACK, 0, 1);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(testPawn);
        when(fakeBoard.occupant(0, 1)).thenReturn(testDouble);
        assertTrue(rule.count(testPawn, fakeBoard) == 1);
    }

    @Test
    public void count_returnsZero_whenNoOtherPawnsExist() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(testPawn);
        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void count_returnsZero_whenEnemyPawnsShareFile() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Pawn testDouble = new Pawn(Color.WHITE, 0, 1);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(testPawn);
        when(fakeBoard.occupant(0, 1)).thenReturn(testDouble);
        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void count_returnsZero_whenFriendlyPieceSharesFile() {
        DoubledPawns rule = new DoubledPawns();
        Pawn testPawn = new Pawn(Color.BLACK, 0, 0);
        Queen testDouble = new Queen(Color.BLACK, 0, 1);
        Board fakeBoard = mock(Board.class);
        when(fakeBoard.occupant(0, 0)).thenReturn(testPawn);
        when(fakeBoard.occupant(0, 1)).thenReturn(testDouble);
        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void value_returnsANegative() {
        DoubledPawns rule = new DoubledPawns();
        assertTrue(rule.value() < 0);
    }
}
