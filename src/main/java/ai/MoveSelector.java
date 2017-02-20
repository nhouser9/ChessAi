/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import chessboard.Board;
import chessboard.Color;
import chessboard.moves.Move;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a tree which will analyze and select all possible board
 * states that are reachable after a given number of moves.
 *
 * @author Nick Houser
 */
public class MoveSelector {

    //the root of the tree
    private AnalysisNode rootNode;

    //the depth of the tree
    //private int depth;
    /**
     * Constructor which builds the full tree to the specified move depth.
     *
     * @param toAnalyze the board to analyze
     * @param depth the number of moves ahead to analyze
     */
    public MoveSelector(Board toAnalyze, int depth) {
        Board analysisCopy = toAnalyze.copyOf();
        rootNode = new AnalysisNode(analysisCopy, null);
        addChildren(analysisCopy, rootNode, 0, depth);
        calculateWorstCaseValues();
    }

    /**
     * Recursive method which adds all children at the current level, and
     * recurses to ask them to do the same.
     *
     * @param analysisCopy the board to analyze
     * @param node the analysis node that children should be added to
     * @param currentDepth the current number of moves away from starting state
     * @param targetDepth the target number of moves away from starting state
     */
    private void addChildren(Board analysisCopy, AnalysisNode node, int currentDepth, int targetDepth) {
        if (currentDepth == targetDepth) {
            return;
        }

        for (Move possible : analysisCopy.validMoves()) {
            analysisCopy.move(possible);

            AnalysisNode child = new AnalysisNode(analysisCopy, possible);
            node.addChild(child);
            addChildren(analysisCopy, child, currentDepth + 1, targetDepth);

            analysisCopy.revertMove();
        }
    }

    /**
     * Method which returns the best possible next move.
     *
     * @return the best possible next move
     */
    public Move bestMove() {
        for (AnalysisNode child : rootNode.children()) {
            if (child.worstCaseValue().equals(rootNode.worstCaseValue())) {
                return child.moveToReach;
            }
        }
        return null;
    }

    /**
     * Helper method which calculates the worst case values for all of the nodes
     * in the tree. This value is the value of each node assuming the opponent
     * makes the best possible response.
     */
    private void calculateWorstCaseValues() {
        Collection<AnalysisNode> toPushUp = leaves();
        Collection<AnalysisNode> parents = new HashSet<>();

        for (AnalysisNode leaf : toPushUp) {
            leaf.setWorstCaseValue(leaf.value);
        }

        while (!toPushUp.isEmpty()) {
            for (AnalysisNode node : toPushUp) {
                pushUpWorstCase(node);
                if (node.parent() != null && !parents.contains(node.parent())) {
                    parents.add(node.parent());
                }
            }
            toPushUp = parents;
            parents = new HashSet<>();
        }
    }

    /**
     * Method which returns all of the leaves of the current tree.
     *
     * @return all leaves of the current tree
     */
    private List<AnalysisNode> leaves() {
        List<AnalysisNode> leaves = new LinkedList<>();

        recursiveLeafSearch(leaves, rootNode);

        return leaves;
    }

    /**
     * Helper method which recursively searches child nodes for leaves of the
     * tree.
     *
     * @param leaves the list to add leaves to
     * @param current the current node to search
     */
    private void recursiveLeafSearch(List<AnalysisNode> leaves, AnalysisNode current) {
        if (current.children().isEmpty()) {
            leaves.add(current);
        } else {
            for (AnalysisNode child : current.children()) {
                recursiveLeafSearch(leaves, child);
            }
        }
    }

    /**
     * Method which pushes up the worst case values for each leaf as if the best
     * possible move for each side was chosen.
     *
     * @param leaf the leaf to push up
     */
    private void pushUpWorstCase(AnalysisNode leaf) {
        AnalysisNode parent = leaf.parent();
        if (parent != null) {
            Double parentWorstCase = parent.worstCaseValue();
            if (parentWorstCase == null) {
                parentWorstCase = leaf.worstCaseValue();
            } else if (leaf.moveToReach.piece.color == Color.WHITE) {
                parentWorstCase = Math.max(parentWorstCase, leaf.worstCaseValue());
            } else {
                parentWorstCase = Math.min(parentWorstCase, leaf.worstCaseValue());
            }
            parent.setWorstCaseValue(parentWorstCase);
        }
    }
}
