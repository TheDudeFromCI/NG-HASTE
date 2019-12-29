package net.whg.nghaste;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to build an environment object with the given settings. As
 * environment objects are intended to be immutable, this builder object exists
 * to aid in the design of larger and more complex instances of environments.
 */
public class EnvironmentBuilder
{
    private final List<IFunction> functions = new ArrayList<>();
    private final List<IAxiom> axioms = new ArrayList<>();
    private final List<IAxiom> solutionAxioms = new ArrayList<>();
    private final List<IHeuristic> heuristics = new ArrayList<>();
    private int depth = 20;

    /**
     * Sets the maximum depth to use when transversing the search tree. In a graph,
     * the depth of a graph is determined by the number of connections which exist
     * within that graph. Larger values allow for more complicated solutions to be
     * found, but use an expotentionally larger search space.
     * 
     * @param depth
     *     - The new depth value. Defaults to 20.
     * @return This object for chaining.
     * @throws IllegalArgumentException
     *     If "depth" is <= 0.
     */
    public EnvironmentBuilder setMaxDepth(int depth)
    {
        if (depth <= 0)
            throw new IllegalArgumentException("nDepth value must be > 0!");

        this.depth = depth;

        return this;
    }

    /**
     * Adds an axiom to validate newly discovered graphs. This method does not
     * validate graphs which have already been discovered and are pending
     * processing.
     * <p>
     * A graph is considered invalidate if at least one axiom marks the graph as
     * invalid.
     * 
     * @param axiom
     *     - The axiom to add.
     * @return This object for chaining.
     */
    public EnvironmentBuilder addAxiom(IAxiom axiom)
    {
        axioms.add(axiom);

        return this;
    }

    /**
     * Adds a axiom which is only executed on solutions. This is used as a final
     * filter to determine whether or not a solution actually meets the given
     * requirements needed to solve the problem. This method does not validate any
     * graphs which are already present in the node container.
     * <p>
     * A graph is considered invalidate if at least one axiom marks the graph as
     * invalid.
     * 
     * @param axiom
     *     - The axiom to add.
     * @return This object for chaining.
     */
    public EnvironmentBuilder addSolutionAxiom(IAxiom axiom)
    {
        solutionAxioms.add(axiom);

        return this;
    }

    /**
     * Adds a new heuristic to optimize the routes in which graph pathways are
     * explored. This function is called only on non-solution graphs as they are
     * discovered. If multiple heuristics are present, the sum of all heuristics are
     * added to find the final heuristic score for a graph.
     * 
     * @param heuristic
     *     - The heuristic to add.
     * @return This object for chaining.
     */
    public EnvironmentBuilder addHeuristic(IHeuristic heuristic)
    {
        heuristics.add(heuristic);

        return this;
    }

    /**
     * Adds a new function to this environment. A function is a node type which can
     * be utilized for deciding what operations are available to the algorithm to
     * use in solutions.
     * 
     * @param function
     *     - The function to add.
     * @return This object for chaining.
     */
    public EnvironmentBuilder addFunction(IFunction function)
    {
        functions.add(function);

        return this;
    }

    /**
     * Builds the new environment given the current state of this builder.
     * 
     * @return A new environment object.
     * @throws IllegalArgumentException
     *     If the functions list does not contain at least one input function and
     *     exactly one output function.
     */
    public Environment build()
    {
        return new Environment(functions, axioms, solutionAxioms, heuristics, depth);
    }
}
