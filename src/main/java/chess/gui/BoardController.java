/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui;

import chess.ai.PositionAnalysis;
import chess.gui.Menu.MenuPane;
import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.Piece;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which acts as an intermediary between the data (the board) and the view
 * (the UI).
 *
 * @author Nick Houser
 */
public class BoardController {

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(BoardController.class.getName());

    //the board that is being played
    private Board board;

    //the view for the board
    private BoardPane boardPane;

    //the view for the menu
    private MenuPane menuPane;

    /**
     * Constructor which creates a new board.
     */
    public BoardController() {
        board = new Board(Board.initialState(), Color.WHITE);
    }

    /**
     * Setter method for the board veiw.
     *
     * @param boardPane the board view to set
     */
    public void setBoardView(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * The setter method for the menu view.
     *
     * @param menuPane the menu view to set
     */
    public void setMenuView(MenuPane menuPane) {
        this.menuPane = menuPane;
    }

    /**
     * Getter method for the occupant of a board square.
     *
     * @param x the x co-ordinate to get
     * @param y the y co-ordinate to get
     * @return the Piece at the given square
     */
    public Piece occupant(int x, int y) {
        return board.square(x, y).occupant();
    }

    /**
     * Sets the board to a new board and refreshes the board view.
     */
    public void reset() {
        LOGGER.log(Level.INFO, "Starting a new game");

        board = new Board(Board.initialState(), Color.WHITE);
        boardPane.refreshSquares();
    }

    /**
     * Reverts the last move made and refreshes the board view.
     */
    public void revert() {
        GenericMove lastMove = board.history().last();

        LOGGER.log(Level.INFO, "Reverting last move ({0})", lastMove);

        lastMove.revert();
        boardPane.refreshSquares();
    }

    /**
     * Attempts to make a move based on user input and returns whether the
     * attempt succeeded.
     *
     * @param selection the piece to move
     * @param xTo the x co-ordinate of the move target
     * @param yTo the y co-ordinate of the move target
     * @return whether the move succeeded
     */
    public boolean humanMove(Piece selection, int xTo, int yTo) {
        LOGGER.log(Level.INFO, "Trying human move");

        GenericMove attempt = MoveFactory.create(board, selection, xTo, yTo);
        boolean success = move(attempt);
        if (success) {
            autoMove();
        }
        return success;
    }

    /**
     * Attempts to make a move based on AI calculation and returns whether the
     * attempt succeeded. AI calculation is done in a thread to avoid locking
     * the UI.
     */
    public void aiMove() {
        LOGGER.log(Level.INFO, "Trying computer move");

        menuPane.lock();
        AiMoveTask computation = new AiMoveTask(board);
        computation.setOnSucceeded(e -> {
            move(computation.getValue());
            autoMove();
        });
        new Thread(computation).start();
    }

    /**
     * Method which triggers an AI move if the user has enabled auto AI moves
     * for the active color.
     */
    private void autoMove() {
        if (menuPane.aiEnabled(board.activePlayer())) {
            aiMove();
        }
    }

    /**
     * Attempts to make a move and update the views to reflect the new board
     * state. Handles on completion actions for the move, including making an
     * automatic AI move if the user wishes. Returns whether the move succeeded.
     *
     * @param toExecute the move to try to make
     * @return whether the move succeeded
     */
    private boolean move(GenericMove toExecute) {
        LOGGER.log(Level.INFO, "Move = {0}", toExecute);

        boolean success = toExecute.execute();
        if (success) {
            boardPane.refreshSquares();
            menuPane.unlock();

            LOGGER.log(Level.INFO, "Success");
            LOGGER.log(Level.FINE, "Value after move: {0}", new PositionAnalysis(board).value);
        } else {
            LOGGER.log(Level.INFO, "Failure");
        }

        return success;
    }
}
