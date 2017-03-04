/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nick Houser
 */
public class AnalysisNode {

    //the expected value of this position
    private final double value;

    //the move that must be made to reach this state from the parent state
    public final GenericMove moveToReach;

    //the parent of this node
    private AnalysisNode parent;

    //the children of this node
    private List<AnalysisNode> children;

    //the best possible response from the opponent to this position
    private AnalysisNode worstCaseChild;

    //the color of the active player
    private Color activePlayer;

    /**
     * Constructor which saves a board and initializes variables.
     *
     * @param board the board state at this node
     * @param move the move required to reach this board from the parent state
     */
    public AnalysisNode(Board board, GenericMove move) {
        children = new LinkedList<>();
        parent = null;
        moveToReach = move;
        worstCaseChild = null;
        activePlayer = board.activePlayer();
        value = new PositionAnalysis(board).value;
    }

    /**
     * Method which adds a child.
     *
     * @param child the child to add
     */
    public void addChild(AnalysisNode child) {
        children.add(child);
        child.parent = this;
    }

    /**
     * Method which removes a child.
     *
     * @param child the child to remove
     */
    public void removeChild(AnalysisNode child) {
        children.remove(child);
    }

    /**
     * Getter method for parent.
     *
     * @return the parent node
     */
    public AnalysisNode parent() {
        return parent;
    }

    /**
     * Getter method for children.
     *
     * @return a list of children
     */
    public List<AnalysisNode> children() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Method which returns the value of this move assuming the opponent makes
     * the best possible response.
     *
     * @return the value of this move assuming the opponent makes the best
     * possible response
     */
    public Double worstCaseValue() {
        if (worstCaseChild == null) {
            return value;
        }

        return worstCaseChild.worstCaseValue();
        //return worstCaseValue;
    }

    /**
     * Method which returns the best possible response to this move.
     *
     * @return an AnalysisNode representing the best possible response
     */
    public AnalysisNode worstCaseChild() {
        return worstCaseChild;
    }

    /**
     * Method which sets the value of this move assuming the opponent makes the
     * best possible response.
     *
     * @param toSet the new worst case value
     */
    public void setWorstCaseChild(AnalysisNode toSet) {
        if (worstCaseChild == null) {
            worstCaseChild = toSet;
        } else if (toSet.betterThan(worstCaseChild)) {
            worstCaseChild = toSet;
        }
    }

    /**
     * Method which allows comparison to determine which node is a better move
     * for the active player.
     *
     * @param o the other node to compare
     * @return 1 if the other node is better than this one, -1 otherwise
     */
    public boolean betterThan(AnalysisNode o) {
        if (activePlayer == Color.WHITE) {
            return (worstCaseValue() < o.worstCaseValue());
        } else {
            return (worstCaseValue() > o.worstCaseValue());
        }
    }
}
