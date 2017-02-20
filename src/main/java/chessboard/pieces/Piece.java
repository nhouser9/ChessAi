/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import chessboard.moves.Capture;
import chessboard.moves.GenericMove;
import chessboard.moves.MoveFactory;
import chessboard.moves.NormalMove;
import java.awt.Point;
import java.util.List;

/**
 * Abstract class representing a generic chess piece. Will be implemented by
 * specific peices.
 *
 * @author Nick Houser
 */
public abstract class Piece {

    //the color of the piece
    public final Color color;

    //the location of the piece
    private Point position;

    //variable indicating whether this piece has moved
    private int moveCount;

    /**
     * Constructor which saves the color of the peice.
     *
     * @param color the color of the peice
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Piece(Color color, int xPosition, int yPosition) {
        this.color = color;
        position = new Point(xPosition, yPosition);
        moveCount = 0;
    }

    /**
     * Getter method for position.
     *
     * @return the position of the piece
     */
    public Point position() {
        return position;
    }

    /**
     * Setter method for position.
     *
     * @param newPos the new position
     */
    public void setPosition(Point newPos) {
        position = newPos;
    }

    /**
     * Method which adds one to the move count.
     */
    public void addMoveCount() {
        moveCount = moveCount + 1;
    }

    /**
     * Method which subtracts one from the move count.
     */
    public void subMoveCount() {
        moveCount = moveCount - 1;
    }

    /**
     * Checks whether this piece has been moved.
     *
     * @return true if the piece has moved, false otherwise
     */
    public boolean hasMoved() {
        return moveCount > 0;
    }

    /**
     * Method which returns a list of valid moves for the piece.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    public abstract List<GenericMove> validMoves(Board board);

    /**
     * Helper method which adds all of the moves for a Piece in one straight
     * direction.
     *
     * @param moves the list of possible moves to add to
     * @param board the board on which the move will be made
     * @param direction the direction in to search for moves
     */
    protected void addDirectionalMoves(List<GenericMove> moves, Board board, Direction direction) {
        int col = position().x + direction.x();
        int row = position().y + direction.y();

        while (Board.inBounds(col, row)) {
            Piece occupant = board.occupant(col, row);
            if (occupant != null) {
                MoveFactory.addIfValid(moves, board, this, col, row, true, true);
                break;
            }

            MoveFactory.addIfValid(moves, board, this, col, row, true, true);

            col = col + direction.x();
            row = row + direction.y();
        }
    }

    /**
     * Checks whether this piece is equal to another.
     *
     * @param other the other object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other.getClass() != getClass()) {
            return false;
        }

        Piece otherPiece = (Piece) other;

        if (otherPiece.color != color) {
            return false;
        }

        if (!otherPiece.position().equals(position)) {
            return false;
        }

        if (otherPiece.moveCount != moveCount) {
            return false;
        }

        return true;
    }
}
