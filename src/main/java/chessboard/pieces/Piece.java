/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
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
    public abstract List<Point> validMoves(Board board);

    /**
     * Helper method which adds all of the moves for a Piece in one straight
     * direction.
     *
     * @param moves the list of possible moves to add to
     * @param board the board on which the move will be made
     * @param direction the direction in to search for moves
     */
    protected void addDirectionalMoves(List<Point> moves, Board board, Direction direction) {
        int col = position().x;
        int row = position().y;

        col = col + direction.x();
        row = row + direction.y();

        while (Board.inBounds(col, row)) {
            Point move = new Point(col, row);

            if (addSquareToMoves(moves, board, move)) {
                break;
            }

            col = col + direction.x();
            row = row + direction.y();
        }
    }

    /**
     * Helper method which adds a square to a list of moves if it is not
     * blocked, and returns whether the search along that line should cease.
     *
     * @param moves the list of moves to add to if valid
     * @param board the board to check the move on
     * @param square the square to check
     * @return true if a blocker was encountered, false otherwise
     */
    private boolean addSquareToMoves(List<Point> moves, Board board, Point square) {
        Piece occupant = board.occupant(square.x, square.y);
        if (occupant == null) {
            moves.add(square);
            return false;
        } else {
            if (occupant.color != color) {
                moves.add(square);
            }
            return true;
        }
    }

    /**
     * Helper method which adds a move to a list of moves if it is on the board
     * and not occupied by an ally.
     *
     * @param moves the list of moves to add to if valid
     * @param board the board to check the move on
     * @param square the square to check
     * @param addEmpty whether to add the move if the target is empty
     * @param addEnemy whether to add the move if the target contains an enemy
     */
    protected void addIfValid(List<Point> moves, Board board, Point square, boolean addEmpty, boolean addEnemy) {
        if (!Board.inBounds(square.x, square.y)) {
            return;
        }

        Piece occupant = board.occupant(square.x, square.y);
        if (occupant != null) {
            if (occupant.color != color && addEnemy) {
                moves.add(square);
            }
        } else if (addEmpty) {
            moves.add(square);
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

    /**
     * Method which returns a copy of this piece.
     *
     * @return a copy of this piece
     */
    public Piece copyOf() {
        Piece copy = null;

        if (this instanceof Queen) {
            copy = new Queen(color, position().x, position().y);
        }
        if (this instanceof Pawn) {
            copy = new Pawn(color, position().x, position().y);
        }
        if (this instanceof King) {
            copy = new King(color, position().x, position().y);
        }
        if (this instanceof Knight) {
            copy = new Knight(color, position().x, position().y);
        }
        if (this instanceof Bishop) {
            copy = new Bishop(color, position().x, position().y);
        }
        if (this instanceof Rook) {
            copy = new Rook(color, position().x, position().y);
        }

        copy.moveCount = moveCount;

        return copy;
    }
}
