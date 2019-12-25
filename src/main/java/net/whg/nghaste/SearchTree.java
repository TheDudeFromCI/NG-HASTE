package net.whg.nghaste;

/**
 * The search tree class is a simple class which serves the purpose of building
 * the search tree for the NG-HASTE algorithm by finding the available children
 * for a given node graph based on the environment settings.
 */
public class SearchTree
{
    private final Connection connectionBuf = new Connection();
    private final Environment environment;

    public SearchTree(Environment environment)
    {
        this.environment = environment;
    }

    public void placeNeighbors(NodeContainer container, NodeGraph graph)
    {
        int nodeCount = graph.getNodeCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            IFunction nodeType = environment.getFunctionAt(graph.getNodeType(nodeIndex));
            IDataType[] nodeInputs = nodeType.getInputs();
            int inputPlugs = nodeInputs.length;

            plugChecker:
            for (int inputPlugIndex = 0; inputPlugIndex < inputPlugs; inputPlugIndex++)
            {
                int connectionCount = graph.getConnectionCount();
                for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
                {
                    graph.getConnection(connectionIndex, connectionBuf);

                    if (connectionBuf.getInputNode() == nodeIndex && connectionBuf.getInputPlug() == inputPlugIndex)
                        continue plugChecker;
                }

                for (int parentNodeIndex = 0; parentNodeIndex < nodeCount; parentNodeIndex++)
                {
                    if (isParentOf(graph, nodeIndex, parentNodeIndex))
                        continue;

                    IFunction parentNodeType = environment.getFunctionAt(graph.getNodeType(parentNodeIndex));
                    IDataType[] parentOutputs = parentNodeType.getOutputs();

                    int parentOutputCount = parentOutputs.length;
                    for (int parentOutputIndex = 0; parentOutputIndex < parentOutputCount; parentOutputIndex++)
                    {
                        if (!parentOutputs[parentOutputIndex].equals(nodeInputs[inputPlugIndex]))
                            continue;

                        processGraph(container,
                                graph.addConnection(parentNodeIndex, parentOutputIndex, nodeIndex, inputPlugIndex));
                    }
                }

                int functionCount = environment.getFunctionCount();
                for (int functionIndex = 0; functionIndex < functionCount; functionIndex++)
                {
                    IDataType[] functionOutputs = environment.getFunctionAt(functionIndex)
                                                             .getOutputs();
                    int functionOutputCount = functionOutputs.length;
                    for (int functionOutputIndex = 0; functionOutputIndex < functionOutputCount; functionOutputIndex++)
                    {
                        if (!functionOutputs[functionOutputIndex].equals(nodeInputs[inputPlugIndex]))
                            continue;

                        processGraph(container, graph.addConnectionAndNode(functionIndex, functionOutputIndex,
                                nodeIndex, inputPlugIndex));
                    }
                }
            }
        }
    }

    private void processGraph(NodeContainer container, NodeGraph graph)
    {
        int connectionCount = graph.getConnectionCount();
        int openPlugs = graph.countOpenPlugs();

        if (connectionCount + openPlugs > environment.getMaxDepth())
            return;

        // TODO Check graph for duplicates
        // TODO Cull graph which breaks axioms
        // TODO Calculate graph heuristics

        if (openPlugs == 0)
            container.addSolution(graph);
        else
            container.addNodeGraph(graph);
    }

    private boolean isParentOf(NodeGraph graph, int parentNode, int childNode)
    {
        if (parentNode == childNode)
            return true;

        int connectionCount = graph.getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            graph.getConnection(connectionIndex, connectionBuf);

            if (connectionBuf.getOutputNode() == parentNode)
            {
                if (isParentOf(graph, connectionBuf.getInputNode(), childNode))
                    return true;
            }
        }

        return false;
    }
}
