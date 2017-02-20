/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.moves.Move;
import chessboard.pieces.Bishop;
import chessboard.pieces.King;
import chessboard.pieces.Knight;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import chessboard.pieces.Queen;
import chessboard.pieces.Rook;
import java.awt.Point;

/**
 * Class which analyzes a position and assigns it a value. This is used for
 * deciding which move is best.
 *
 * @author Nick Houser
 */
public class PositionAnalysis {

    //values for all of the respective factors
    protected static final double VALUE_QUEEN = 9;
    protected static final double VALUE_PAWN = 1;
    protected static final double VALUE_KNIGHT = 3;
    protected static final double VALUE_BISHOP = 3;
    protected static final double VALUE_ROOK = 5;
    protected static final double VALUE_CHECKMATE = 10000;

    //the value of this position
    public final double value;

    /**
     * Method which returns the value of a passed position.
     *
     * @param toAnalyze the board to analyze
     */
    public PositionAnalysis(Board toAnalyze) {
        double aggregateValue = 0.0;
        aggregateValue = aggregateValue + addCheckmateValue(toAnalyze);
        if (aggregateValue == 0.0) {
            aggregateValue = aggregateValue + addMaterialValues(toAnalyze);
        }
        value = aggregateValue;
    }

    private double addCheckmateValue(Board toAnalyze) {
        Color active = toAnalyze.getActivePlayer();
        King activeKing = toAnalyze.findKing(active);
        if (activeKing == null) {
            return 0;
        }
        if (!toAnalyze.kingThreatened(activeKing)) {
            return 0;
        }
        for (Point kingMove : activeKing.validMoves(toAnalyze)) {
            if (!toAnalyze.moveSacksKing(new Move(toAnalyze, activeKing, kingMove))) {
                return 0;
            }
        }
        return VALUE_CHECKMATE * colorFactor(active.enemy());
    }

    /**
     * Method which applies the rules for material value of each piece.
     *
     * @param toAnalyze the board to analyze
     */
    private double addMaterialValues(Board toAnalyze) {
        double toReturn = 0.0;
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece occupant = toAnalyze.occupant(col, row);
                double pieceValue = 0.0;

                if (occupant == null) {
                    continue;
                } else if (occupant instanceof Queen) {
                    pieceValue = VALUE_QUEEN;
                } else if (occupant instanceof Bishop) {
                    pieceValue = VALUE_BISHOP;
                } else if (occupant instanceof Knight) {
                    pieceValue = VALUE_KNIGHT;
                } else if (occupant instanceof Pawn) {
                    pieceValue = VALUE_PAWN;
                } else if (occupant instanceof Rook) {
                    pieceValue = VALUE_ROOK;
                }

                toReturn = toReturn + (colorFactor(occupant.color) * pieceValue);
            }
        }
        return toReturn;
    }

    /**
     * Method which determines the sign of a beneficial position for the given
     * color. By convention negative values indicate a winning position for
     * black, while positive ones indicate a winning position for white.
     *
     * @param color the color to check the factor for
     * @return the sign of the color's factor
     */
    private int colorFactor(Color color) {
        if (color == Color.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }
}
