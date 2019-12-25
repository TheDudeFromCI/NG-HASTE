package net.whg.nghaste;

import java.util.List;

/**
 * The environment is the container for storing all information about what the
 * algorithm is trying to solve and what it's allowed to do to solve the
 * problem. This object is immuatble and is used as a reference for worker
 * threads to mine the search tree. This class is immutable after creation.
 */
public final class Environment
{
    private final IFunction[] functions;
    private final IFunction outputFunction;
    private final int nDepth;

    /**
     * o Creates a new environment object.
     * 
     * @param functions
     *     - All of the functions within this environment.
     * @param outputFunction
     *     - The output function for the solution. This function should be inside of
     *     the function list. If not, it is added to the functions list internally.
     * @param nDepth
     *     - The maximum depth of the search tree to use for searching for
     *     solutions. Larger values allow for more complicated solutions to be
     *     found, but use an expotentionally larger search space.
     * @throws IllegalArgumentException
     *     If "nDepth" is <= 0.
     */
    public Environment(List<IFunction> functions, IFunction outputFunction, int nDepth)
    {
        if (nDepth <= 0)
            throw new IllegalArgumentException("nDepth value must be > 0!");

        if (functions.contains(outputFunction))
            this.functions = functions.toArray(new IFunction[functions.size()]);
        else
        {
            this.functions = functions.toArray(new IFunction[functions.size() + 1]);
            this.functions[this.functions.length - 1] = outputFunction;
        }

        this.outputFunction = outputFunction;
        this.nDepth = nDepth;
    }

    /**
     * Gets the output function to use for the solution.
     * 
     * @return The output function.
     */
    public IFunction getOutputFunction()
    {
        return outputFunction;
    }

    /**
     * Gets the number of functions in this environment.
     * 
     * @return The number of functions.
     */
    public int getFunctionCount()
    {
        return functions.length;
    }

    /**
     * Gets the function at the specified index of this environment.
     * 
     * @param index
     *     - The index of the function.
     * @return The function.
     */
    public IFunction getFunctionAt(int index)
    {
        return functions[index];
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
        for (int i = 0; i < functions.length; i++)
            if (function.equals(functions[i]))
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

        max = Math.max(max, functions.length);
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
     * @return The smallest number of bytes, being 1, 2, or 4, representing a byte,
     *     a short, or short respectively.
     */
    private int countBytes(int value)
    {
        if (value >= 1 << 16)
            return 4;

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
}
