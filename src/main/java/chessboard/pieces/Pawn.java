/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import chessboard.moves.GenericMove;
import chessboard.moves.MoveFactory;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Pawn, a piece which can only move forward. It can move
 * two moves if it has never moved, or one otherwise. It can only move forward
 * if unblocked, and may only capture diagonally.
 *
 * @author Nick Houser
 */
public class Pawn extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Pawn(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which returns a list of valid moves for the Pawn. Pawns can move
     * only forward one square at a time, except for on the first move, on which
     * they can move two squares. Pawns cannot capture while moving forward,
     * they can only capture diagonally.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<GenericMove> validMoves(Board board) {
        //set up variables
        List<GenericMove> toReturn = new ArrayList<>();
        int addX;
        int addY;

        //add normal move
        addX = position().x;
        addY = position().y + color.forwardDirection().y();
        MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);

        //add capture move
        for (Direction diagonal : color.forwardDiagonals()) {
            addX = position().x + diagonal.x();
            addY = position().y + diagonal.y();
            MoveFactory.addIfValid(toReturn, board, this, addX, addY, true, false);
        }

        //add double move
        Piece blocker = board.occupant(position().x, position().y + color.forwardDirection().y());
        if (blocker == null && position().y == color.pawnRow()) {
            addX = position().x;
            addY = position().y + (2 * color.forwardDirection().y());
            MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
        }

        //add en passant
        GenericMove lastMove = board.lastMove();
        if (lastMove != null) {
            boolean enemyMoveAllowsEnPassant = true;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && lastMove.piece instanceof Pawn;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && lastMove.piece.color != color;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && lastMove.to.y == position().y;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && Math.abs(lastMove.to.x - position().x) == 1;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && lastMove.from.x == lastMove.to.x;
            enemyMoveAllowsEnPassant = enemyMoveAllowsEnPassant && Math.abs(lastMove.from.y - lastMove.to.y) == 2;
            if (enemyMoveAllowsEnPassant) {
                addX = lastMove.to.x;
                addY = position().y + color.forwardDirection().y();
                MoveFactory.addIfValid(toReturn, board, this, addX, addY, false, true);
            }
        }

        //return
        return toReturn;
    }
}
