/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class KingTest {

    @Test
    public void validMoves_preventsNonAdjacentMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        List<GenericMove> moves = testKing.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                GenericMove testMove = MoveFactory.create(fakeBoard, testKing, col, row);
                if (deltaX == 0 && deltaY == 0) {
                    assertFalse(moves.contains(testMove));
                }
                if (Math.abs(deltaX) >= 2 && Math.abs(deltaY) >= 2) {
                    assertFalse(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validMoves_allowsAdjacentMoves() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        List<GenericMove> moves = testKing.validMoves(fakeBoard);
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                int deltaX = col - testInitialX;
                int deltaY = row - testInitialY;
                GenericMove testMove = MoveFactory.create(fakeBoard, testKing, col, row);
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }
                if (Math.abs(deltaX) < 2 && Math.abs(deltaY) < 2) {
                    assertTrue(moves.contains(testMove));
                }
            }
        }
    }

    @Test
    public void validMoves_preventsCollisionWithNonEnemies() {
        int testInitialX = 5;
        int testInitialY = 6;
        King testKing = new King(Color.WHITE, testInitialX, testInitialY);
        Direction blockerDir = Direction.EAST;
        int blockerX = testInitialX + blockerDir.x();
        int blockerY = testInitialY + blockerDir.y();
        Piece testBlocker = new StubPiece(Color.WHITE, blockerX, blockerY);

        Board fakeBoard = mock(Board.class);
        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fakeBoard.square(any(int.class), any(int.class))).thenReturn(fakeSquare);
        Square fakeOccupied = mock(Square.class);
        when(fakeOccupied.occupant()).thenReturn(testBlocker);
        when(fakeBoard.square(blockerX, blockerY)).thenReturn(fakeOccupied);

        GenericMove testMove = MoveFactory.create(fakeBoard, testKing, blockerX, blockerY);
        List<GenericMove> moves = testKing.validMoves(fakeBoard);
        assertFalse(moves.contains(testMove));
    }

    @Test
    public void canCastle_returnsFalse_whenKingHasMoved() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        King king = new King(Color.WHITE, kingLoc.x, kingLoc.y);
        king.addMoveCount();
        initialPieces.add(king);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        assertFalse(canCastleRight(king, testBoard));
    }

    @Test
    public void canCastleEast_returnsFalse_whenRightRookHasMoved() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        Point rookLoc = new Point(7, 7);
        King king = new King(Color.WHITE, kingLoc.x, kingLoc.y);
        Rook rook = mock(Rook.class);
        when(rook.hasMoved()).thenReturn(true);
        when(rook.position()).thenReturn(rookLoc);
        initialPieces.add(king);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        assertFalse(canCastleRight(king, testBoard));
    }

    @Test
    public void canCastle_returnsFalse_whenKingIsInCheck() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 0);
        Point rookLoc = new Point(7, 0);
        King king = new King(Color.BLACK, kingLoc.x, kingLoc.y);
        Rook rook = new Rook(Color.BLACK, rookLoc.x, rookLoc.y);
        Rook enemy = new Rook(Color.WHITE, 7, 7);
        initialPieces.add(king);
        initialPieces.add(rook);
        initialPieces.add(enemy);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        GenericMove rookMove = MoveFactory.create(testBoard, enemy, kingLoc.x, enemy.position().y);
        rookMove.execute();

        assertFalse(canCastleRight(king, testBoard));
    }

    @Test
    public void canCastleRight_returnsFalse_whenPiecesInTheWayOfKing() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        King king = testBoard.findKing(Color.WHITE);
        assertFalse(canCastleRight(king, testBoard));
        assertFalse(canCastleLeft(king, testBoard));
    }

    @Test
    public void canCastleRight_returnsFalse_whenPiecesInTheWayOfRook() {
        List<Piece> initialPieces = new LinkedList<>();
        King king = new King(Color.WHITE, 4, Color.WHITE.homeRow());
        initialPieces.add(king);
        initialPieces.add(new Rook(Color.WHITE, 0, Color.WHITE.homeRow()));
        initialPieces.add(new Rook(Color.WHITE, 2, Color.WHITE.homeRow()));
        Board testBoard = new Board(initialPieces, Color.WHITE);
        assertFalse(canCastleLeft(king, testBoard));
    }

    @Test
    public void canCastleRight_returnsFalse_whenInterveningSpaceThreatened() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 0);
        Point rookLoc = new Point(7, 0);
        King king = new King(Color.BLACK, kingLoc.x, kingLoc.y);
        Rook rook = new Rook(Color.BLACK, rookLoc.x, rookLoc.y);
        Rook enemy = new Rook(Color.WHITE, 7, 7);
        initialPieces.add(king);
        initialPieces.add(rook);
        initialPieces.add(enemy);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        GenericMove rookMove = MoveFactory.create(testBoard, enemy, kingLoc.x + 1, enemy.position().y);
        rookMove.execute();

        assertFalse(canCastleRight(king, testBoard));
    }

    @Test
    public void canCastle_returnsTrue_whenUnblockedUnthreatenedAndUnmoved() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        Point rookLoc = new Point(7, 7);
        King king = new King(Color.WHITE, kingLoc.x, kingLoc.y);
        Rook rook = new Rook(Color.WHITE, rookLoc.x, rookLoc.y);
        initialPieces.add(king);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        assertTrue(canCastleRight(king, testBoard));
    }

    private boolean canCastleLeft(King king, Board board) {
        List<GenericMove> moves = king.validMoves(board);
        GenericMove move = MoveFactory.create(board, king, 2, king.color.homeRow());
        return moves.contains(move);
    }

    private boolean canCastleRight(King king, Board board) {
        List<GenericMove> moves = king.validMoves(board);
        GenericMove move = MoveFactory.create(board, king, 6, king.color.homeRow());
        return moves.contains(move);
    }
}
