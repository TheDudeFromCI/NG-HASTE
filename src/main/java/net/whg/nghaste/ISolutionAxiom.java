package net.whg.nghaste;

import net.whg.nghaste.impl.NodeGraph;

/**
 * A solution axiom is a validator rule for confirming whether a graph is
 * considered fully valid solution or not. This rule is applied to the search
 * tree and tested for every proposed solution to determine whether it meets all
 * requirements and passes all tests.
 * <p>
 * A search tree can contain any number of solution axioms, where each solution
 * axiom represents a single test or goal the solution must meet to be
 * considered valid.
 */
public interface ISolutionAxiom
{
    /**
     * Checks whether a solution is valid or not. This function is called on every
     * solution witihin the search tree output as it is discovered. A solution is
     * considered invalid if it does not meet the requirements or goals to be
     * considered a valid solution. A solution is considered invalid if at least one
     * solution marks it as invalid.
     * 
     * @param graph
     *     - The graph to validate.
     * @return True if the solution is valid, false otherwise.
     */
    boolean isValid(NodeGraph graph);
}
