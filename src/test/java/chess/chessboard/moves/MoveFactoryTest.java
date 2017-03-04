/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Square;
import chess.chessboard.pieces.Bishop;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Knight;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Rook;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveFactoryTest {

    @Test
    public void create_returnsACapture_whenTargetOccupiedByEnemy() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new Bishop(Color.BLACK, 1, 3);
        Piece target = new Knight(Color.WHITE, 3, 1);

        Square occupied = mock(Square.class);
        when(fakeBoard.square(target.position().x, target.position().y)).thenReturn(occupied);
        when(occupied.occupant()).thenReturn(target);

        GenericMove test = MoveFactory.create(fakeBoard, mover, target.position().x, target.position().y);

        assertTrue(test instanceof Capture);
    }

    @Test
    public void create_returnsAQueensideCastle_whenKingMovesTwoToTheLeft() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new King(Color.WHITE, 4, 7);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 2, 7);

        assertTrue(test instanceof QueensideCastle);
    }

    @Test
    public void create_returnsAKingsideCastle_whenKingMovesTwoToTheRight() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new King(Color.WHITE, 4, 7);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 6, 7);

        assertTrue(test instanceof KingsideCastle);
    }

    @Test
    public void create_returnsAnEnPassant_whenPawnMovesDiagonallyToEmptySpace() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new Pawn(Color.BLACK, 0, 4);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 1, 5);

        assertTrue(test instanceof EnPassant);
    }

    @Test
    public void create_returnsAnInitialPawnMove_whenPawnMovesTwoForward() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new Pawn(Color.WHITE, 4, 6);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 4, 4);

        assertTrue(test instanceof InitialPawnMove);
    }

    @Test
    public void create_returnsAPromotion_whenPawnMovesToEnemyHomeRow() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new Pawn(Color.WHITE, 4, 1);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 4, 0);

        assertTrue(test instanceof Promotion);
    }

    @Test
    public void create_returnsANormalMove_whenNoSpecialConditionsMet() {
        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        Piece mover = new Rook(Color.WHITE, 4, 1);

        GenericMove test = MoveFactory.create(fakeBoard, mover, 4, 7);

        assertTrue(test instanceof NormalMove);
    }
}
