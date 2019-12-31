package net.whg.nghaste.util;

import net.whg.nghaste.Connection;
import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.NodeGraph;

/**
 * This axiom is wrapped around a specific data type to ensure that an output
 * plug connection may only connect to a single input plug. This axiom is
 * intended for use with data types which are intended to be reference types
 * rather than value types.
 */
public final class SingleInstanceAxiom implements IAxiom
{
    private final IDataType dataType;

    /**
     * Creates a new SingleInstance axiom. This axiom will ensure that, at most, one
     * connection of the given data type may be pulled out of any output plug.
     * Output plugs of the given type may have 0 or 1 connections.
     * 
     * @param dataType
     *     - The type of data to validate.
     * @throws IllegalArgumentException
     *     If dataType is null.
     */
    public SingleInstanceAxiom(IDataType dataType)
    {
        if (dataType == null)
            throw new IllegalArgumentException("Datatype cannot be null!");

        this.dataType = dataType;
    }

    @Override
    public boolean isValid(NodeGraph graph)
    {
        Connection connection = new Connection();

        int nodeCount = graph.getNodeCount();
        int connectionCount = graph.getConnectionCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            IDataType[] outputPlugs = graph.getNodeAsFunction(nodeIndex)
                                           .getOutputs();
            for (int plugIndex = 0; plugIndex < outputPlugs.length; plugIndex++)
            {
                if (!outputPlugs[plugIndex].equals(dataType))
                    continue;

                int cons = 0;
                for (int c = 0; c < connectionCount; c++)
                {
                    graph.getConnection(c, connection);

                    if (connection.getOutputNode() == nodeIndex && connection.getOutputPlug() == plugIndex)
                    {
                        cons++;

                        if (cons > 1)
                            return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return dataType.hashCode() + 2349082;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof SingleInstanceAxiom))
            return false;

        SingleInstanceAxiom other = (SingleInstanceAxiom) obj;
        return dataType.equals(other.dataType);
    }
}
