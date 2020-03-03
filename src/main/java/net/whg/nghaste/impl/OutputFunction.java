package net.whg.nghaste.impl;

import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;

/**
 * An output function is a special type of function which takes only inputs and
 * provides returns the result of the entire output algorithm. There is only one
 * output function in a solution. This function exists as a method of returning
 * the results of the algorithm call.
 */
public abstract class OutputFunction implements IFunction
{
    private static final IDataType[] OUTPUTS = new IDataType[0];

    private final IDataType[] inputs;

    /**
     * Creates a new output function object.
     * 
     * @param inputs
     *     - The input types used for this input function. Must contain at least one
     *     element.
     * @throws IllegalArgumentException
     *     If the length of "inputs" is 0.
     */
    public OutputFunction(IDataType[] inputs)
    {
        if (inputs.length == 0)
            throw new IllegalArgumentException("Input length cannot be 0!");

        this.inputs = inputs;
    }

    @Override
    public final IDataType[] getInputs()
    {
        return inputs;
    }

    @Override
    public final IDataType[] getOutputs()
    {
        return OUTPUTS;
    }
}
