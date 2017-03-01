/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.pieces;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import java.util.ArrayList;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.MoveFactory;
import java.util.List;

/**
 * Class representing a King, a piece which can move to any adjacent square and
 * which must be captured to win the game.
 *
 * @author Nick Houser
 */
public class King extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public King(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which returns a list of valid moves for the King. Kings can move
     * to any adjacent square, including diagonally.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<GenericMove> validMoves(Board board) {
        List<GenericMove> toReturn = new ArrayList<>();
        int addX;
        int addY;

        for (Direction dir : Direction.values()) {
            addX = position().x + dir.x();
            addY = position().y + dir.y();
            MoveFactory.addIfValid(toReturn, board, this, addX, addY, true, true);
        }

        Direction[] castleDirs = {Direction.EAST, Direction.WEST};
        for (Direction castleDir : castleDirs) {
            if (canCastle(board, castleDir)) {
                addX = position().x + (2 * castleDir.x());
                addY = position().y;
                MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
            }
        }

        return toReturn;
    }

    /**
     * Method which adds all relevant positional rules to this piece.
     */
    @Override
    public void addPositionalRules() {
    }

    /**
     * Method which returns the material value for the piece, which is a number
     * representing the worth of the type of piece.
     *
     * @return the material value of the piece
     */
    @Override
    public double materialValue() {
        return 0.0;
    }

    /**
     * Method which returns a copy of the Piece.
     *
     * @return a copy of this Piece
     */
    @Override
    public King copy() {
        return new King(color, position().x, position().y);
    }

    /**
     * Method which checks whether the king is being threatened. This is used to
     * determine move validity.
     *
     * @param board board on which the king resides
     * @return true if the king is under threat, false otherwise
     */
    public boolean threatened(Board board) {
        //knights
        int[] ones = {-1, 1};
        int[] twos = {-2, 2};
        for (int one : ones) {
            for (int two : twos) {
                Piece occupant;
                if (Board.inBounds(position().x + one, position().y + two)) {
                    occupant = board.occupant(position().x + one, position().y + two);
                    if (occupant != null && occupant instanceof Knight && occupant.color != color) {
                        return true;
                    }
                }
                if (Board.inBounds(position().x + two, position().y + one)) {
                    occupant = board.occupant(position().x + two, position().y + one);
                    if (occupant != null && occupant instanceof Knight && occupant.color != color) {
                        return true;
                    }
                }
            }
        }

        //pieces in straight lines from king
        for (Direction direction : Direction.values()) {
            int col = position().x + direction.x();
            int row = position().y + direction.y();

            while (Board.inBounds(col, row)) {
                Piece occupant = board.occupant(col, row);
                if (occupant != null) {
                    if (occupant.color != color) {
                        if (occupant instanceof Queen) {
                            return true;
                        } else if (occupant instanceof King) {
                            int deltaX = Math.abs(occupant.position().x - position().x);
                            int deltaY = Math.abs(occupant.position().y - position().y);
                            if (deltaX <= 1 && deltaY <= 1) {
                                return true;
                            }
                        } else if (occupant instanceof Pawn) {
                            int deltaX = Math.abs(occupant.position().x - position().x);
                            int yAfterCapture = occupant.position().y + occupant.color.forwardDirection().y();
                            if (yAfterCapture == position().y && deltaX == 1) {
                                return true;
                            }
                        } else if (occupant instanceof Rook) {
                            if (direction == Direction.NORTH || direction == Direction.SOUTH || direction == Direction.EAST || direction == Direction.WEST) {
                                return true;
                            }
                        } else if (occupant instanceof Bishop) {
                            if (direction == Direction.NORTHWEST || direction == Direction.SOUTHWEST || direction == Direction.NORTHEAST || direction == Direction.SOUTHEAST) {
                                return true;
                            }
                        }
                    }
                    break;
                }

                col = col + direction.x();
                row = row + direction.y();
            }
        }

        //nothing was threatening
        return false;
    }

    /**
     * Method which checks whether the active player can castle, which should be
     * true only if neither the king nor the left rook has moved, the king is
     * not in check, and the space to the side of the king is not under threat,
     * and none of the spaces required are occupied.
     *
     * @param board the board on which the king resides
     * @param direction the direction in which to castle; must be east or west
     * @return true if the active player can castle left, false otherwise
     * @throws IllegalArgumentException if passed a direction not east or west
     */
    private boolean canCastle(Board board, Direction direction) {
        if (direction != Direction.WEST && direction != Direction.EAST) {
            throw new IllegalArgumentException("Tried to castle in a direction other than left or right.");
        }

        int xIndex = 4;
        if (position().x != xIndex || position().y != color.homeRow()) {
            return false;
        }

        if (hasMoved()) {
            return false;
        }

        while (Board.inBounds(xIndex + (2 * direction.x()), 0)) {
            xIndex = xIndex + direction.x();
            if (board.occupant(xIndex, color.homeRow()) != null) {
                return false;
            }
        }

        if (MoveFactory.create(board, this, position().x, position().y).endangersKing()) {
            return false;
        }

        if (MoveFactory.create(board, this, position().x + direction.x(), position().y).endangersKing()) {
            return false;
        }

        xIndex = xIndex + direction.x();
        Piece rookSquareOccupant = board.occupant(xIndex, color.homeRow());
        if (rookSquareOccupant == null || rookSquareOccupant.hasMoved()) {
            return false;
        }

        return true;
    }
}
