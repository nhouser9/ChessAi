/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui.Menu;

import chess.chessboard.Color;
import chess.gui.BoardController;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Extension of GridPane which contains the menu items for the GUI.
 *
 * @author Nick Houser
 */
public class SubmenuAi extends GridPane {

    //the current game board controller
    private BoardController controller;

    //the index on the UI
    private int gridIndex;

    //ui elements
    private Button computerMove;
    private Map<Color, CheckBox> aiStatus;

    /**
     * Constructor which initializes subcomponents.
     *
     * @param controller the game board controller
     */
    public SubmenuAi(BoardController controller) {
        this.controller = controller;
        aiStatus = new HashMap<>();
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
        addAiHeader();
        addComputerMoveButton();
        addWhiteAiOption();
        addBlackAiOption();
    }

    /**
     * Adds the header label for the actions.
     */
    private void addAiHeader() {
        Label title = new Label("AI Controls");
        title.setAlignment(Pos.TOP_CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        add(title, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Adds the computer move button.
     */
    private void addComputerMoveButton() {
        computerMove = new Button("AI Move");
        computerMove.setMaxWidth(Double.MAX_VALUE);

        computerMove.setOnAction((ActionEvent t) -> {
            controller.aiMove();
        });

        add(computerMove, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Adds the checkbox for enabling the AI for white.
     */
    private void addWhiteAiOption() {
        CheckBox whiteAi = new CheckBox("White");
        aiStatus.put(Color.WHITE, whiteAi);
        whiteAi.setMaxWidth(Double.MAX_VALUE);
        whiteAi.setAlignment(Pos.TOP_CENTER);
        add(whiteAi, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Adds the checkbox for enabling the AI for black.
     */
    private void addBlackAiOption() {
        CheckBox blackAi = new CheckBox("Black");
        aiStatus.put(Color.BLACK, blackAi);
        blackAi.setMaxWidth(Double.MAX_VALUE);
        blackAi.setAlignment(Pos.TOP_CENTER);
        add(blackAi, 0, gridIndex);
        gridIndex = gridIndex + 1;
    }

    /**
     * Returns whether the AI is turned on for the passed color.
     *
     * @param color the color to check for AI
     * @return whether the AI is turned on for the passed color
     */
    public boolean aiEnabled(Color color) {
        return aiStatus.get(color).isSelected();
    }

    /**
     * Locks all of the UI buttons during humanMove calculation.
     */
    public void lock() {
        computerMove.setDisable(true);
    }

    /**
     * Unlocks all of the UI buttons after humanMove calculation.
     */
    public void unlock() {
        computerMove.setDisable(false);
    }
}
