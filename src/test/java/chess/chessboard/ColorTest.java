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
    public void fenCase_returnsALowerCase_whenCalledOnBlack() {
        char test = 'S';
        char expected = 's';
        char result = Color.BLACK.fenCase(test);
        assertTrue(expected == result);
    }

    @Test
    public void fenCase_returnsAnUpperCase_whenCalledOnWhite() {
        char test = 'x';
        char expected = 'X';
        char result = Color.WHITE.fenCase(test);
        assertTrue(expected == result);
    }
}
