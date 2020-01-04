package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import net.whg.nghaste.codeflow.ThreadDataInstance;
import net.whg.nghaste.codeflow.ThreadDataType;
import net.whg.nghaste.util.NoUnusedObjectAxiom;
import net.whg.nghaste.util.SingleInstanceAxiom;

public class ThreadDataTypeTest
{
    @Test
    public void requiredAxioms()
    {
        ThreadDataType thread = new ThreadDataType();

        assertEquals(1, thread.getRequiredAxioms().length);
        assertEquals(SingleInstanceAxiom.class, thread.getRequiredAxioms()[0].getClass());

        assertEquals(1, thread.getRequiredSolutionAxioms().length);
        assertEquals(NoUnusedObjectAxiom.class, thread.getRequiredSolutionAxioms()[0].getClass());
    }

    @Test
    public void equals_sameInstance()
    {
        ThreadDataType thread = new ThreadDataType();

        assertEquals(thread, thread);
        assertEquals(thread.hashCode(), thread.hashCode());
    }

    @Test
    public void equals_diffInstance()
    {
        ThreadDataType thread1 = new ThreadDataType();
        ThreadDataType thread2 = new ThreadDataType();

        assertEquals(thread1, thread2);
        assertEquals(thread1.hashCode(), thread2.hashCode());
    }

    @Test
    public void notEquals_diffObject()
    {
        ThreadDataType thread = new ThreadDataType();

        assertNotEquals(thread, new Object());
    }

    @Test
    public void notEquals_null()
    {
        ThreadDataType thread = new ThreadDataType();

        assertNotEquals(thread, null);
    }

    @Test
    public void dataInstance()
    {
        ThreadDataInstance instance = new ThreadDataInstance();
        assertEquals(new ThreadDataType(), instance.getType());
    }
}
