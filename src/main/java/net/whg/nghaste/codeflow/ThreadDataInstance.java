package net.whg.nghaste.codeflow;

import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.IDataType;

public class ThreadDataInstance implements IDataInstance
{
    private final ThreadDataType dataType;

    public ThreadDataInstance(ThreadDataType dataType)
    {
        this.dataType = dataType;
    }

    @Override
    public IDataType getType()
    {
        return dataType;
    }
}
