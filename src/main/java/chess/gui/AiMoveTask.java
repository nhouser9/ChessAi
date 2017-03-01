/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui;

import chess.ai.MoveSelector;
import chess.chessboard.Board;
import chess.chessboard.moves.GenericMove;
import javafx.concurrent.Task;

/**
 * A task which allows position analysis to be performed on a background thread
 * so as not to lock the UI.
 *
 * @author Nick Houser
 */
public class AiMoveTask extends Task<GenericMove> {

    //the game board to analyze
    private final Board board;

    /**
     * Constructor which saves a link to the board to analyze.
     *
     * @param board the board to analyze
     */
    public AiMoveTask(Board board) {
        this.board = board;
    }

    /**
     * Override of the call() method in Task. Analyzes the position and returns
     * the best move.
     *
     * @return the best move in the given position
     * @throws Exception if the computation fails
     */
    @Override
    protected GenericMove call() throws Exception {
        return new MoveSelector(board, 4).selectMove();
    }
}
