package net.whg.nghaste.codeflow;

import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.ISolutionAxiom;
import net.whg.nghaste.util.NoUnusedObjectAxiom;
import net.whg.nghaste.util.SingleInstanceAxiom;

/**
 * The thread data type is a special data type which is used to direct the flow
 * of how code is moved through a function. This is used to ensure functions are
 * executed in the correct order and that the solution respects branching
 * factors such as if-else-statements and loops.
 * <p>
 * When using a codeflow based solution, it is intended to have one instance of
 * this datatype present in the output node, to ensure the code is executed
 * smoothly. Using zero instances of this data type will prevent any codeflow
 * functions from being generated. Using multiple thread outputs may lead to
 * unexpected results.
 * <p>
 * This data type adds the required axioms of SingleInstanceAxiom of this data
 * type.
 * 
 * @see net.whg.nghaste.util.SingleInstanceAxiom
 */
public final class ThreadDataType implements IDataType
{
    @Override
    public IAxiom[] getRequiredAxioms()
    {
        return new IAxiom[] {new SingleInstanceAxiom(this)};
    }

    @Override
    public ISolutionAxiom[] getRequiredSolutionAxioms()
    {
        return new ISolutionAxiom[] {new NoUnusedObjectAxiom(this)};
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        return obj instanceof ThreadDataType;
    }

    @Override
    public int hashCode()
    {
        return 398676511;
    }
}
