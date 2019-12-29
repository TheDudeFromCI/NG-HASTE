package net.whg.nghaste;

/**
 * A heuristic, in this context, is used to decide how likely a given node graph
 * is to contain a child graph with a valid solution. This function is applied
 * to all node graphs as they are discovered to organize where they should all
 * within the search queue. Multiple heuristics can all exist in a search tree
 * at once, where the final heuristic for a graph is the sum of all provided
 * heuristics.
 */
public interface IHeuristic
{
    /**
     * The function is used to estimate the heuristic score of a given graph. A
     * heuristic score is used to determine how likely the given graph is to contain
     * a valid solution. Graphs with a higher heuristic score are processed first,
     * while graphs with a lower score are processed last. The score itself is used
     * solely for sorting purposes, and can be any value.
     * <p>
     * If multiple heuristic rules are present in a search tree, the final heuristic
     * score of a node graph is the sum of all heuristic scores.
     * 
     * @param graph
     *     - The graph to check.
     * @return The estimated heuristic score.
     */
    float estimateHeuristic(NodeGraph graph);
}
