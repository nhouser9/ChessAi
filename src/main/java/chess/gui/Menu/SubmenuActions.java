/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui.Menu;

import chess.gui.BoardController;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Extension of GridPane which contains the menu items for the GUI.
 *
 * @author Nick Houser
 */
public class SubmenuActions extends GridPane {

    //the current game board controller
    private BoardController controller;

    //the index on the UI
    private int gridIndex;

    //ui elements
    private Button newGame;
    private Button undoMove;

    /**
     * Constructor which initializes subcomponents.
     *
     * @param controller the game board controller
     */
    public SubmenuActions(BoardController controller) {
        this.controller = controller;
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(100);
        getColumnConstraints().add(constraints);
        gridIndex = 0;
        addUiElements();
    }

    /**
     * Adds the UI controls.
     */
    private void addUiElements() {
        addActionsHeader();
        addNewGameButton();
        addUndoMoveButton();
    }

    /**
     * Adds the header label for the actions.
     */
    private void addActionsHeader() {
        Label title = new Label("Actions");
        title.setAlignment(Pos.TOP_CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        add(title, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Adds the new game button.
     */
    private void addNewGameButton() {
        newGame = new Button("New Game");
        newGame.setMaxWidth(Double.MAX_VALUE);

        newGame.setOnAction((ActionEvent t) -> {
            controller.reset();
        });

        add(newGame, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Adds the undo move button.
     */
    private void addUndoMoveButton() {
        undoMove = new Button("Undo");
        undoMove.setMaxWidth(Double.MAX_VALUE);

        undoMove.setOnAction((ActionEvent t) -> {
            controller.revert();
        });

        add(undoMove, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Locks all of the UI buttons during humanMove calculation.
     */
    public void lock() {
        newGame.setDisable(true);
        undoMove.setDisable(true);
    }

    /**
     * Unlocks all of the UI buttons after humanMove calculation.
     */
    public void unlock() {
        newGame.setDisable(false);
        undoMove.setDisable(false);
    }
}
