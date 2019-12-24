package net.whg.nghaste;

/**
 * A NodeGraph is a collection of nodes, each containing a set of inputs and
 * outputs, where multiple nodes are connected together, output-to-input, to
 * form a logical function. Nodes are able to represent any function which
 * processes data, and then passes that data to child functions to further
 * process it, propagating down the chain.<br>
 * <br>
 * This class represents a very simple and lightweight implementation of that
 * function. It's designed for the sole purpose of being as fast and memory
 * effcient as possible. It is backed by a single bye array which stores all of
 * the informaiton about the graph in a semi-compressed format. Whenever
 * information is read from the graph, it is pulled from a specific spot on the
 * byte array to quickly retreive information. This class is semi-immuatble, and
 * all edits preformed on this object simply return a new object instead. The
 * only exception to this rule is the heurisitic value which may be assigned at
 * any time.
 */
public class NodeGraph implements Comparable<NodeGraph>
{
    /**
     * A small utility function for writing a number to a byte array.
     * 
     * @param bytes
     *     - The byte array.
     * @param numSize
     *     - The number of bytes in each number.
     * @param pos
     *     - The byte position to write to.
     * @param value
     *     - The value to write.
     */
    private static void writeNumber(byte[] bytes, int numSize, int pos, int value)
    {
        for (int i = numSize - 1; i >= 0; i--)
            bytes[pos++] = (byte) ((value >> (i * 8)) & 0xFF);
    }

    /**
     * A small utility function for reading a number from a byte array.
     * 
     * @param bytes
     *     - The byte array.
     * @param numSize
     *     - The number of bytes in each number.
     * @param pos
     *     - The byte position to read from.
     * @return The value at the given position.
     */
    private static int readNumber(byte[] bytes, int numSize, int pos)
    {
        int value = 0;

        for (int i = numSize - 1; i >= 0; i--)
            value |= (bytes[pos++] & 0xFF) << i * 8;

        return value;
    }

    /**
     * Creates a new node graph containing a single node, the output node.
     * 
     * @param numSize
     *     - The number of bytes to use when reading or writing to the byte array.
     *     Maybe be either 1, 2, or 4 bytes per value.
     * @param nodeType
     *     - The type of node to use as the output node.
     * @return The newly created node graph.
     * @throws IllegalArgumentException
     *     If the numSize is not a value of 1, 2, or 4.
     */
    public static NodeGraph newGraph(int numSize, int nodeType)
    {
        if (numSize != 1 && numSize != 2 && numSize != 4)
            throw new IllegalArgumentException("Unsupported byte size: " + numSize);

        NodeGraph graph = new NodeGraph(numSize, 2 * numSize);

        writeNumber(graph.data, numSize, 0, 1);
        writeNumber(graph.data, numSize, numSize, nodeType);

        return graph;
    }

    private final int numSize;
    private final byte[] data;
    private float heuristic;

    /**
     * Creates a new, empty node graph.
     * 
     * @param numSize
     *     - The number of bytes per value.
     * @param buffer
     *     - The size of the byte array.
     */
    private NodeGraph(int numSize, int buffer)
    {
        this.numSize = numSize;
        data = new byte[buffer];
    }

    /**
     * Adds a new connection to this node graph.
     * 
     * @param outputNode
     *     - The output node for the connection.
     * @param outputPlug
     *     - The plug of the output node for the connection.
     * @param inputNode
     *     - The input plug for the connection.
     * @param inputPlug
     *     - The plug of the input node for the connection.
     * @return A newly created node graph which represents a copy of this node graph
     *     with the modification applied.
     * @throws IndexOutOfBoundsException
     *     if the input node or output node point to an element outside of the range
     *     of nodes in this graph.
     */
    public NodeGraph addConnection(int outputNode, int outputPlug, int inputNode, int inputPlug)
    {
        if (outputNode < 0 || outputNode >= getNodeCount())
            throw new IndexOutOfBoundsException("Output node out of bounds: " + outputNode);

        if (inputNode < 0 || inputNode >= getNodeCount())
            throw new IndexOutOfBoundsException("Input node out of bounds: " + inputNode);

        NodeGraph graph = new NodeGraph(numSize, data.length + numSize * 4);

        for (int i = 0; i < data.length; i++)
            graph.data[i] = data[i];

        writeNumber(graph.data, numSize, data.length, outputNode);
        writeNumber(graph.data, numSize, data.length + numSize, outputPlug);
        writeNumber(graph.data, numSize, data.length + numSize * 2, inputNode);
        writeNumber(graph.data, numSize, data.length + numSize * 3, inputPlug);

        return graph;
    }

    /**
     * This function creates a connection to a new node, where the new node is the
     * output node.
     * 
     * @param nodeType
     *     - The type of the new node to add.
     * @param outputPlug
     *     - The plug of the new node to take the connection from.
     * @param inputNode
     *     - The node the connection is moving to.
     * @param inputPlug
     *     - The plug of the input node for the connection.
     * @return A newly created node graph which represents a copy of this node graph
     *     with the modification applied.
     * @throws IndexOutOfBoundsException
     *     if the input node or output node point to an element outside of the range
     *     of nodes in this graph.
     */
    public NodeGraph addConnectionAndNode(int nodeType, int outputPlug, int inputNode, int inputPlug)
    {
        if (inputNode < 0 || inputNode >= getNodeCount())
            throw new IndexOutOfBoundsException("Input node out of bounds: " + inputNode);

        NodeGraph graph = new NodeGraph(numSize, data.length + 5 * numSize);

        int nodeCount = getNodeCount();
        int connCount = getConnectionCount();
        int j = 0;

        writeNumber(graph.data, numSize, j++ * numSize, nodeCount + 1);

        for (int i = 0; i < nodeCount; i++)
            writeNumber(graph.data, numSize, j++ * numSize, getNodeType(i));
        writeNumber(graph.data, numSize, j++ * numSize, nodeType);

        for (int i = 0; i < connCount * 4; i++)
            writeNumber(graph.data, numSize, j++ * numSize, readNumber(data, numSize, (nodeCount + 1 + i) * numSize));

        writeNumber(graph.data, numSize, j++ * numSize, nodeCount);
        writeNumber(graph.data, numSize, j++ * numSize, outputPlug);
        writeNumber(graph.data, numSize, j++ * numSize, inputNode);
        writeNumber(graph.data, numSize, j++ * numSize, inputPlug);

        return graph;
    }

    /**
     * Gets the number of bytes being used per value to represent this node graph.
     * 
     * @return The size of a single value in this object.
     */
    public int getNumberSize()
    {
        return numSize;
    }

    /**
     * Gets the number of nodes in this graph.
     * 
     * @return The node count.
     */
    public int getNodeCount()
    {
        return readNumber(data, numSize, 0);
    }

    /**
     * Gets the type of node at the given node index.
     * 
     * @param node
     *     - The index of the node.
     * @return The type of the node.
     * @throws ArrayIndexOutOfBoundsException
     *     If the index of the node is outside of the range of available nodes.
     */
    public int getNodeType(int node)
    {
        if (node < 0 || node >= getNodeCount())
            throw new ArrayIndexOutOfBoundsException(node);

        return readNumber(data, numSize, (node + 1) * numSize);
    }

    /**
     * Gets the number of connections in this graph.
     * 
     * @return The connection count.
     */
    public int getConnectionCount()
    {
        return (data.length / numSize - 1 - getNodeCount()) / 4;
    }

    /**
     * Gets the data for a specific connection within this graph.
     * 
     * @param node
     *     - The index of the connection.
     * @param out
     *     - The connection to write the data to.
     * @return The type of the node.
     * @throws ArrayIndexOutOfBoundsException
     *     If the index of the connection is outside of the range of available
     *     connections.
     */
    public void getConnection(int index, Connection out)
    {
        if (index < 0 || index >= getConnectionCount())
            throw new ArrayIndexOutOfBoundsException(index);

        int nodeCount = getNodeCount();
        int off = nodeCount + 1 + index * 4;

        out.set(readNumber(data, numSize, (off + 0) * numSize), readNumber(data, numSize, (off + 1) * numSize),
                readNumber(data, numSize, (off + 2) * numSize), readNumber(data, numSize, (off + 3) * numSize));
    }

    /**
     * Gets the heuristic score assigned with this NodeGraph.
     * 
     * @return The heurisitic score, or 0 if a score has not been assigned.
     */
    public float getHeuristicScore()
    {
        return heuristic;
    }

    /**
     * Assigns the heurisitic score of this NodeGraph.
     * 
     * @param heuristic
     *     - The heurisitic score.
     */
    public void setHeuristicScore(float heuristic)
    {
        this.heuristic = heuristic;
    }

    @Override
    public int compareTo(NodeGraph o)
    {
        return -Float.compare(heuristic, o.heuristic);
    }
}
