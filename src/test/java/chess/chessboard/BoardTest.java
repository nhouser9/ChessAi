/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.Bishop;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Queen;
import chess.chessboard.pieces.Rook;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

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
        List<GenericMove> moves = testBoard.validMoves();
        for (GenericMove move : moves) {
            assertTrue(move.getPiece().color == Color.WHITE);
        }
    }

    @Test
    public void validMoves_enforcesTurnOrder() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        Color lastMove = null;
        for (int moveNum = 0; moveNum < 10; moveNum++) {
            List<GenericMove> moves = testBoard.validMoves();
            for (GenericMove move : moves) {
                assertTrue(move.getPiece().color != lastMove);
            }
            GenericMove toMake = moves.get(0);
            lastMove = toMake.getPiece().color;
            toMake.execute();
        }
    }

    @Test
    public void move_allowsAValidMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        List<GenericMove> moves = testBoard.validMoves();
        GenericMove toMake = moves.get(0);
        assertTrue(testBoard.square(toMake.from.x, toMake.from.y).occupant() == toMake.getPiece());
        assertTrue(toMake.execute());
        assertTrue(testBoard.square(toMake.to.x, toMake.to.y).occupant() == toMake.getPiece());
        assertFalse(testBoard.square(toMake.from.x, toMake.from.y).occupant() == toMake.getPiece());
    }

    @Test
    public void move_preventsAnInvalidMove() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        GenericMove toMake = MoveFactory.create(testBoard, testBoard.square(0, 0).occupant(), 7, 7);
        assertFalse(toMake.execute());
        assertTrue(testBoard.square(0, 0).occupant() != null);
    }

    @Test
    public void move_updatesMoverPosition_whenSuccessful() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        List<GenericMove> moves = testBoard.validMoves();
        GenericMove toMake = moves.get(0);
        Point oldPos = toMake.getPiece().position();
        assertTrue(toMake.execute());
        assertFalse(toMake.getPiece().position() == oldPos);
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

        GenericMove initialPawnMove = MoveFactory.create(testBoard, capturedPawn, captured.x, captured.y - 2);
        initialPawnMove.execute();

        GenericMove enPassant = MoveFactory.create(testBoard, capturingPawn, captured.x, capturing.y + 1);
        enPassant.execute();

        assertTrue(testBoard.square(capturedPawn.position().x, capturedPawn.position().y).occupant() == null);
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

        GenericMove testMove = MoveFactory.create(testBoard, king, kingLoc.x, rookLoc.y);
        assertTrue(testMove.endangersKing());
    }

    @Test
    public void moveSacksKing_returnsFalse_whenMovingNormally() {
        Board testBoard = new Board(Board.initialState(), Color.WHITE);
        assertFalse(testBoard.validMoves().get(0).endangersKing());
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

        List<GenericMove> moves = testBoard.validMoves();
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

        GenericMove castle = MoveFactory.create(testBoard, king, king.position().x + (2 * Direction.EAST.x()), king.position().y);
        castle.execute();

        assertTrue(testBoard.square(rookLoc.x, rookLoc.y).occupant() == null);
        assertTrue(testBoard.square(rookLoc.x + (2 * Direction.WEST.x()), rookLoc.y).occupant() == rook);
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

        GenericMove castle = MoveFactory.create(testBoard, king, king.position().x + (2 * Direction.WEST.x()), king.position().y);
        castle.execute();

        assertTrue(testBoard.square(rookLoc.x, rookLoc.y).occupant() == null);
        assertTrue(testBoard.square(rookLoc.x + (3 * Direction.EAST.x()), rookLoc.y).occupant() == rook);
    }

    @Test
    public void move_queensPawns_whenTheyReachTheLastRow() {
        List<Piece> initialPieces = new LinkedList<>();
        Pawn pawn = new Pawn(Color.WHITE, 0, Color.WHITE.queenRow() + Color.BLACK.forwardDirection().y());
        initialPieces.add(pawn);
        Board testBoard = new Board(initialPieces, Color.WHITE);

        Point target = new Point(pawn.position().x, Color.WHITE.queenRow());
        MoveFactory.create(testBoard, pawn, target.x, target.y).execute();

        assertTrue(testBoard.square(target.x, target.y).occupant() instanceof Queen);
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

        GenericMove move1 = MoveFactory.create(original, capturedPawn, 0, 3);
        move1.execute();

        GenericMove move2 = MoveFactory.create(original, attackingPawn, 0, 2);
        move2.execute();

        move2.revert();
        move1.revert();

        assertTrue(original.square(captured.x, captured.y).occupant() == capturedPawn);
        assertTrue(original.square(attacking.x, attacking.y).occupant() == attackingPawn);
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
        GenericMove move1 = MoveFactory.create(original, throwawayPawn, 0, 2);
        move1.execute();
        GenericMove move2 = MoveFactory.create(original, whitePawn, 3, 3);
        move2.execute();
        GenericMove move3 = MoveFactory.create(original, blackPawn, 4, 3);
        move3.execute();
        GenericMove move4 = MoveFactory.create(original, whitePawn, 4, 2);
        move4.execute();

        move4.revert();
        move3.revert();
        move2.revert();
        move1.revert();

        assertTrue(original.square(throwawayPawnLoc.x, throwawayPawnLoc.y).occupant() == throwawayPawn);
        assertTrue(original.square(blackPawnLoc.x, blackPawnLoc.y).occupant() == blackPawn);
        assertTrue(original.square(whitePawnLoc.x, whitePawnLoc.y).occupant() == whitePawn);
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
        GenericMove move1 = MoveFactory.create(original, blackPawn, 2, 3);
        move1.execute();
        GenericMove move2 = MoveFactory.create(original, whitePawn, 3, 2);
        move2.execute();

        move2.revert();
        move1.revert();

        assertTrue(original.square(2, 2).occupant() == null);
        assertTrue(original.square(blackPawnLoc.x, blackPawnLoc.y).occupant() == blackPawn);
        assertTrue(original.square(whitePawnLoc.x, whitePawnLoc.y).occupant() == whitePawn);
    }

    @Test
    public void copyOf_placesPiecesCorrectly() {
        Board original = new Board(Board.initialState(), Color.WHITE);
        for (int move = 0; move < 4; move++) {
            original.validMoves().get(2).execute();
        }

        Board copy = original.copyOf();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece originalOccupant = original.square(col, row).occupant();
                Piece copyOccupant = copy.square(col, row).occupant();
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
            original.validMoves().get(1).execute();
        }

        Board copy = original.copyOf();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece originalOccupant = original.square(col, row).occupant();
                Piece copyOccupant = copy.square(col, row).occupant();
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
        Color active = original.validMoves().get(0).getPiece().color;

        assertTrue(copy.validMoves().get(0).getPiece().color == active);
    }
}
