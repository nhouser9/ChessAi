/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui.Menu;

import chess.gui.BoardController;
import javafx.scene.layout.GridPane;

/**
 * Extension of GridPane which contains the menu items for the GUI.
 *
 * @author Nick Houser
 */
public class MenuPane extends GridPane {

    //the submenus
    private SubmenuActions menuActions;
    private SubmenuAi menuAi;

    /**
     * Constructor which initializes subcomponents.
     *
     * @param controller the game board controller
     */
    public MenuPane(BoardController controller) {
        controller.setMenuView(this);
        addUiElements(controller);
    }

    /**
     * Adds the UI controls.
     */
    private void addUiElements(BoardController controller) {
        menuActions = new SubmenuActions(controller);
        add(menuActions, 0, 0);
        menuAi = new SubmenuAi(controller);
        add(menuAi, 0, 1);
    }

    /**
     * Returns whether the AI is turned on for the passed color.
     *
     * @param color the color to check for AI
     * @return whether the AI is turned on for the passed color
     */
    public boolean aiEnabled(chess.chessboard.Color color) {
        return menuAi.aiEnabled(color);
    }

    /**
     * Locks all of the UI buttons during humanMove calculation.
     */
    public void lock() {
        menuActions.lock();
        menuAi.lock();
    }

    /**
     * Unlocks all of the UI buttons after humanMove calculation.
     */
    public void unlock() {
        menuActions.unlock();
        menuAi.unlock();
    }
}
