package net.whg.nghaste.codeflow;

import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.IDataType;

public class ThreadDataInstance implements IDataInstance
{
    private final ThreadDataType dataType;

    public ThreadDataInstance()
    {
        this.dataType = new ThreadDataType();
    }

    @Override
    public IDataType getType()
    {
        return dataType;
    }
}
