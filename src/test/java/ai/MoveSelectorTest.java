/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.moves.Move;
import chessboard.pieces.King;
import chessboard.pieces.Knight;
import chessboard.pieces.Piece;
import chessboard.pieces.Queen;
import chessboard.pieces.Rook;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class MoveSelectorTest {

    @Test
    public void bestMove_returnsAMove_whenCheckmatePossible() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new King(Color.BLACK, 0, 0));
        initialPieces.add(new Rook(Color.WHITE, 7, 1));
        initialPieces.add(new Rook(Color.WHITE, 6, 6));

        Board testBoard = new Board(initialPieces, Color.WHITE);
        MoveSelector test = new MoveSelector(testBoard, 3);

        assertTrue(test.bestMove() != null);
    }

    @Test
    public void bestMove_returnsNull_whenNoMovesPossible() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new King(Color.BLACK, 0, 0));
        initialPieces.add(new Rook(Color.WHITE, 7, 1));
        initialPieces.add(new Rook(Color.WHITE, 6, 0));

        Board testBoard = new Board(initialPieces, Color.BLACK);
        MoveSelector test = new MoveSelector(testBoard, 3);

        assertTrue(test.bestMove() == null);
    }

    @Test
    public void bestMove_findsForkAttack() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new Queen(Color.WHITE, 0, 0));
        initialPieces.add(new Rook(Color.WHITE, 4, 0));
        initialPieces.add(new Knight(Color.BLACK, 0, 2));

        Board testBoard = new Board(initialPieces, Color.BLACK);
        MoveSelector test = new MoveSelector(testBoard, 4);

        Move result = test.bestMove();
        assertTrue(result.to.equals(new Point(2, 1)));
    }

    @Test
    public void bestMove_findsCheckmate() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new King(Color.BLACK, 0, 0));
        initialPieces.add(new Queen(Color.WHITE, 7, 1));
        initialPieces.add(new Rook(Color.WHITE, 6, 6));

        Board testBoard = new Board(initialPieces, Color.WHITE);
        MoveSelector test = new MoveSelector(testBoard, 1);

        Move result = test.bestMove();
        assertTrue(result.to.equals(new Point(6, 0)));
        assertTrue(result.piece instanceof Rook);
    }
}
