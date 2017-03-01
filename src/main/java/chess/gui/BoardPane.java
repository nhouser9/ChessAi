/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui;

import chess.chessboard.Board;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import chess.chessboard.pieces.Piece;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * Extension of GridPane which handles displaying the board and processing
 * clicks on it.
 *
 * @author Nick Houser
 */
public class BoardPane extends GridPane {

    //the game board controller to be displayed in this window
    private BoardController controller;

    //graphical representation of squares for the pieces on the board
    private BoardSquare[][] squares;

    //the selected piece
    private Piece selection;

    /**
     * Constructor which initializes each board square on the board.
     *
     * @param controller the game board controller
     */
    public BoardPane(BoardController controller) {
        this.controller = controller;
        controller.setBoardView(this);
        setAlignment(Pos.CENTER);
        addBoardSquares();
        refreshSquares();
    }

    /**
     * Helper method which refreshes the visual state of all squares on the
     * board and their occupants.
     */
    public void refreshSquares() {
        for (int refreshCol = 0; refreshCol < squares.length; refreshCol++) {
            for (int refreshRow = 0; refreshRow < squares.length; refreshRow++) {
                Piece occupant = controller.occupant(refreshCol, refreshRow);
                squares[refreshCol][refreshRow].refreshState(occupant);
            }
        }
    }

    /**
     * Helper method which adds the grid squares to the GridPane.
     *
     * @param grid the GridPane to add to
     */
    private void addBoardSquares() {
        Image[] backgrounds = {loadImage("LightSquare.png"), loadImage("DarkSquare.png")};
        int backgroundIndex = 0;

        squares = new BoardSquare[Board.SQUARES_PER_SIDE][Board.SQUARES_PER_SIDE];
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                ImageView background = new ImageView(backgrounds[backgroundIndex]);
                backgroundIndex = (backgroundIndex + 1) % backgrounds.length;

                BoardSquare toAdd = new BoardSquare(col, row, background);

                squares[col][row] = toAdd;
                add(toAdd, col, row);

                toAdd.setOnMouseClicked((MouseEvent t) -> {
                    handleClick(toAdd.col, toAdd.row);
                });
            }
            backgroundIndex = (backgroundIndex + 1) % backgrounds.length;
        }
    }

    /**
     * Helper method which loads and returns a JavaFX Image from the specified
     * path.
     *
     * @param path the String path to load the image from
     * @return the loaded Image object
     */
    private Image loadImage(String path) {
        return new Image(getClass().getClassLoader().getResource(path).toExternalForm());
    }

    /**
     * Method which handles a mouse click on a specific board square.
     *
     * @param col the column of the clicked location
     * @param row the row of the clicked location
     */
    private void handleClick(int col, int row) {
        Piece occupant = controller.occupant(col, row);
        if (selection == null) {
            selection = occupant;
        } else if (controller.humanMove(selection, col, row)) {
            selection = null;
        } else {
            selection = occupant;
        }
    }
}
