/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.notation;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.MockBoard;
import chess.chessboard.Square;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.Bishop;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Queen;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FenTest {

    @Test
    public void toString_returnsSixParts() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        fakeCastleOptions(fakeBoard);

        Fen test = new Fen(fakeBoard);

        String result = test.toString();
        assertTrue(result.split(" ").length == 6);
    }

    @Test
    public void toString_returnsCorrectFenOfPieces() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.WHITE);
        fakeCastleOptions(fakeBoard);

        Square fakeSquare = mock(Square.class);
        when(fakeBoard.square(0, 0)).thenReturn(fakeSquare);
        when(fakeBoard.square(0, 1)).thenReturn(fakeSquare);
        when(fakeBoard.square(1, 1)).thenReturn(fakeSquare);
        when(fakeBoard.square(4, 7)).thenReturn(fakeSquare);
        when(fakeBoard.square(5, 7)).thenReturn(fakeSquare);
        when(fakeBoard.square(7, 7)).thenReturn(fakeSquare);
        when(fakeSquare.occupant())
                .thenReturn(new King(Color.BLACK, 0, 0))
                .thenReturn(new Pawn(Color.BLACK, 0, 1))
                .thenReturn(new Pawn(Color.BLACK, 1, 1))
                .thenReturn(new Queen(Color.WHITE, 4, 7))
                .thenReturn(new Bishop(Color.WHITE, 5, 7))
                .thenReturn(new King(Color.WHITE, 7, 7));

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[0];
        String expected = "k7/pp6/8/8/8/8/8/4QB1K";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectFenOfActivePlayer() {
        Board fakeBoard = MockBoard.create();
        fakeCastleOptions(fakeBoard);

        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[1];
        String expected = "b";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectFenOfCastles_whenCastlesPossible() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.WHITE);

        King fakeWhiteKing = mock(King.class);
        when(fakeWhiteKing.castleStillLegal(fakeBoard, Direction.EAST)).thenReturn(false);
        when(fakeWhiteKing.castleStillLegal(fakeBoard, Direction.WEST)).thenReturn(true);
        when(fakeBoard.findKing(Color.WHITE)).thenReturn(fakeWhiteKing);
        King fakeBlackKing = mock(King.class);
        when(fakeBlackKing.castleStillLegal(fakeBoard, Direction.EAST)).thenReturn(true);
        when(fakeBlackKing.castleStillLegal(fakeBoard, Direction.WEST)).thenReturn(true);
        when(fakeBoard.findKing(Color.BLACK)).thenReturn(fakeBlackKing);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[2];
        String expected = "Qkq";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectFenOfCastles_whenCastlesImpossible() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);

        fakeCastleOptions(fakeBoard);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[2];
        String expected = "-";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectEnPassantTarget_whenLastMoveWasDoubleMove() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        fakeCastleOptions(fakeBoard);

        Square fakeSquare = mock(Square.class);
        when(fakeSquare.toString()).thenReturn("a6");
        when(fakeBoard.square(0, 2)).thenReturn(fakeSquare);
        GenericMove fakeMove = MoveFactory.create(fakeBoard, new Pawn(Color.BLACK, 0, 1), 0, 3);
        History fakeHistory = mock(History.class);
        when(fakeHistory.last()).thenReturn(fakeMove);
        when(fakeBoard.history()).thenReturn(fakeHistory);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[3];
        String expected = "a6";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectEnPassantTarget_whenLastMoveWasNormalMove() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        fakeCastleOptions(fakeBoard);

        GenericMove fakeMove = MoveFactory.create(fakeBoard, new Pawn(Color.BLACK, 0, 1), 0, 2);
        History fakeHistory = mock(History.class);
        when(fakeHistory.last()).thenReturn(fakeMove);
        when(fakeBoard.history()).thenReturn(fakeHistory);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[3];
        String expected = "-";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectEnPassantTarget_whenNoLastMoveExists() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        fakeCastleOptions(fakeBoard);

        History fakeHistory = mock(History.class);
        when(fakeHistory.last()).thenReturn(null);
        when(fakeBoard.history()).thenReturn(fakeHistory);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[3];
        String expected = "-";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnsCorrectHalfMoveClock() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.BLACK);
        fakeCastleOptions(fakeBoard);

        History fakeHistory = mock(History.class);
        when(fakeBoard.history()).thenReturn(fakeHistory);
        when(fakeHistory.halfMoveClock()).thenReturn(3);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[4];
        String expected = "3";

        assertTrue(expected.equals(result));
    }

    @Test
    public void toString_returnCorrectFullMoveClock() {
        Board fakeBoard = MockBoard.create();
        when(fakeBoard.activePlayer()).thenReturn(Color.WHITE);
        fakeCastleOptions(fakeBoard);

        History fakeHistory = mock(History.class);
        when(fakeBoard.history()).thenReturn(fakeHistory);
        when(fakeHistory.fullMoveClock()).thenReturn(6);

        Fen test = new Fen(fakeBoard);

        String result = test.toString().split(" ")[5];
        String expected = "6";

        assertTrue(expected.equals(result));
    }

    private void fakeCastleOptions(Board fakeBoard) {
        King fakeWhiteKing = mock(King.class);
        when(fakeWhiteKing.castleStillLegal(fakeBoard, Direction.EAST)).thenReturn(false);
        when(fakeWhiteKing.castleStillLegal(fakeBoard, Direction.WEST)).thenReturn(false);
        when(fakeBoard.findKing(Color.WHITE)).thenReturn(fakeWhiteKing);

        King fakeBlackKing = mock(King.class);
        when(fakeBlackKing.castleStillLegal(fakeBoard, Direction.EAST)).thenReturn(false);
        when(fakeBlackKing.castleStillLegal(fakeBoard, Direction.WEST)).thenReturn(false);
        when(fakeBoard.findKing(Color.BLACK)).thenReturn(fakeBlackKing);
    }
}
