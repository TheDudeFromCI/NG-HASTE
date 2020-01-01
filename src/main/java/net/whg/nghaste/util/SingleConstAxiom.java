package net.whg.nghaste.util;

import net.whg.nghaste.IAxiom;
import net.whg.nghaste.InputFunction;
import net.whg.nghaste.NodeGraph;

/**
 * This axiom is used to ensure that only at most a single instance of each
 * input function is present in a graph. Using duplicate constants only takes up
 * extra node slots in the graph, and increases the search space without
 * effecting the output of the graph in any way.
 */
public final class SingleConstAxiom implements IAxiom
{
    @Override
    public boolean isValid(NodeGraph graph)
    {
        int nodeCount = graph.getNodeCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            if (!(graph.getNodeAsFunction(nodeIndex) instanceof InputFunction))
                continue;

            int type = graph.getNodeType(nodeIndex);
            for (int secondIndex = nodeIndex + 1; secondIndex < nodeCount; secondIndex++)
            {
                if (graph.getNodeType(secondIndex) == type)
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        return obj instanceof SingleConstAxiom;
    }

    @Override
    public int hashCode()
    {
        return 3124561;
    }
}
