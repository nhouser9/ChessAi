/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.chessboard.Board;
import chess.chessboard.moves.GenericMove;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a tree which will analyze and select all possible board
 * states that are reachable after a given number of moves.
 *
 * @author Nick Houser
 */
public class MoveSelector {

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(MoveSelector.class.getName());

    //the game board on which to actually make the move
    private Board original;

    //the game board to analyze
    private Board analysisCopy;

    //the root of the tree
    private AnalysisNode rootNode;

    //the analysis depth to use for selection
    private int depth;

    /**
     * Constructor which builds the full tree to the specified move depth.
     *
     * @param toAnalyze the board to analyze
     * @param depth the number of moves ahead to analyze
     */
    public MoveSelector(Board toAnalyze, int depth) {
        original = toAnalyze;
        analysisCopy = toAnalyze.copyOf();
        rootNode = new AnalysisNode(toAnalyze, null);
        this.depth = depth;
    }

    /**
     * Calculates the best move.
     *
     * @return the best move on the given board
     */
    public GenericMove selectMove() {
        addChildren(analysisCopy, rootNode, 0, depth);
        AnalysisNode child = rootNode.worstCaseChild();

        LOGGER.log(Level.FINE, "Worst case value of best move: {0}", rootNode.worstCaseValue());
        while (child != null) {
            LOGGER.log(Level.FINER, "Next move in best sequence = {0}", child.moveToReach);
            child = child.worstCaseChild();
        }

        child = rootNode.worstCaseChild();
        if (child == null) {
            return null;
        }

        GenericMove move = child.moveToReach;
        move.changeBoard(original);
        return move;
    }

    /**
     * Recursive method which adds all children at the current level, and
     * recurses to ask them to do the same. Also updates the worst case value to
     * the worst case value of the worst child.
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

        for (GenericMove possible : analysisCopy.validMoves()) {
            possible.execute();

            AnalysisNode child = new AnalysisNode(analysisCopy, possible);
            node.addChild(child);
            addChildren(analysisCopy, child, currentDepth + 1, targetDepth);

            node.setWorstCaseChild(child);

            possible.revert();
        }
    }
}
