/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.notation;

import chess.chessboard.Color;
import chess.chessboard.moves.Capture;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.pieces.Pawn;
import java.util.Stack;
import javafx.util.Pair;

/**
 *
 * @author Nick Houser
 */
public class History {

    //the move history
    private Stack<GenericMove> moveHistory;

    //the history of the number of moves since a piece has been captured or a pawn moved
    private Stack<Integer> halfMoveClock;

    //the number of total turns taken by each player in the game
    private int fullMoveClock;

    /**
     * Constructor which initializes the history to an empty stack.
     */
    public History() {
        moveHistory = new Stack<>();
        halfMoveClock = new Stack<>();
        fullMoveClock = 1;
    }

    /**
     * Method which returns a list of past moves made on the board.
     *
     * @return a list of past moves made on the board
     */
    public GenericMove last() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.peek();
    }

    /**
     * Method which removes the most recent item of move history.
     */
    public void undo() {
        GenericMove reversion = moveHistory.pop();

        if (reversion.getPiece().color == Color.BLACK) {
            fullMoveClock = fullMoveClock - 1;
        }

        halfMoveClock.pop();
    }

    /**
     * Method which adds a move to the move history.
     *
     * @param move the move to add
     */
    public void add(GenericMove move) {
        moveHistory.push(move);

        if (move.getPiece().color == Color.BLACK) {
            fullMoveClock = fullMoveClock + 1;
        }

        if (move.getPiece() instanceof Pawn || move instanceof Capture) {
            halfMoveClock.push(0);
        } else {
            halfMoveClock.push(halfMoveClock() + 1);
        }
    }

    /**
     * Method which returns how many full turns have passed. A full turn is
     * defined as a white move followed by a black move.
     *
     * @return the number of full moves that have passed
     */
    public int fullMoveClock() {
        return fullMoveClock;
    }

    /**
     * Method which returns how many moves have passed since a piece was
     * captured or a pawn was moved. This is used for determining the status of
     * a draw under the 50 move rule.
     *
     * @return the number of moves since a piece was captured or a pawn was
     * moved
     */
    public int halfMoveClock() {
        if (halfMoveClock.isEmpty()) {
            return 0;
        }

        return halfMoveClock.peek();
    }
}
