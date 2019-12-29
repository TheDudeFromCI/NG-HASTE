package net.whg.nghaste;

/**
 * The search tree class is a simple class which serves the purpose of building
 * the search tree for the NG-HASTE algorithm by finding the available children
 * for a given node graph based on the environment settings.
 */
public class SearchTree
{
    private final Connection connectionBuf = new Connection();
    private final GraphHasher hasher = new GraphHasher();
    private final NodeContainer container;

    /**
     * Creates a new search tree object.
     * 
     * @param container
     *     - The container to store new graphs to.
     */
    public SearchTree(NodeContainer container)
    {
        this.container = container;
    }

    public void placeNeighbors(NodeGraph graph)
    {
        int nodeCount = graph.getNodeCount();
        for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++)
        {
            IFunction nodeType = graph.getNodeAsFunction(nodeIndex);
            int inputPlugs = nodeType.getInputs().length;

            for (int inputPlugIndex = 0; inputPlugIndex < inputPlugs; inputPlugIndex++)
            {
                if (hasConnection(graph, nodeIndex, inputPlugIndex))
                    continue;

                addNewConnections(graph, nodeIndex, inputPlugIndex);
                addNewNodes(graph, nodeIndex, inputPlugIndex);
            }
        }
    }

    private boolean hasConnection(NodeGraph graph, int nodeIndex, int inputPlugIndex)
    {
        int connectionCount = graph.getConnectionCount();
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++)
        {
            graph.getConnection(connectionIndex, connectionBuf);

            if (connectionBuf.getInputNode() == nodeIndex && connectionBuf.getInputPlug() == inputPlugIndex)
                return true;
        }

        return false;
    }

    private void addNewConnections(NodeGraph graph, int nodeIndex, int inputPlugIndex)
    {
        IFunction nodeType = graph.getNodeAsFunction(nodeIndex);
        IDataType[] nodeInputs = nodeType.getInputs();
        int nodeCount = graph.getNodeCount();

        for (int parentNodeIndex = 0; parentNodeIndex < nodeCount; parentNodeIndex++)
        {
            if (isParentOf(graph, nodeIndex, parentNodeIndex))
                continue;

            IFunction parentNodeType = graph.getNodeAsFunction(parentNodeIndex);
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
    }

    private void addNewNodes(NodeGraph graph, int nodeIndex, int inputPlugIndex)
    {
        IFunction nodeType = graph.getNodeAsFunction(nodeIndex);
        IDataType[] nodeInputs = nodeType.getInputs();

        int functionCount = graph.getEnvironment()
                                 .getFunctionCount();
        for (int functionIndex = 0; functionIndex < functionCount; functionIndex++)
        {
            IDataType[] functionOutputs = graph.getEnvironment()
                                               .getFunctionAt(functionIndex)
                                               .getOutputs();
            int functionOutputCount = functionOutputs.length;
            for (int functionOutputIndex = 0; functionOutputIndex < functionOutputCount; functionOutputIndex++)
            {
                if (!functionOutputs[functionOutputIndex].equals(nodeInputs[inputPlugIndex]))
                    continue;

                processGraph(container,
                        graph.addConnectionAndNode(functionIndex, functionOutputIndex, nodeIndex, inputPlugIndex));
            }
        }
    }

    private void processGraph(NodeContainer container, NodeGraph graph)
    {
        int connectionCount = graph.getConnectionCount();
        int openPlugs = graph.countOpenPlugs();

        if (connectionCount + openPlugs > graph.getEnvironment()
                                               .getMaxDepth())
            return;

        if (!container.getDuplicateFinder()
                      .isUnquie(hasher, graph))
            return;

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

            if (connectionBuf.getOutputNode() == parentNode
                    && isParentOf(graph, connectionBuf.getInputNode(), childNode))
                return true;
        }

        return false;
    }
}
