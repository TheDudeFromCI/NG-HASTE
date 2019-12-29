package net.whg.nghaste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The environment is the container for storing all information about what the
 * algorithm is trying to solve and what it's allowed to do to solve the
 * problem. This object is immuatble and is used as a reference for worker
 * threads to mine the search tree. This class is immutable after creation.
 */
public final class Environment
{
    private final List<IAxiom> axioms;
    private final List<IAxiom> solutionAxioms;
    private final List<IHeuristic> heuristics;
    private final List<IFunction> functions;
    private final int nDepth;

    /**
     * Creates a new environment object. This method creates an unmodifiable copy of
     * all lists to make this object immutable.
     * 
     * @param functions
     *     - A list of functions. Must contain exactly one output function and at
     *     least one input function.
     * @param axioms
     *     - A list of all axioms.
     * @param solutionAxioms
     *     - A list of all solution axioms.
     * @param heuristics
     *     - A list of all heuristics.
     * @param nDepth
     *     - The maximum depth of the search tree to use for searching for
     *     solutions. Larger values allow for more complicated solutions to be
     *     found, but use an expotentionally larger search space.
     * @throws IllegalArgumentException
     *     If the functions list does not contain at least one input function and
     *     exactly one output function.
     */
    Environment(List<IFunction> functions, List<IAxiom> axioms, List<IAxiom> solutionAxioms,
            List<IHeuristic> heuristics, int nDepth)
    {
        validateFunctions(functions);

        this.axioms = Collections.unmodifiableList(new ArrayList<>(axioms));
        this.solutionAxioms = Collections.unmodifiableList(new ArrayList<>(solutionAxioms));
        this.heuristics = Collections.unmodifiableList(new ArrayList<>(heuristics));
        this.functions = Collections.unmodifiableList(new ArrayList<>(functions));
        this.nDepth = nDepth;
    }

    private void validateFunctions(List<IFunction> functions)
    {
        int inputs = 0;
        int outputs = 0;

        for (IFunction function : functions)
        {
            if (function instanceof InputFunction)
                inputs++;

            if (function instanceof OutputFunction)
                outputs++;
        }

        if (inputs == 0)
            throw new IllegalArgumentException("Function list must contain at least one input function!");

        if (outputs != 1)
            throw new IllegalArgumentException(
                    "Function list must contain at exactly one output function! Actual is " + outputs);
    }

    /**
     * Gets the output function to use for the solution.
     * 
     * @return The output function.
     */
    public IFunction getOutputFunction()
    {
        for (IFunction function : functions)
            if (function instanceof OutputFunction)
                return function;

        throw new RuntimeException("Output function not present!");
    }

    /**
     * Gets the index of the requested function within this environment. This method
     * checks for functions which are equal to the given function, using the
     * {@link java.lang.Object#equals()} method.
     * 
     * @param function
     *     - The function to locate.
     * @return The index of the function, or -1 if the function is not in this
     *     environment.
     */
    public int getIndexOf(IFunction function)
    {
        for (int i = 0; i < functions.size(); i++)
            if (function.equals(functions.get(i)))
                return i;

        return -1;
    }

    /**
     * Calculates the minimum number of bytes required to effectively represent any
     * NodeGraph binary array within this environment. When creating the first node
     * graph object, this method is useful for determining a safe value for the
     * "numSize" parameter.
     * <p>
     * The values considered at the number of functions, the number of input or
     * output plugs on each function, and the nDepth.
     * 
     * @return 1, 2, or 4, representing a byte, short, and int respectively.
     */
    public int getMinByteCount()
    {
        int max = 0;

        max = Math.max(max, functions.size());
        max = Math.max(max, countPlugs());
        max = Math.max(max, nDepth);

        return countBytes(max);
    }

    /**
     * Gets the largest number of input or output plugs being used on any function
     * within this environment.
     * 
     * @return The largest plug count used.
     */
    private int countPlugs()
    {
        int plugs = 0;

        for (IFunction f : functions)
        {
            plugs = Math.max(plugs, f.getInputs().length);
            plugs = Math.max(plugs, f.getOutputs().length);
        }

        return plugs;
    }

    /**
     * Calculates then fewest number of bytes required to represent a single value.
     * 
     * @param value
     *     - The value.
     * @return The smallest number of bytes, being 1, 2, 3, or 4.
     */
    private int countBytes(int value)
    {
        if (value >= 1 << 24)
            return 4;

        if (value >= 1 << 16)
            return 3;

        if (value >= 1 << 8)
            return 2;

        return 1;
    }

    /**
     * Gets the maximum depth that a solution can be. The depth of a solution is
     * measured by the number of connections in that graph, where one new connection
     * is added per level. This value is used to prevent solutions from scaling to
     * infinity in complexity. This can also be used to prune solutions early if the
     * number of open plugs plus the number of connections exceeds this value.
     * 
     * @return The maximum depth of a solution.
     */
    public int getMaxDepth()
    {
        return nDepth;
    }

    /**
     * Gets an unmodifiable list of all axioms present in this environment.
     * 
     * @return A list of all axioms.
     */
    public List<IAxiom> getAxioms()
    {
        return axioms;
    }

    /**
     * Gets an unmodifiable list of all solution axioms present in this environment.
     * 
     * @return A list of all solution axioms.
     */
    public List<IAxiom> getSolutionAxioms()
    {
        return solutionAxioms;
    }

    /**
     * Gets an unmodifiable list of all heuristics present in this environment.
     * 
     * @return A list of all heuristics.
     */
    public List<IHeuristic> getHeuristics()
    {
        return heuristics;
    }

    /**
     * Gets an unmodifiable list of all functions present in this environment.
     * 
     * @return A list of all functions.
     */
    public List<IFunction> getFunctions()
    {
        return functions;
    }
}
