/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import org.junit.Test;
import static org.junit.Assert.*;

public class ColorTest {

    @Test
    public void bestOutcome_returnsTheLargerValue_forWhite() {
        double larger = 1.2;
        double smaller = .01;
        assertTrue(Color.WHITE.bestOutcome(smaller, larger) == larger);
    }

    @Test
    public void bestOutcome_returnsTheSmallerValue_forBlack() {
        double larger = -1.2;
        double smaller = -5.01;
        assertTrue(Color.BLACK.bestOutcome(smaller, larger) == smaller);
    }
}
