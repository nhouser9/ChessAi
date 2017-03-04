/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.chessboard.notation.History;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Fake implementation of Board for unit testing.
 *
 * @author Nick Houser
 */
public class MockBoard {

    /**
     * Factory method for mock board.
     *
     * @return a fake board
     */
    public static Board create() {
        Board fake = mock(Board.class);

        Square fakeSquare = mock(Square.class);
        when(fakeSquare.occupant()).thenReturn(null);
        when(fake.square(any(int.class), any(int.class))).thenReturn(fakeSquare);

        History fakeHistory = mock(History.class);
        when(fake.history()).thenReturn(fakeHistory);

        return fake;
    }
}
