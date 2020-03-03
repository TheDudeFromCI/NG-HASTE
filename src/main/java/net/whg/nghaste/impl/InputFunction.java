package net.whg.nghaste.impl;

import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;

/**
 * An input function is a special type of function which takes no inputs and
 * only provides data to other parts of the function. This function exists as a
 * method of providing input parameters to the provided function for the rest of
 * the solution.
 */
public abstract class InputFunction implements IFunction
{
    private static final IDataType[] INPUTS = new IDataType[0];

    private final IDataType[] outputs;

    /**
     * Creates a new input function object.
     * 
     * @param outputs
     *     - The output types used for this input function. Must contain at least
     *     one element.
     * @throws IllegalArgumentException
     *     If the length of "outputs" is 0.
     */
    public InputFunction(IDataType[] outputs)
    {
        if (outputs.length == 0)
            throw new IllegalArgumentException("Output length cannot be 0!");

        this.outputs = outputs;
    }

    @Override
    public final IDataType[] getInputs()
    {
        return INPUTS;
    }

    @Override
    public final IDataType[] getOutputs()
    {
        return outputs;
    }
}
