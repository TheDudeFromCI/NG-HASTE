package net.whg.nghaste.codeflow;

import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.InputFunction;
import net.whg.nghaste.util.SingleConstAxiom;

/**
 * This function exists as the intended method for starting a thread. This
 * function is required when working with the codeflow package to successfully
 * generate a solution.
 * <p>
 * To prevent broken solutions, in which multiple threads are started, this
 * function adds required SingleConstAxiom to the environment.
 * 
 * @see net.whg.nghaste.util.SingleConstAxiom
 */
public final class BeginThreadFunction extends InputFunction
{
    public BeginThreadFunction()
    {
        super(new IDataType[] {new ThreadDataType()});
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        return obj instanceof BeginThreadFunction;
    }

    @Override
    public int hashCode()
    {
        return 32340981;
    }

    @Override
    public IAxiom[] getRequiredAxioms()
    {
        return new IAxiom[] {new SingleConstAxiom()};
    }

    @Override
    public IDataInstance[] execute(IDataInstance[] inputs)
    {
        return new IDataInstance[] {new ThreadDataInstance()};
    }
}
