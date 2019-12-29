package net.whg.nghaste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a simple utility class for reindexing a node graph. By this, a
 * map is populated with a set of corresponding node indicesx, matching their
 * old index to their new index. When a graph is reindexed, then any two graphs
 * which represent the same node structure, regardless of the original order of
 * nodes or connections, will have the same new node indices. This action is
 * useful in cases such as duplicate matching. Node graphs that are similar, but
 * not equal in structure, are not promised to have similar output indices. This
 * class is not thread safe.
 */
public final class NodeReindexer
{
    private NodeReindexer()
    {}

    /**
     * Analyzes a graph and populates a map for now each node should be reindexed.
     * 
     * @param graph
     *     - The graph to analyize.
     * @param out
     *     - The map to write the new indices to. The key is the old node index
     *     while the value is the new node index.
     */
    public static void reindex(NodeGraph graph, Map<Integer, Integer> out)
    {
        placeNodes(out, sortConnections(graph), 0, 0);
    }

    /**
     * Looks at nodes recursively to write their indices to the new map, based on
     * the order of conections to input plugs. This function operates by looking at
     * the sorted connection list and building up connections in reverse order.
     * 
     * @param indices
     *     - The map to write the output to.
     * @param nodeIndex
     *     - The index of the current node.
     * @param outIndex
     *     - The new index to write to the current node, if not already updated.
     * @return The new index of the next node to be updated.
     */
    private static int placeNodes(Map<Integer, Integer> indices, List<Connection> connections, int nodeIndex,
            int outIndex)
    {
        if (indices.containsKey(nodeIndex))
            return outIndex;

        indices.put(nodeIndex, outIndex++);

        for (Connection connection : connections)
        {
            if (connection.getInputNode() != nodeIndex)
                continue;

            outIndex = placeNodes(indices, connections, connection.getOutputNode(), outIndex);
        }

        return outIndex;
    }

    /**
     * Builds a list of all connections for the graph and sorts the connection list
     * based on the order of the input plug. This method makes use of a second
     * buffer list to pool connection objects to avoid unnessicary memory
     * allocations.
     * 
     * @param graph
     *     - The graph to build the connection list for.
     */
    private static List<Connection> sortConnections(NodeGraph graph)
    {
        List<Connection> connections = new ArrayList<>();

        int connectionCount = graph.getConnectionCount();
        for (int i = 0; i < connectionCount; i++)
        {
            Connection connection = new Connection();
            graph.getConnection(i, connection);
            connections.add(connection);
        }

        connections.sort((a, b) -> a.getInputPlug() - b.getInputPlug());

        return connections;
    }
}
