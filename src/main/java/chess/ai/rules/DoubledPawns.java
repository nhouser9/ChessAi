/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.chessboard.Board;
import chess.chessboard.Square;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import java.awt.Point;

/**
 * Positional rule which removes value from pawns that are on the same file.
 *
 * @author Nick Houser
 */
public class DoubledPawns implements PositionalRule {

    /**
     * Method which counts instances of this rule on the board.
     *
     * @param piece the piece to check for this rule
     * @param board the game board
     * @return the number of times this rule is satisfied on the passed board
     */
    @Override
    public int count(Piece piece, Board board) {
        for (int rank = 0; rank < Board.SQUARES_PER_SIDE; rank++) {
            if (rank == piece.position().y) {
                continue;
            }

            Point w = piece.position();
            Square x = board.square(w.x, rank);
            Piece y = x.occupant();

            Piece occupant = board.square(piece.position().x, rank).occupant();
            if (occupant != null && occupant instanceof Pawn && occupant.color == piece.color) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * The value each instance of this rule adds to the positional value of a
     * piece with satisfies the rule.
     *
     * @return the value of each instance of this rule
     */
    @Override
    public Double value() {
        return -.02;
    }
}
