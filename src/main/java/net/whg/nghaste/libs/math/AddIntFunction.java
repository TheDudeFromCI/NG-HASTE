package net.whg.nghaste.libs.math;

import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.codeflow.ObjectHeirarchy;

public class AddIntFunction implements IFunction
{
    private final IDataType[] inputs;
    private final IDataType[] outputs;

    public AddIntFunction(ObjectHeirarchy heirarchy)
    {
        IDataType number = heirarchy.getObject("Integer");
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
        int value = (int) inputs[0].getValue() + (int) inputs[1].getValue();
        return new DataInstance[] {new DataInstance(outputs[0], value)};
    }

    @Override
    public String toString()
    {
        return "Add(Integer: a, Integer: b) => Integer: value";
    }
}
