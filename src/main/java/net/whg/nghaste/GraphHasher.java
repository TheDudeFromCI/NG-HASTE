package net.whg.nghaste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The graph hasher is an object which is used to break a graph down into a
 * compressed byte array format, for the purpose of comparing the structures of
 * different graphs. Two graphs of the same structure, regardless of the order
 * connections or nodes were added, will always have the same output hash.
 * Hashes are designed to have a smaller size to take up a smaller space in
 * memory.
 */
public class GraphHasher
{
    private final Map<Integer, Integer> newIndexBuffer = new HashMap<>();
    private final List<Connection> connectionBuffer = new ArrayList<>();

    /**
     * Hashes a node graph by creating a compressed byte array which represents the
     * structure of the graph. This is used for comparing the graph structure
     * without considering the order nodes and connections were added in.
     * 
     * @param graph
     *     - The graph to create the hash for.
     * @return A byte array representing the hash of the graph.
     */
    public GraphHash createHash(NodeGraph graph)
    {
        NodeReindexer.reindex(graph, newIndexBuffer);
        prepareConnectionBuffer(graph);

        byte[] hash = hashConnectionList();

        connectionBuffer.clear();
        newIndexBuffer.clear();

        return new GraphHash(hash);
    }

    /**
     * Populates the connection buffer list with all of the connections for the
     * graph, and swapping out the indices of the nodes with the new indices as
     * determined by the node reindexer. Then sorts the newly populated list for
     * use.
     */
    private void prepareConnectionBuffer(NodeGraph graph)
    {
        int connectionCount = graph.getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            Connection connection = new Connection();
            graph.getConnection(connectionIndex, connection);
            connection.set(newIndexBuffer.get(connection.getOutputNode()), connection.getOutputPlug(),
                    newIndexBuffer.get(connection.getInputNode()), connection.getInputPlug(),
                    connection.getOutputNodeType(), connection.getInputNodeType());

            connectionBuffer.add(connection);
        }
        connectionBuffer.sort((a, b) -> a.hashCode() - b.hashCode());
    }

    /**
     * Generates a byte array hash for a list of connections currently in the
     * connection buffer.
     * 
     * @return The byte array hash.
     */
    private byte[] hashConnectionList()
    {
        int numSize = countBytes(getHashSize());

        int j = 1;
        byte[] hash = new byte[connectionBuffer.size() * 6 * numSize + 1];
        hash[0] = (byte) numSize;

        for (Connection connection : connectionBuffer)
        {
            j = writeNumber(hash, numSize, j, connection.getOutputNode());
            j = writeNumber(hash, numSize, j, connection.getInputPlug());
            j = writeNumber(hash, numSize, j, connection.getOutputNode());
            j = writeNumber(hash, numSize, j, connection.getInputPlug());
            j = writeNumber(hash, numSize, j, connection.getOutputNodeType());
            j = writeNumber(hash, numSize, j, connection.getInputNodeType());
        }

        return hash;
    }

    private int getHashSize()
    {
        int max = 0;

        for (Connection c : connectionBuffer)
        {
            max = Math.max(max, c.getOutputNode());
            max = Math.max(max, c.getInputPlug());
            max = Math.max(max, c.getOutputNode());
            max = Math.max(max, c.getInputPlug());
            max = Math.max(max, c.getOutputNodeType());
            max = Math.max(max, c.getInputNodeType());
        }

        return max;
    }

    /**
     * A small utility function for writing a number to a byte array.
     * 
     * @param bytes
     *     - The byte array to write to.
     * @param numSize
     *     - The number of bytes to use per value.
     * @param pos
     *     - The byte position to write to.
     * @param value
     *     - The value to write.
     * @return The new index, for future writes. Equal to
     *     <code>index + numSize</code>
     */
    private int writeNumber(byte[] bytes, int numSize, int index, int value)
    {
        for (int i = numSize - 1; i >= 0; i--)
            bytes[index++] = (byte) ((value >> (i * 8)) & 0xFF);

        return index;
    }

    /**
     * Calculates then fewest number of bytes required to represent a single value.
     * 
     * @param value
     *     - The value.
     * @return The smallest number of bytes, being 1, 2, 3, or 4.
     */
    private int countBytes(int value)
    {
        if (value >= 1 << 24)
            return 4;

        if (value >= 1 << 16)
            return 3;

        if (value >= 1 << 8)
            return 2;

        return 1;
    }
}
