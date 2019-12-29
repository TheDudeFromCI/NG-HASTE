package net.whg.nghaste;

import java.util.HashMap;
import java.util.Map;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;

/**
 * The duplicate finder class is a utility class designed to work alongside the
 * search tree class to find and remove duplicate graphs which are revealed as
 * the tree searchs for new graphs. These duplicates become increasingly common
 * as the search space increases, and removing duplicates leads to a huge
 * decrease in the number of areas needlessly being searched. As this object
 * stores a state of what graphs have already been visited, one single instance
 * may exist per algorithm instance. This class is thread safe.
 */
public class DuplicateFinder
{
    // private BlockingMap<GraphHash, Integer> duplicates = new BlockingHashMap<>();
    private Map<GraphHash, Integer> duplicates = new HashMap<>();

    /**
     * This method is used to check if a matching graph has already been searched or
     * not. This method works by using a hashing object to compress a graph down
     * into a smaller form in which graphs that match, regardless or node indices,
     * will have the same resulting hash. Then an internal counter is used to
     * determine the number of remaining duplicates that exist for a graph of the
     * structure. This method will keep an internal counter for the number of
     * remaining duplicates, which decrements by one each time a new duplicate is
     * discovered and checked. After all duplicates for a graph have been checked,
     * this object will clean up any allocated memory.
     * 
     * @param hasher
     *     - The graph hashing object to use when hashing the graph.
     * @param graph
     *     - The graph to check.
     * @return True the given graph has not yet been searched before. False
     *     otherwise.
     */
    public boolean isUnquie(GraphHasher hasher, NodeGraph graph)
    {
        GraphHash hash = hasher.createHash(graph);
        int remaining = countRemaining(graph);

        synchronized (duplicates)
        {
            if (duplicates.containsKey(hash))
            {
                int val = duplicates.get(hash) - 1;

                if (val == 0)
                    duplicates.remove(hash);
                else
                    duplicates.put(hash, val);

                return false;
            }

            if (remaining > 0)
                duplicates.put(hash, remaining);
        }

        return true;
    }

    /**
     * Counts the number of remaining duplicate graphs which can exist, not counting
     * the given graph. This method works based on the way search trees operate,
     * only adding connections one step at a time, working backwards.
     * 
     * @param graph
     *     - The graph to check.
     * @return The number of other duplicate graphs, not counting the given graph.
     */
    private int countRemaining(NodeGraph graph)
    {
        int nodeCount = graph.getNodeCount();

        int p = 0;
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            int out = countOutputs(graph, nodeIndex);
            if (out > 1 || isLeafNode(graph, nodeIndex))
                p += out;
        }

        return p - 1;
    }

    /**
     * Counts the number of output connections coming from the given node.
     * 
     * @param graph
     *     - The graph the node is in.
     * @param nodeIndex
     *     - The index of the node.
     * @return The number of output connections.
     */
    private int countOutputs(NodeGraph graph, int nodeIndex)
    {
        int outputs = 0;

        Connection connection = new Connection();
        int connectionCount = graph.getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            graph.getConnection(connectionIndex, connection);

            if (connection.getOutputNode() == nodeIndex)
                outputs++;
        }

        return outputs;
    }

    /**
     * Checks if the given node is a leaf node or not. A leaf node is defined as a
     * node which currently has no incoming connections.
     * 
     * @param graph
     *     - The graph the node is in.
     * @param nodeIndex
     *     - The index of the node.
     * @return True if there are no connections leading to the node. False
     *     otherwise.
     */
    private boolean isLeafNode(NodeGraph graph, int nodeIndex)
    {
        Connection connection = new Connection();
        int connectionCount = graph.getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            graph.getConnection(connectionIndex, connection);
            if (connection.getInputNode() == nodeIndex)
                return false;
        }

        return true;
    }

    /**
     * Gets the number of unquie graph structures in this object which have
     * remaining duplicates that have not yet been discovered. When a new graph
     * strucutre is scanned, and {@link #isUnquie(GraphHasher, NodeGraph)} returns
     * true, an internal counter is stored which stores the number of graphs that
     * would match that structure. After all of those graphs have been scanned, the
     * value is removed from memory. This method checks how many structures are
     * awaiting further scans.
     * 
     * @return The number of graph structures which have remaining duplicates in the
     *     search tree.
     */
    public int getRemainingDuplicateTypes()
    {
        synchronized (duplicates)
        {
            return duplicates.size();
        }
    }
}
