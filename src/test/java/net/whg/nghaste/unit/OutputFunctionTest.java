package net.whg.nghaste.unit;

import org.junit.Test;
import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.OutputFunction;

public class OutputFunctionTest
{
    @Test(expected = NullPointerException.class)
    public void constructor_nullInputs()
    {
        new OutputFunction(null)
        {
            @Override
            public IDataInstance[] execute(IDataInstance[] inputs)
            {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_0lengthInputs()
    {
        new OutputFunction(new IDataType[0])
        {
            @Override
            public IDataInstance[] execute(IDataInstance[] inputs)
            {
                return null;
            }
        };
    }
}
