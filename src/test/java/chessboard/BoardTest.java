/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard;

import chessboard.moves.Move;
import chessboard.pieces.King;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import chessboard.pieces.Queen;
import chessboard.pieces.Rook;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BoardTest {

    @Test
    public void inBounds_returnsFalse_whenPassedNegativeY() {
        assertFalse(Board.inBounds(0, -1));
    }

    @Test
    public void inBounds_returnsFalse_whenPassedNegativeX() {
        assertFalse(Board.inBounds(-5, 1));
    }

    @Test
    public void inBounds_returnsFalse_whenPassedLargeY() {
        assertFalse(Board.inBounds(7, 100));
    }

    @Test
    public void inBounds_returnsFalse_whenPassedLargeX() {
        assertFalse(Board.inBounds(Board.SQUARES_PER_SIDE, 4));
    }

    @Test
    public void inBounds_returnsTrue_whenPassedNormalCoOrdinates() {
        assertTrue(Board.inBounds(Board.SQUARES_PER_SIDE - 1, 4));
    }

    @Test
    public void validMoves_allowsOnlyWhiteToMoveFirst() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        List<Move> moves = testBoard.validMoves();
        for (Move move : moves) {
            assertTrue(move.piece.color == Color.WHITE);
        }
    }

    @Test
    public void validMoves_enforcesTurnOrder() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        Color lastMove = null;
        for (int moveNum = 0; moveNum < 10; moveNum++) {
            List<Move> moves = testBoard.validMoves();
            for (Move move : moves) {
                assertTrue(move.piece.color != lastMove);
            }
            Move toMake = moves.get(0);
            lastMove = toMake.piece.color;
            testBoard.move(toMake);
        }
    }

    @Test
    public void move_allowsAValidMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        List<Move> moves = testBoard.validMoves();
        Move toMake = moves.get(0);
        assertTrue(testBoard.occupant(toMake.from.x, toMake.from.y) == toMake.piece);
        assertTrue(testBoard.move(toMake));
        assertTrue(testBoard.occupant(toMake.to.x, toMake.to.y) == toMake.piece);
        assertFalse(testBoard.occupant(toMake.from.x, toMake.from.y) == toMake.piece);
    }

    @Test
    public void move_preventsAnInvalidMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        Move toMake = new Move(testBoard, testBoard.occupant(0, 0), new Point(7, 7));
        assertFalse(testBoard.move(toMake));
        assertTrue(testBoard.occupant(0, 0) != null);
    }

    @Test
    public void move_updatesMoverPosition_whenSuccessful() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        List<Move> moves = testBoard.validMoves();
        Move toMake = moves.get(0);
        Point oldPos = toMake.piece.position();
        assertTrue(testBoard.move(toMake));
        assertFalse(toMake.piece.position() == oldPos);
    }

    @Test
    public void lastMove_returnsTheLastMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        Move move1 = testBoard.validMoves().get(3);
        testBoard.move(move1);
        assertTrue(testBoard.lastMove() == move1);
        Move move2 = testBoard.validMoves().get(5);
        testBoard.move(move2);
        assertTrue(testBoard.lastMove() == move2);
    }

    @Test
    public void move_removesPawnDuringEnPassant() {
        List<Piece> initialPieces = new LinkedList<>();
        Point capturing = new Point(0, 4);
        Point captured = new Point(1, 6);
        Pawn capturingPawn = new Pawn(Color.BLACK, capturing.x, capturing.y);
        Pawn capturedPawn = new Pawn(Color.WHITE, captured.x, captured.y);
        initialPieces.add(capturingPawn);
        initialPieces.add(capturedPawn);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Move initialPawnMove = new Move(testBoard, capturedPawn, new Point(captured.x, captured.y - 2));
        testBoard.move(initialPawnMove);

        Move enPassant = new Move(testBoard, capturingPawn, new Point(captured.x, capturing.y + 1));
        testBoard.move(enPassant);

        assertTrue(testBoard.occupant(capturedPawn.position().x, capturedPawn.position().y) == null);
    }

    @Test
    public void canCastle_returnsFalse_whenKingHasMoved() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        King king = mock(King.class);
        when(king.hasMoved()).thenReturn(true);
        when(king.position()).thenReturn(kingLoc);
        initialPieces.add(king);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        assertFalse(testBoard.canCastle(Direction.EAST, Color.WHITE));
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

        assertFalse(testBoard.canCastle(Direction.EAST, Color.WHITE));
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

        Move rookMove = new Move(testBoard, enemy, new Point(kingLoc.x, enemy.position().y));
        testBoard.move(rookMove);

        assertFalse(testBoard.canCastle(Direction.EAST, king.color));
    }

    @Test
    public void canCastleRight_returnsFalse_whenPiecesInTheWayOfKing() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        assertFalse(testBoard.canCastle(Direction.EAST, Color.WHITE));
        assertFalse(testBoard.canCastle(Direction.WEST, Color.WHITE));
    }

    @Test
    public void canCastleRight_returnsFalse_whenPiecesInTheWayOfRook() {
        List<Piece> initialPieces = new LinkedList<>();
        initialPieces.add(new King(Color.WHITE, 4, Color.WHITE.homeRow()));
        initialPieces.add(new Rook(Color.WHITE, 0, Color.WHITE.homeRow()));
        initialPieces.add(new Rook(Color.WHITE, 2, Color.WHITE.homeRow()));
        Board testBoard = new Board(initialPieces, Color.WHITE);
        assertFalse(testBoard.canCastle(Direction.WEST, Color.WHITE));
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

        Move rookMove = new Move(testBoard, enemy, new Point(kingLoc.x + 1, enemy.position().y));
        testBoard.move(rookMove);

        assertFalse(testBoard.canCastle(Direction.EAST, Color.BLACK));
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

        assertTrue(testBoard.canCastle(Direction.EAST, Color.WHITE));
    }

    @Test
    public void canCastle_returnsFalse_whenPassedADirectionNotEastOrWest() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        assertFalse(testBoard.canCastle(Direction.NORTH, Color.WHITE));
    }

    @Test
    public void moveSacksKing_returnsTrue_whenMovingIntoCheck() {
        List<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        Point rookLoc = new Point(7, 6);
        King king = new King(Color.BLACK, kingLoc.x, kingLoc.y);
        Rook rook = new Rook(Color.WHITE, rookLoc.x, rookLoc.y);
        initialPieces.add(king);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Move testMove = new Move(testBoard, king, new Point(kingLoc.x, rookLoc.y));
        assertTrue(testBoard.moveSacksKing(testMove));
    }

    @Test
    public void moveSacksKing_returnsFalse_whenMovingNormally() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        assertFalse(testBoard.moveSacksKing(testBoard.validMoves().get(0)));
    }

    @Test
    public void validMoves_preventsMovingIntoCheck() {
        LinkedList<Piece> initialPieces = new LinkedList<>();
        Point kingLoc = new Point(4, 7);
        Point rookLoc = new Point(7, 6);
        King king = new King(Color.WHITE, kingLoc.x, kingLoc.y);
        Rook rook = new Rook(Color.BLACK, rookLoc.x, rookLoc.y);
        initialPieces.add(king);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        List<Move> moves = testBoard.validMoves();
        assertTrue(moves.size() == 2);
    }

    @Test
    public void move_movesRookDuringCastleRight() {
        List<Piece> initialPieces = new LinkedList<>();
        King king = new King(Color.WHITE, 4, Color.WHITE.homeRow());
        initialPieces.add(king);
        Point rookLoc = new Point(7, Color.WHITE.homeRow());
        Rook rook = new Rook(Color.WHITE, rookLoc.x, rookLoc.y);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Move castle = new Move(testBoard, king, new Point(king.position().x + (2 * Direction.EAST.x()), king.position().y));
        testBoard.move(castle);

        assertTrue(testBoard.occupant(rookLoc.x, rookLoc.y) == null);
        assertTrue(testBoard.occupant(rookLoc.x + (2 * Direction.WEST.x()), rookLoc.y) == rook);
    }

    @Test
    public void move_movesRookDuringCastleLeft() {
        List<Piece> initialPieces = new LinkedList<>();
        King king = new King(Color.WHITE, 4, Color.WHITE.homeRow());
        initialPieces.add(king);
        Point rookLoc = new Point(0, Color.WHITE.homeRow());
        Rook rook = new Rook(Color.WHITE, rookLoc.x, rookLoc.y);
        initialPieces.add(rook);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Move castle = new Move(testBoard, king, new Point(king.position().x + (2 * Direction.WEST.x()), king.position().y));
        testBoard.move(castle);

        assertTrue(testBoard.occupant(rookLoc.x, rookLoc.y) == null);
        assertTrue(testBoard.occupant(rookLoc.x + (3 * Direction.EAST.x()), rookLoc.y) == rook);
    }

    @Test
    public void move_queensPawns_whenTheyReachTheLastRow() {
        List<Piece> initialPieces = new LinkedList<>();
        Pawn pawn = new Pawn(Color.WHITE, 0, Color.WHITE.queenRow() + Color.BLACK.forwardDirection().y());
        initialPieces.add(pawn);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Point target = new Point(pawn.position().x, Color.WHITE.queenRow());
        testBoard.move(new Move(testBoard, pawn, target));

        assertTrue(testBoard.occupant(target.x, target.y) instanceof Queen);
    }

    @Test
    public void copyOf_placesPiecesCorrectly() {
        Board original = new Board(Board.initialState(), Color.WHITE);
        for (int move = 0; move < 4; move++) {
            original.move(original.validMoves().get(1));
        }

        Board copy = original.copyOf();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece originalOccupant = original.occupant(col, row);
                Piece copyOccupant = copy.occupant(col, row);
                if (originalOccupant == null) {
                    assertTrue(copyOccupant == null);
                } else {
                    assertTrue(originalOccupant.equals(copyOccupant));
                }
            }
        }
    }

    @Test
    public void copyOf_performsDeepCopy() {
        Board original = new Board(Board.initialState(), Color.WHITE);
        for (int move = 0; move < 4; move++) {
            original.move(original.validMoves().get(1));
        }

        Board copy = original.copyOf();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece originalOccupant = original.occupant(col, row);
                Piece copyOccupant = copy.occupant(col, row);
                if (originalOccupant == null) {
                    assertTrue(copyOccupant == null);
                } else {
                    assertTrue(originalOccupant != copyOccupant);
                }
            }
        }
    }

    @Test
    public void copyOf_copiesActivePlayer() {
        Board original = new Board(Board.initialState(), Color.BLACK);

        Board copy = original.copyOf();
        Color active = original.validMoves().get(0).piece.color;

        assertTrue(copy.validMoves().get(0).piece.color == active);
    }

    @Test
    public void revertMove_restoresOriginalBoardState_afterOneMoveThenEnPassant() {
        List<Piece> initial = new LinkedList<>();

        Point captured = new Point(0, Color.BLACK.pawnRow());
        Pawn capturedPawn = new Pawn(Color.BLACK, captured.x, captured.y);
        initial.add(capturedPawn);

        Point attacking = new Point(1, 3);
        Pawn attackingPawn = new Pawn(Color.WHITE, attacking.x, attacking.y);
        initial.add(attackingPawn);

        Board original = new Board(initial, Color.BLACK);
        Move move1 = new Move(original, capturedPawn, new Point(0, 3));
        Move move2 = new Move(original, attackingPawn, new Point(0, 2));

        original.move(move1);
        original.move(move2);
        original.revertMove();
        original.revertMove();

        assertTrue(original.occupant(captured.x, captured.y) == capturedPawn);
        assertTrue(original.occupant(attacking.x, attacking.y) == attackingPawn);
    }

    @Test
    public void revertMove_restoresOriginalBoardState_afterThreeMovesThenEnPassant() {
        List<Piece> initial = new LinkedList<>();

        Point throwawayPawnLoc = new Point(0, 1);
        Pawn throwawayPawn = new Pawn(Color.BLACK, throwawayPawnLoc.x, throwawayPawnLoc.y);
        initial.add(throwawayPawn);

        Point blackPawnLoc = new Point(4, 1);
        Pawn blackPawn = new Pawn(Color.BLACK, blackPawnLoc.x, blackPawnLoc.y);
        initial.add(blackPawn);

        Point whitePawnLoc = new Point(3, 4);
        Pawn whitePawn = new Pawn(Color.WHITE, whitePawnLoc.x, whitePawnLoc.y);
        initial.add(whitePawn);

        Board original = new Board(initial, Color.BLACK);
        Move move1 = new Move(original, throwawayPawn, new Point(0, 2));
        original.move(move1);
        Move move2 = new Move(original, whitePawn, new Point(3, 3));
        original.move(move2);
        Move move3 = new Move(original, blackPawn, new Point(4, 3));
        original.move(move3);
        Move move4 = new Move(original, whitePawn, new Point(4, 2));
        original.move(move4);

        original.revertMove();
        original.revertMove();
        original.revertMove();
        original.revertMove();

        assertTrue(original.occupant(throwawayPawnLoc.x, throwawayPawnLoc.y) == throwawayPawn);
        assertTrue(original.occupant(blackPawnLoc.x, blackPawnLoc.y) == blackPawn);
        assertTrue(original.occupant(whitePawnLoc.x, whitePawnLoc.y) == whitePawn);
    }

    @Test
    public void revertMove_doesNotDuplicatePawn_whenAdvancingAndEnPassantPossible() {
        List<Piece> initial = new LinkedList<>();

        Point blackPawnLoc = new Point(2, 1);
        Pawn blackPawn = new Pawn(Color.BLACK, blackPawnLoc.x, blackPawnLoc.y);
        initial.add(blackPawn);

        Point whitePawnLoc = new Point(3, 3);
        Pawn whitePawn = new Pawn(Color.WHITE, whitePawnLoc.x, whitePawnLoc.y);
        initial.add(whitePawn);

        Board original = new Board(initial, Color.BLACK);
        Move move1 = new Move(original, blackPawn, new Point(2, 3));
        original.move(move1);
        Move move2 = new Move(original, whitePawn, new Point(3, 2));
        original.move(move2);

        original.revertMove();
        original.revertMove();

        assertTrue(original.occupant(2, 2) == null);
        assertTrue(original.occupant(blackPawnLoc.x, blackPawnLoc.y) == blackPawn);
        assertTrue(original.occupant(whitePawnLoc.x, whitePawnLoc.y) == whitePawn);
    }

}
