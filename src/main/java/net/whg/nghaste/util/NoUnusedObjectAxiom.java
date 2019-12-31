package net.whg.nghaste.util;

import net.whg.nghaste.Connection;
import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.NodeGraph;

/**
 * This is a solution axiom is used to ensure that no instances of a data type
 * go unused. By this, this axiom will mark all solutions invalid which contain
 * output plugs of the given data type which have no connection coming out of
 * them.
 */
public final class NoUnusedObjectAxiom implements IAxiom
{
    private final IDataType dataType;

    public NoUnusedObjectAxiom(IDataType dataType)
    {
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
            IDataType[] outputs = graph.getNodeAsFunction(nodeIndex)
                                       .getOutputs();
            for (int plugIndex = 0; plugIndex < outputs.length; plugIndex++)
            {
                if (!outputs[plugIndex].equals(dataType))
                    continue;

                int refs = 0;
                for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
                {
                    graph.getConnection(connectionIndex, connection);

                    if (connection.getOutputNode() == nodeIndex && connection.getOutputPlug() == plugIndex)
                        refs++;
                }

                if (refs == 0)
                    return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        NoUnusedObjectAxiom other = (NoUnusedObjectAxiom) obj;
        if (dataType == null)
        {
            if (other.dataType != null)
                return false;
        }
        else if (!dataType.equals(other.dataType))
            return false;

        return true;
    }
}
