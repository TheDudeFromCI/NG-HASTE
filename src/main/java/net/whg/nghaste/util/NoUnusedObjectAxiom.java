package net.whg.nghaste.util;

import net.whg.nghaste.IDataType;
import net.whg.nghaste.ISolutionAxiom;
import net.whg.nghaste.impl.NodeGraph;

/**
 * This is a solution axiom is used to ensure that no instances of a data type
 * go unused. By this, this axiom will mark all solutions invalid which contain
 * output plugs of the given data type which have no connection coming out of
 * them.
 */
public final class NoUnusedObjectAxiom implements ISolutionAxiom
{
    private final IDataType dataType;

    /**
     * Creates a new NoUnusedObject axiom. This solution axiom will ensure that all
     * created instances of the given data type are used. That being, all output
     * plugs that use the provided data type must have at least one connection
     * coming from them.
     * 
     * @param dataType
     *     - The type of data to validate.
     * @throws IllegalArgumentException
     *     If dataType is null.
     */
    public NoUnusedObjectAxiom(IDataType dataType)
    {
        if (dataType == null)
            throw new IllegalArgumentException("Datatype cannot be null!");

        this.dataType = dataType;
    }

    @Override
    public boolean isValid(NodeGraph graph)
    {
        int nodeCount = graph.getNodeCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            IDataType[] outputs = graph.getNodeAsFunction(nodeIndex)
                                       .getOutputs();
            for (int plugIndex = 0; plugIndex < outputs.length; plugIndex++)
            {
                if (!outputs[plugIndex].equals(dataType))
                    continue;

                if (graph.getOutputConnectionCount(nodeIndex, plugIndex) == 0)
                    return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return dataType.hashCode() + 123450912;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof NoUnusedObjectAxiom))
            return false;

        NoUnusedObjectAxiom other = (NoUnusedObjectAxiom) obj;
        return dataType.equals(other.dataType);
    }
}
