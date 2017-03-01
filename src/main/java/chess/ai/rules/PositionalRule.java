/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai.rules;

import chess.chessboard.Board;
import chess.chessboard.pieces.Piece;

/**
 * Interface which defines a positional rule. An example would be that for each
 * square a bishop can move to, it is worth .01 additional points. Implementing
 * classes will define the logic for testing how many times the rule is
 * satisfied and how much each instance is worth.
 *
 * @author Nick Houser
 */
public interface PositionalRule {

    /**
     * Method which counts instances of this rule on the board.
     *
     * @param piece the piece to check for this rule
     * @param board the game board
     * @return the number of times this rule is satisfied on the passed board
     */
    public abstract int count(Piece piece, Board board);

    /**
     * The value each instance of this rule adds to the positional value of a
     * piece with satisfies the rule.
     *
     * @return the value of each instance of this rule
     */
    public abstract Double value();
}
