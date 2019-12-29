package net.whg.nghaste;

/**
 * A connection is a pointer for how information should be moved around within a
 * node graph. This object is a data-only object which simply stores the values
 * used in the pointer.
 */
public class Connection
{
    private int outputNode;
    private int outputPlug;
    private int inputNode;
    private int inputPlug;
    private int outputNodeType;
    private int inputNodeType;

    /**
     * Assigns the values of this connection.
     * 
     * @param outputNode
     *     - The node sending the connection.
     * @param outputPlug
     *     - The plug of the output node.
     * @param inputNode
     *     - The node receiving the connection.
     * @param inputPlug
     *     - The plug of the input node.
     */
    public void set(int outputNode, int outputPlug, int inputNode, int inputPlug, int outputNodeType, int inputNodeType)
    {
        this.outputNode = outputNode;
        this.outputPlug = outputPlug;
        this.inputNode = inputNode;
        this.inputPlug = inputPlug;
        this.outputNodeType = outputNodeType;
        this.inputNodeType = inputNodeType;
    }

    /**
     * Gets the output node.
     * 
     * @return The output node.
     */
    public int getOutputNode()
    {
        return outputNode;
    }

    /**
     * Gets the plug for the output node.
     * 
     * @return The output plug.
     */
    public int getOutputPlug()
    {
        return outputPlug;
    }

    /**
     * Gets the input node.
     * 
     * @return The input node.
     */
    public int getInputNode()
    {
        return inputNode;
    }

    /**
     * Gets the plug for the input node.
     * 
     * @return The input plug.
     */
    public int getInputPlug()
    {
        return inputPlug;
    }

    /**
     * Gets the node type for the output node.
     * 
     * @return The output node type.
     */
    public int getOutputNodeType()
    {
        return outputNodeType;
    }

    /**
     * Gets the node type for the input node.
     * 
     * @return The input node type.
     */
    public int getInputNodeType()
    {
        return inputNodeType;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof Connection))
            return false;

        Connection c = (Connection) obj;

        return outputNode == c.outputNode && outputPlug == c.outputPlug && inputNode == c.inputNode
                && inputPlug == c.inputPlug && outputNodeType == c.outputNodeType && inputNodeType == c.inputNodeType;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int value = 1;
        value = prime * value + outputNode;
        value = prime * value + inputNode;
        value = prime * value + outputPlug;
        value = prime * value + inputPlug;
        value = prime * value + outputNodeType;
        value = prime * value + inputNodeType;

        return value;
    }

    @Override
    public String toString()
    {
        return String.format("%d:%d -> %d:%d (%d -> %d)", outputNode, outputPlug, inputNode, inputPlug, outputNodeType,
                inputNodeType);
    }
}
