package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.codeflow.BeginThreadFunction;
import net.whg.nghaste.codeflow.ThreadDataInstance;
import net.whg.nghaste.codeflow.ThreadDataType;
import net.whg.nghaste.util.SingleConstAxiom;

public class BeginThreadFunctionTest
{
    @Test
    public void asInputFunction_getOutputType_IsThread()
    {
        BeginThreadFunction function = new BeginThreadFunction();

        assertEquals(0, function.getInputs().length);
        assertEquals(1, function.getOutputs().length);
        assertEquals(new ThreadDataType(), function.getOutputs()[0]);
    }

    @Test
    public void getRequiredAxioms()
    {
        BeginThreadFunction function = new BeginThreadFunction();

        assertEquals(1, function.getRequiredAxioms().length);
        assertEquals(SingleConstAxiom.class, function.getRequiredAxioms()[0].getClass());
    }

    @Test
    public void equals_sameInstance()
    {
        BeginThreadFunction function = new BeginThreadFunction();

        assertEquals(function, function);
        assertEquals(function.hashCode(), function.hashCode());
    }

    @Test
    public void equals_diffInstance()
    {
        BeginThreadFunction function1 = new BeginThreadFunction();
        BeginThreadFunction function2 = new BeginThreadFunction();

        assertEquals(function1, function2);
        assertEquals(function1.hashCode(), function2.hashCode());
    }

    @Test
    public void notEquals_diffObject()
    {
        BeginThreadFunction function = new BeginThreadFunction();
        assertNotEquals(function, new Object());
    }

    @Test
    public void notEquals_null()
    {
        BeginThreadFunction function = new BeginThreadFunction();
        assertNotEquals(function, null);
    }

    @Test
    public void execute()
    {
        BeginThreadFunction function = new BeginThreadFunction();

        IDataInstance[] in = new IDataInstance[0];
        IDataInstance[] out = function.execute(in);

        assertEquals(1, out.length);
        assertEquals(ThreadDataInstance.class, out[0].getClass());
    }
}
