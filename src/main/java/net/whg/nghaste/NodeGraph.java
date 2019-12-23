package net.whg.nghaste;

import java.util.Arrays;

public class NodeGraph
{
    private static void writeNumber(byte[] bytes, int numSize, int pos, int value)
    {
        for (int i = numSize - 1; i >= 0; i--)
            bytes[pos++] = (byte) ((value >> (i * 8)) & 0xFF);
    }

    private static int readNumber(byte[] bytes, int numSize, int pos)
    {
        int value = 0;

        for (int i = numSize - 1; i >= 0; i--)
            value |= (bytes[pos++] & 0xFF) << i * 8;

        return value;
    }

    public static NodeGraph newGraph(int numSize, int i)
    {
        NodeGraph graph = new NodeGraph(numSize, 2 * numSize);

        writeNumber(graph.data, numSize, 0, 1);
        writeNumber(graph.data, numSize, numSize, i);

        return graph;
    }

    private final int numSize;
    private final byte[] data;

    private NodeGraph(int numSize, int buffer)
    {
        this.numSize = numSize;
        data = new byte[buffer];
    }

    public NodeGraph addConnection(int outputNode, int outputPlug, int inputNode, int inputPlug)
    {
        if (outputNode < 0 || outputNode >= getNodeCount())
            throw new IndexOutOfBoundsException(inputNode);

        if (inputNode < 0 || inputNode >= getNodeCount())
            throw new IndexOutOfBoundsException(inputNode);

        NodeGraph graph = new NodeGraph(numSize, data.length + numSize * 4);

        for (int i = 0; i < data.length; i++)
            graph.data[i] = data[i];

        writeNumber(graph.data, numSize, data.length, outputNode);
        writeNumber(graph.data, numSize, data.length + numSize, outputPlug);
        writeNumber(graph.data, numSize, data.length + numSize * 2, inputNode);
        writeNumber(graph.data, numSize, data.length + numSize * 3, inputPlug);

        return graph;
    }

    public NodeGraph addConnectionAndNode(int nodeType, int outputPlug, int inputNode, int inputPlug)
    {
        if (inputNode < 0 || inputNode >= getNodeCount())
            throw new IndexOutOfBoundsException(inputNode);

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

    public int getNumberSize()
    {
        return numSize;
    }

    public int getNodeCount()
    {
        return readNumber(data, numSize, 0);
    }

    public int getNodeType(int node)
    {
        if (node < 0 || node >= getNodeCount())
            throw new ArrayIndexOutOfBoundsException(node);

        return readNumber(data, numSize, (node + 1) * numSize);
    }

    public int getConnectionCount()
    {
        return (data.length / numSize - 1 - getNodeCount()) / 4;
    }

    public void getConnection(int index, Connection out)
    {
        int nodeCount = getNodeCount();
        int off = nodeCount + 1 + index * 4;

        out.set(readNumber(data, numSize, (off + 0) * numSize), readNumber(data, numSize, (off + 1) * numSize),
                readNumber(data, numSize, (off + 2) * numSize), readNumber(data, numSize, (off + 3) * numSize));
    }
}
