package net.whg.nghaste.codeflow;

import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.impl.OutputFunction;

public class SimpleOutput extends OutputFunction
{
    public SimpleOutput(IDataType dataType)
    {
        super(new IDataType[] {dataType});
    }

    @Override
    public DataInstance[] execute(DataInstance[] inputs)
    {
        return new DataInstance[0];
    }

    @Override
    public String toString()
    {
        return String.format("SimpleOutput(%s: value) => Void", this.getInputs()[0]);
    }
}
