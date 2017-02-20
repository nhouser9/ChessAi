/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import ai.MoveSelector;
import chessboard.Board;
import chessboard.Color;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Extension of GridPane which contains the menu items for the GUI.
 *
 * @author Nick Houser
 */
public class MenuPane extends GridPane {

    //the current game board
    private Board gameBoard;

    //the panel which displays the game board
    private final BoardPane boardPane;

    /**
     * Constructor which initializes subcomponents.
     *
     * @param boardPane the UI pane that represents the board
     */
    public MenuPane(BoardPane boardPane) {
        this.boardPane = boardPane;

        newGame();

        Button newGame = new Button("New Game");
        Button computerMove = new Button("AI Move");
        Button undoMove = new Button("Undo");

        newGame.setMaxWidth(Double.MAX_VALUE);
        computerMove.setMaxWidth(Double.MAX_VALUE);
        undoMove.setMaxWidth(Double.MAX_VALUE);

        newGame.setOnAction((ActionEvent t) -> {
            newGame();
        });

        computerMove.setOnAction((ActionEvent t) -> {
            gameBoard.move(new MoveSelector(gameBoard, 4).bestMove());
            boardPane.refreshSquares();
        });

        undoMove.setOnAction((ActionEvent t) -> {
            gameBoard.revertMove();
            boardPane.refreshSquares();
        });

        add(newGame, 0, 0);
        add(computerMove, 0, 1);
        add(undoMove, 0, 2);
    }

    /**
     * Helper method which starts a new game and updates the UI.
     */
    private void newGame() {
        gameBoard = new Board(Board.initialState(), Color.WHITE);
        boardPane.setBoard(gameBoard);
        boardPane.refreshSquares();
    }
}
