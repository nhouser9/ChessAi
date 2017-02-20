/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.moves.GenericMove;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Nick Houser
 */
public class AnalysisNode {

    //the expected value of this position
    public final double value;

    //the move that must be made to reach this state from the parent state
    public final GenericMove moveToReach;

    //the parent of this node
    private AnalysisNode parent;

    //the children of this node
    private List<AnalysisNode> children;

    //the value of this move assuming the best possible response from the opponent
    private Double worstCaseValue;

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
        value = new PositionAnalysis(board).value;
        worstCaseValue = null;
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
        return worstCaseValue;
    }

    /**
     * Method which sets the value of this move assuming the opponent makes the
     * best possible response.
     *
     * @param toSet the new worst case value
     */
    public void setWorstCaseValue(double toSet) {
        worstCaseValue = toSet;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.moveToReach);
        return hash;
    }
}
