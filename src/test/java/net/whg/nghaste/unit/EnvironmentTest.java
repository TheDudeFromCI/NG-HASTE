package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.impl.Environment;
import net.whg.nghaste.impl.EnvironmentBuilder;
import net.whg.nghaste.impl.InputFunction;
import net.whg.nghaste.impl.OutputFunction;

public class EnvironmentTest
{
    private IDataType dataType()
    {
        return new IDataType()
        {
            @Override
            public boolean canConnectTo(IDataType dataType)
            {
                return dataType == this;
            }
        };
    }

    private IFunction outputFunction()
    {
        IDataType dataType = dataType();
        return new OutputFunction(new IDataType[] {dataType})
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }

    private IFunction inputFunction()
    {
        IDataType dataType = dataType();
        return new InputFunction(new IDataType[] {dataType})
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }

    private IFunction function()
    {
        IDataType dataType = dataType();
        return new IFunction()
        {
            @Override
            public IDataType[] getInputs()
            {
                return new IDataType[] {dataType};
            }

            @Override
            public IDataType[] getOutputs()
            {
                return new IDataType[] {dataType};
            }

            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }

    @Test
    public void getOutputFunction()
    {
        IFunction func = function();
        IFunction out = outputFunction();
        IFunction in = inputFunction();

        Environment env = new EnvironmentBuilder().addFunction(func)
                                                  .addFunction(out)
                                                  .addFunction(in)
                                                  .build();

        assertEquals(out, env.getOutputFunction());
    }

    @Test
    public void getFunction_indexOf_Input()
    {
        IFunction func = function();
        IFunction out = outputFunction();
        IFunction in = inputFunction();

        Environment env = new EnvironmentBuilder().addFunction(func)
                                                  .addFunction(out)
                                                  .addFunction(in)
                                                  .build();

        assertEquals(2, env.getIndexOf(in));
    }

    @Test
    public void getFunction_indexOf_notPresent()
    {
        IFunction func = function();
        IFunction out = outputFunction();
        IFunction in = inputFunction();
        IFunction extra = function();

        Environment env = new EnvironmentBuilder().addFunction(func)
                                                  .addFunction(out)
                                                  .addFunction(in)
                                                  .build();

        assertEquals(-1, env.getIndexOf(extra));
    }
}
