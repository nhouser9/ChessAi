/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.ai.MoveSelector;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Knight;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Queen;
import chess.chessboard.pieces.Rook;
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

        assertTrue(test.selectMove() != null);
    }

    @Test
    public void bestMove_returnsNull_whenNoMovesPossible() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new King(Color.BLACK, 0, 0));
        initialPieces.add(new Rook(Color.WHITE, 7, 1));
        initialPieces.add(new Rook(Color.WHITE, 6, 0));

        Board testBoard = new Board(initialPieces, Color.BLACK);
        MoveSelector test = new MoveSelector(testBoard, 3);

        assertTrue(test.selectMove() == null);
    }

    @Test
    public void bestMove_findsForkAttack() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new Queen(Color.WHITE, 0, 0));
        initialPieces.add(new Rook(Color.WHITE, 4, 0));
        initialPieces.add(new Knight(Color.BLACK, 0, 2));

        Board testBoard = new Board(initialPieces, Color.BLACK);
        MoveSelector test = new MoveSelector(testBoard, 4);

        GenericMove result = test.selectMove();
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

        GenericMove result = test.selectMove();
        assertTrue(result.to.equals(new Point(6, 0)));
        assertTrue(result.getPiece() instanceof Rook);
    }
}
