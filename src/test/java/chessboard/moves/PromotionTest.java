/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.moves;

import chessboard.Board;
import chessboard.Color;
import chessboard.pieces.Knight;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PromotionTest {

    @Test
    public void revert_restoresACapturedPiece() {
        Pawn testPromote = new Pawn(Color.WHITE, 0, Color.BLACK.pawnRow());
        Knight testCapture = new Knight(Color.BLACK, 1, Color.BLACK.homeRow());

        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(testPromote);
        initialPieces.add(testCapture);

        Board board = new Board(initialPieces, Color.WHITE);

        Promotion test = new Promotion(board, testPromote, testCapture.position());

        test.execute();
        test.revert();

        assertTrue(board.occupant(testCapture.position().x, testCapture.position().y) == testCapture);
    }
}
