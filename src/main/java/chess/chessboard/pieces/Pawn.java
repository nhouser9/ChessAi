/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.ai.rules.DoubledPawns;
import chess.ai.rules.PushedPawns;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Pawn, a piece which can only move forward. It can move
 * two moves if it has never moved, or one otherwise. It can only move forward
 * if unblocked, and may only capture diagonally.
 *
 * @author Nick Houser
 */
public class Pawn extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Pawn(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which adds all relevant positional rules to this piece.
     */
    @Override
    public void addPositionalRules() {
        addPositionalRule(new DoubledPawns());
        addPositionalRule(new PushedPawns());
    }

    /**
     * Method which returns the material value for the piece, which is a number
     * representing the worth of the type of piece.
     *
     * @return the material value of the piece
     */
    @Override
    public double materialValue() {
        return 1.0;
    }

    /**
     * Method which returns a list of valid moves for the Pawn. Pawns can move
     * only forward one square at a time, except for on the first move, on which
     * they can move two squares. Pawns cannot capture while moving forward,
     * they can only capture diagonally.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<GenericMove> validMoves(Board board) {
        List<GenericMove> toReturn = new ArrayList<>();

        addNormalMove(toReturn, board);
        addCaptureMove(toReturn, board);
        addDoubleMove(toReturn, board);
        addEnPassant(toReturn, board);

        return toReturn;
    }

    /**
     * Method which returns a copy of the Piece.
     *
     * @return a copy of this Piece
     */
    @Override
    public Pawn copy() {
        return new Pawn(color, position().x, position().y);
    }

    /**
     * Helper method which adds valid normal moves to the list of valid moves.
     *
     * @param toReturn the list to populate
     * @param board the board on which the move will be made
     */
    private void addNormalMove(List<GenericMove> toReturn, Board board) {
        int addX = position().x;
        int addY = position().y + color.forwardDirection().y();
        MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
    }

    /**
     * Helper method which adds valid capture moves to the list of valid moves.
     *
     * @param toReturn the list to populate
     * @param board the board on which the move will be made
     */
    private void addCaptureMove(List<GenericMove> toReturn, Board board) {
        for (Direction diagonal : color.forwardDiagonals()) {
            int addX = position().x + diagonal.x();
            int addY = position().y + diagonal.y();
            MoveFactory.addIfValid(toReturn, board, this, addX, addY, true, false);
        }
    }

    /**
     * Helper method which adds valid double moves to the list of valid moves.
     *
     * @param toReturn the list to populate
     * @param board the board on which the move will be made
     */
    private void addDoubleMove(List<GenericMove> toReturn, Board board) {
        Piece blocker = board.occupant(position().x, position().y + color.forwardDirection().y());
        if (blocker == null && position().y == color.pawnRow()) {
            int addX = position().x;
            int addY = position().y + (2 * color.forwardDirection().y());
            MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
        }
    }

    /**
     * Helper method which adds valid en passant moves to the list of valid
     * moves.
     *
     * @param toReturn the list to populate
     * @param board the board on which the move will be made
     */
    private void addEnPassant(List<GenericMove> toReturn, Board board) {
        GenericMove lastMove = board.lastMove();
        if (lastMove == null) {
            return;
        }
        if (!(lastMove.getPiece() instanceof Pawn)) {
            return;
        }
        if (Math.abs(lastMove.to.x - position().x) != 1) {
            return;
        }
        if (Math.abs(lastMove.from.y - lastMove.to.y) != 2) {
            return;
        }
        if (lastMove.to.y != position().y) {
            return;
        }

        int addX = lastMove.to.x;
        int addY = position().y + color.forwardDirection().y();
        MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
    }
}
