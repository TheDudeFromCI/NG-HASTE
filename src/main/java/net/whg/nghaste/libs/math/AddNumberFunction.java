package net.whg.nghaste.libs.math;

import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.codeflow.ObjectHeirarchy;

public class AddNumberFunction implements IFunction
{
    private final IDataType[] inputs;
    private final IDataType[] outputs;

    public AddNumberFunction(ObjectHeirarchy heirarchy)
    {
        IDataType number = heirarchy.getObject("Number");
        inputs = new IDataType[] {number, number};
        outputs = new IDataType[] {number};
    }

    @Override
    public IDataType[] getInputs()
    {
        return inputs;
    }

    @Override
    public IDataType[] getOutputs()
    {
        return outputs;
    }

    @Override
    public DataInstance[] execute(DataInstance[] inputs)
    {
        float value = (float) inputs[0].getValue() + (float) inputs[1].getValue();
        return new DataInstance[] {new DataInstance(outputs[0], value)};
    }

    @Override
    public String toString()
    {
        return "Add(Number: a, Number: b) => Number: value";
    }
}
