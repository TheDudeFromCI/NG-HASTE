package net.whg.nghaste;

public class Connection
{
    private int outputNode;
    private int outputPlug;
    private int inputNode;
    private int inputPlug;

    public void set(int outputNode, int outputPlug, int inputNode, int inputPlug)
    {
        this.outputNode = outputNode;
        this.outputPlug = outputPlug;
        this.inputNode = inputNode;
        this.inputPlug = inputPlug;
    }

    public int getOutputNode()
    {
        return outputNode;
    }

    public int getOutputPlug()
    {
        return outputPlug;
    }

    public int getInputNode()
    {
        return inputNode;
    }

    public int getInputPlug()
    {
        return inputPlug;
    }
}
