package net.whg.nghaste;

/**
 * An axiom is a validator rule for confirming whether a graph is considered
 * fully valid or not. This rule is applied to the search tree and tested for
 * every single graph in the tree to decide which directions are worth pursing
 * or which directions are confirmed to contain no validate solutions.
 */
public interface IAxiom
{
    /**
     * Checks whether or not a graph is valid or not. This function is called on
     * every graph witihin the search tree as it is discovered. A graph is
     * considered invalid if there are no new connections or nodes which can be
     * added to create a valid solution.
     * <p>
     * This function is also called on solution graphs to check whether or not they
     * are valid, following the same rules.
     * 
     * @param graph
     *     - The graph to validate.
     * @return True if the graph is valid, false otherwise.
     */
    boolean isValid(NodeGraph graph);
}
