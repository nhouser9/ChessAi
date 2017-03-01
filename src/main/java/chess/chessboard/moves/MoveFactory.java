/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.moves;

import chess.chessboard.Board;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import java.awt.Point;
import java.util.List;

/**
 * Factory for move creation. Determines the appropriate move type based on
 * input and returns an instance of that type.
 *
 * @author Nick Houser
 */
public class MoveFactory {

    /**
     * Method which determines the appropriate move type based on input and
     * returns an instance of that type.
     *
     * @param board the board on which the move will be executed
     * @param mover the piece with will undergo the move
     * @param targetX the x co-ordinate of the target location
     * @param targetY the y co-ordinate of the target location
     * @return a move representing the passed criteria
     */
    public static GenericMove create(Board board, Piece mover, int targetX, int targetY) {
        Point target = new Point(targetX, targetY);
        if (!Board.inBounds(targetX, targetY)) {
            return new NormalMove(board, mover, target);
        }

        if (isEnPassant(board, mover, target)) {
            return new EnPassant(board, mover, target);
        }

        if (isPromotion(mover, target)) {
            return new Promotion(board, mover, target);
        }

        if (isKingsideCastle(mover, target)) {
            return new KingsideCastle(board, mover, target);
        }

        if (isQueensideCastle(mover, target)) {
            return new QueensideCastle(board, mover, target);
        }

        if (isCapture(board, target)) {
            return new Capture(board, mover, target);
        }

        return new NormalMove(board, mover, target);
    }

    /**
     * Helper method which creates a move and adds it to a list of moves only if
     * it meets the passed criteria for validity.
     *
     * @param moves the list of moves to add to
     * @param board the board on which the move will be executed
     * @param mover the piece with will undergo the move
     * @param targetX the x co-ordinate of the target location
     * @param targetY the y co-ordinate of the target location
     * @param enemies whether the move should allow moving onto enemies
     * @param empties whether the move should allow moving onto empty squares
     */
    public static void addIfValid(List<GenericMove> moves, Board board, Piece mover, int targetX, int targetY, boolean enemies, boolean empties) {
        GenericMove potential = create(board, mover, targetX, targetY);
        if (!potential.valid()) {
            return;
        }
        if (board.occupant(targetX, targetY) != null && !enemies) {
            return;
        }
        if (board.occupant(targetX, targetY) == null && !empties) {
            return;
        }
        moves.add(potential);
    }

    /**
     * Helper method which determines if a move is en passant. A move is en
     * passant if it is a pawn move that moves off the file but does not move
     * onto a piece.
     *
     * @param board the board on which the move will be executed
     * @param mover the piece with will undergo the move
     * @param targetPosition the the target location
     * @return true if the move is en passant, false otherwise
     */
    private static boolean isEnPassant(Board board, Piece mover, Point targetPosition) {
        if (!(mover instanceof Pawn)) {
            return false;
        }

        if (mover.position().x == targetPosition.x) {
            return false;
        }

        if (board.occupant(targetPosition.x, targetPosition.y) != null) {
            return false;
        }

        return true;
    }

    /**
     * Helper method which determines if a move is a promotion. A move is a
     * promotion if it is a pawn move that lands on the last rank.
     *
     * @param mover the piece with will undergo the move
     * @param targetPosition the the target location
     * @return true if the move is a promotion, false otherwise
     */
    private static boolean isPromotion(Piece mover, Point targetPosition) {
        if (!(mover instanceof Pawn)) {
            return false;
        }

        if (targetPosition.y != mover.color.queenRow()) {
            return false;
        }

        return true;
    }

    /**
     * Helper method which determines if a move is a kingside castle.
     *
     * @param mover the piece with will undergo the move
     * @param targetPosition the the target location
     * @return true if the move is a kingside castle, false otherwise
     */
    private static boolean isKingsideCastle(Piece mover, Point targetPosition) {
        if (!(mover instanceof King)) {
            return false;
        }

        if (targetPosition.x != mover.position().x + 2) {
            return false;
        }

        return true;
    }

    /**
     * Helper method which determines if a move is a queenside castle.
     *
     * @param mover the piece with will undergo the move
     * @param targetPosition the the target location
     * @return true if the move is a queenside castle, false otherwise
     */
    private static boolean isQueensideCastle(Piece mover, Point targetPosition) {
        if (!(mover instanceof King)) {
            return false;
        }

        if (targetPosition.x != mover.position().x - 2) {
            return false;
        }

        return true;
    }

    /**
     * Helper method which determines if a move is a capture. A move is a
     * capture if it moves onto another piece.
     *
     * @param board the board on which the move will be executed
     * @param targetPosition the the target location
     * @return true if the move is a queenside castle, false otherwise
     */
    private static boolean isCapture(Board board, Point targetPosition) {
        if (board.occupant(targetPosition.x, targetPosition.y) == null) {
            return false;
        }

        return true;
    }
}
