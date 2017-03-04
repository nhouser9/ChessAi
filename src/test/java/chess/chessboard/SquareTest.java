/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.chessboard.Square;
import org.junit.Test;
import static org.junit.Assert.*;

public class SquareTest {

    @Test
    public void toString_returnsCorrectChessNotation_forSquareA1() {
        Square test = new Square(0, 7);
        assertTrue(test.toString().equals("a1"));
    }

    @Test
    public void toString_returnsCorrectChessNotation_forSquareH8() {
        Square test = new Square(7, 0);
        assertTrue(test.toString().equals("h8"));
    }

    @Test
    public void toString_returnsCorrectChessNotation_forSquareC4() {
        Square test = new Square(2, 4);
        assertTrue(test.toString().equals("c4"));
    }
}
