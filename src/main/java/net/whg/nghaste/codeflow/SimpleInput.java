package net.whg.nghaste.codeflow;

import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.impl.InputFunction;

public class SimpleInput extends InputFunction
{
    private final DataInstance[] dataInstance;

    public SimpleInput(IDataType dataType, Object value)
    {
        super(new IDataType[] {dataType});
        dataInstance = new DataInstance[] {new DataInstance(dataType, value)};
    }

    @Override
    public DataInstance[] execute(DataInstance[] inputs)
    {
        return dataInstance;
    }

    @Override
    public String toString()
    {
        return String.format("SimpleInput() => %s: %s", dataInstance[0].getType(), dataInstance[0].getValue());
    }
}
