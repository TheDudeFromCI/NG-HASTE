package net.whg.nghaste.unit;

import org.junit.Test;
import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.impl.OutputFunction;

public class OutputFunctionTest
{
    @Test(expected = NullPointerException.class)
    public void constructor_nullInputs()
    {
        new OutputFunction(null)
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
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
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }
}
