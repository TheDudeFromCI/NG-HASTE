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
    public void set(int outputNode, int outputPlug, int inputNode, int inputPlug)
    {
        this.outputNode = outputNode;
        this.outputPlug = outputPlug;
        this.inputNode = inputNode;
        this.inputPlug = inputPlug;
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
}
