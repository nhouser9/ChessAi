/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.ai.rules.PushedPawns;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.pieces.Pawn;
import java.awt.Point;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PushedPawnsTest {

    @Test
    public void count_returnsPawnDistanceTravelled() {
        PushedPawns rule = new PushedPawns();
        Board fakeBoard = mock(Board.class);
        int expected = 3;
        Pawn testPawn = new Pawn(Color.WHITE, 0, Color.WHITE.pawnRow() + (3 * Color.WHITE.forwardDirection().y()));
        assertTrue(rule.count(testPawn, fakeBoard) == expected);
    }

    @Test
    public void count_returnsZero_whenPawnHasNotMoved() {
        PushedPawns rule = new PushedPawns();
        Board fakeBoard = mock(Board.class);
        Pawn testPawn = new Pawn(Color.BLACK, 0, Color.BLACK.pawnRow());
        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }

    @Test
    public void count_ignoresXDirectionTravel() {
        PushedPawns rule = new PushedPawns();
        Board fakeBoard = mock(Board.class);
        Pawn testPawn = new Pawn(Color.BLACK, 0, Color.BLACK.pawnRow());
        testPawn.setPosition(new Point(5, Color.BLACK.pawnRow()));
        assertTrue(rule.count(testPawn, fakeBoard) == 0);
    }
}
