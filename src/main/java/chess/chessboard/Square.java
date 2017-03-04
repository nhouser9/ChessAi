/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.chessboard.pieces.Piece;

/**
 * Class which represents a square on the chess board. Also handles returning
 * chess notation for the square.
 *
 * @author Nick Houser
 */
public class Square {

    //the location of this square on the board
    public final int col;
    public final int row;

    //variables for the notation of this square
    private final String notation;

    //the piece on this square
    private Piece occupant;

    /**
     * Constructor which initializes the square.
     *
     * @param col the column of the square
     * @param row the row of the square
     */
    public Square(int col, int row) {
        StringBuilder notation = new StringBuilder(2);
        notation.append((char) ('a' + col));
        notation.append(8 - row);
        this.notation = notation.toString();
        occupant = null;
        this.col = col;
        this.row = row;
    }

    /**
     * Getter method for the piece on this square.
     *
     * @return the piece on this square
     */
    public Piece occupant() {
        return occupant;
    }

    /**
     * Setter method for the piece on this square.
     *
     * @param occupant the piece to add to this square
     */
    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
    }

    /**
     * Override of toString method which returns chess notation for the square.
     *
     * @return chess notation for the square
     */
    @Override
    public String toString() {
        return notation;
    }
}
