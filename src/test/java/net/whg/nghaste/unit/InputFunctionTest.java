package net.whg.nghaste.unit;

import org.junit.Test;
import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.impl.InputFunction;

public class InputFunctionTest
{
    @Test(expected = NullPointerException.class)
    public void constructor_nullInputs()
    {
        new InputFunction(null)
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
        new InputFunction(new IDataType[0])
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }
}
