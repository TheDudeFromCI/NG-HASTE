package net.whg.nghaste.impl;

import java.util.Arrays;
import net.whg.nghaste.IFunction;

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
     * Creates a new node graph containing a single node, the output node.
     * 
     * @param environment
     *     - The environment this node graph exists within.
     * @param nodeType
     *     - The type of node to use as the output node.
     * @return The newly created node graph.
     */
    public static NodeGraph newGraph(Environment environment, int nodeType)
    {
        NodeGraph graph = new NodeGraph(environment, environment.getMinByteCount() * 2);

        graph.writeNumber(0, 1);
        graph.writeNumber(1, nodeType);

        return graph;
    }

    private final Environment environment;
    private final int numSize;
    private final byte[] data;
    private float heuristic;

    /**
     * Creates a new, empty node graph.
     * 
     * @param environment
     *     - The environment this node graph exists within.
     * @param buffer
     *     - The size of the byte array.
     */
    private NodeGraph(Environment environment, int buffer)
    {
        this.environment = environment;
        this.numSize = environment.getMinByteCount();
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

        NodeGraph graph = new NodeGraph(environment, data.length + numSize * 4);

        System.arraycopy(data, 0, graph.data, 0, data.length);

        int off = data.length / numSize;
        graph.writeNumber(off++, outputNode);
        graph.writeNumber(off++, outputPlug);
        graph.writeNumber(off++, inputNode);
        graph.writeNumber(off, inputPlug);

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

        NodeGraph graph = new NodeGraph(environment, data.length + 5 * numSize);

        int nodeCount = getNodeCount();
        int connCount = getConnectionCount();
        int j = 0;

        graph.writeNumber(j++, nodeCount + 1);

        for (int i = 0; i < nodeCount; i++)
            graph.writeNumber(j++, getNodeType(i));
        graph.writeNumber(j++, nodeType);

        for (int i = 0; i < connCount * 4; i++)
            graph.writeNumber(j++, readNumber(nodeCount + 1 + i));

        graph.writeNumber(j++, nodeCount);
        graph.writeNumber(j++, outputPlug);
        graph.writeNumber(j++, inputNode);
        graph.writeNumber(j, inputPlug);

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
        return readNumber(0);
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

        return readNumber(node + 1);
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

        out.set(readNumber(off + 0), readNumber(off + 1), readNumber(off + 2), readNumber(off + 3),
                getNodeType(readNumber(off + 0)), getNodeType(readNumber(off + 2)));
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

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof NodeGraph))
            return false;

        NodeGraph g = (NodeGraph) o;
        if (!Arrays.equals(data, g.data))
            return false;

        return Math.abs(heuristic - g.heuristic) <= 0.0000001;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(data);
    }

    /**
     * Counts the number of open input nodes in this graph.
     * 
     * @return The number of open input plugs.
     */
    public int countOpenPlugs()
    {
        int openPlugs = -getConnectionCount();

        int nodeCount = getNodeCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            IFunction nodeType = environment.getFunctions()
                                            .get(getNodeType(nodeIndex));
            openPlugs += nodeType.getInputs().length;
        }

        return openPlugs;
    }

    /**
     * A small utility function for writing a number to a byte array.
     * 
     * @param pos
     *     - The byte position to write to.
     * @param value
     *     - The value to write.
     */
    private void writeNumber(int pos, int value)
    {
        pos *= numSize;
        for (int i = numSize - 1; i >= 0; i--)
            data[pos++] = (byte) ((value >> (i * 8)) & 0xFF);
    }

    /**
     * A small utility function for reading a number from a byte array.
     * 
     * @param pos
     *     - The byte position to read from.
     * @return The value at the given position.
     */
    private int readNumber(int pos)
    {
        int value = 0;

        pos *= numSize;
        for (int i = numSize - 1; i >= 0; i--)
            value |= (data[pos++] & 0xFF) << i * 8;

        return value;
    }

    /**
     * Gets the function which belongs to the node at the specified index.
     * 
     * @param index
     *     - The index of the node.
     * @return The function which belongs to the node at given index.
     * @throws ArrayIndexOutOfBoundsException
     *     If the index points to a node which does not exist.
     */
    public IFunction getNodeAsFunction(int index)
    {
        return environment.getFunctions()
                          .get(getNodeType(index));
    }

    /**
     * Gets the environment object being used by this node graph.
     * 
     * @return The environment.
     */
    public Environment getEnvironment()
    {
        return environment;
    }

    public int getOutputConnectionCount(int nodeIndex, int plugIndex)
    {
        if (nodeIndex < 0 || nodeIndex >= getNodeCount())
            throw new ArrayIndexOutOfBoundsException("Node does not exist: " + nodeIndex);

        if (plugIndex < 0 || plugIndex >= getNodeAsFunction(nodeIndex).getOutputs().length)
            throw new ArrayIndexOutOfBoundsException("Output plug does not exist: " + plugIndex);

        Connection connection = new Connection();
        int cons = 0;

        int connectionCount = getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            getConnection(connectionIndex, connection);

            if (connection.getOutputNode() == nodeIndex && connection.getOutputPlug() == plugIndex)
                cons++;
        }

        return cons;
    }

    @Override
    public String toString()
    {
        int connCount = getConnectionCount();
        int nodeCount = getNodeCount();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("NodeGraph: (%d Connections, %d Nodes)", connCount, nodeCount));

        for (int i = 0; i < nodeCount; i++)
        {
            sb.append("\n  ")
              .append(i)
              .append(") ")
              .append(getNodeAsFunction(i));
        }

        Connection conn = new Connection();
        for (int i = 0; i < connCount; i++)
        {
            getConnection(i, conn);
            sb.append(String.format("\n  - %d:%d => %d:%d", conn.getOutputNode(), conn.getOutputPlug(),
                    conn.getInputNode(), conn.getInputPlug()));
        }

        return sb.toString();
    }
}
