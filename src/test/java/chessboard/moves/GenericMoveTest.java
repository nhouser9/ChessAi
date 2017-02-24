/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.moves;

import chessboard.Board;
import chessboard.Color;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import java.awt.Point;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericMoveTest {

    @Test
    public void equals_returnsFalse_whenPassedAnObjectOfWrongType() {
        Piece fakeMover = mock(Piece.class);
        when(fakeMover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new FakeGenericMove(null, fakeMover, null);
        assertFalse(test.equals(fakeMover));
    }

    @Test
    public void equals_returnsFalse_whenPassedDifferentPieces() {
        Piece mover = new Pawn(Color.BLACK, 0, 0);
        Piece notEqual = new Pawn(Color.WHITE, 0, 0);
        Point moveTo = new Point(1, 1);
        when(mover.position()).thenReturn(new Point(0, 0));
        GenericMove test = new FakeGenericMove(null, mover, moveTo);
        GenericMove testEquals = new FakeGenericMove(null, notEqual, moveTo);
        assertFalse(test.equals(testEquals));
    }

    @Test
    public void revert_returnsFalse_whenNotEqualToLastMove() {
        Board fakeBoard = mock(Board.class);
        Piece mover = mock(Piece.class);
        GenericMove fakeLastMove = new FakeGenericMove(fakeBoard, mover, new Point(0, 0));
        when(fakeBoard.lastMove()).thenReturn(fakeLastMove);

        GenericMove test = new FakeGenericMove(fakeBoard, mover, new Point(1, 1));
        assertFalse(test.revert());
    }
}
