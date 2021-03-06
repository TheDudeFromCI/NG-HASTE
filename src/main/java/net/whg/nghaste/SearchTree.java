package net.whg.nghaste;

/**
 * The search tree class is a simple class which serves the purpose of building
 * the search tree for the NG-HASTE algorithm by finding the available children
 * for a given node graph based on the environment settings.
 * <p>
 * This class is not thread safe.
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

    /**
     * This method preforms a single iteration on the graph, by processing the graph
     * and finding all of the given child solutions of the given graph, finally
     * adding those solutions to the container. This function is the heart of the
     * NG-HASTE algorithm and calls all of the functions used to process a graph.
     * 
     * @param graph
     *     - The graphs to process.
     */
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

    /**
     * Checks if the given input plug has any connections leading to it.
     * 
     * @param graph
     *     - The graph to check on.
     * @param nodeIndex
     *     - The index of the node the plug blongs to.
     * @param inputPlugIndex
     *     - The index of the plug.
     * @return True if the given input plug is connected to something. False
     *     otherwise.
     */
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

    /**
     * Finds all of the new graphs that could be creating by adding connections to
     * the given input plug without violating circulation rules or creating new
     * nodes. Axioms are then used to further process the children before finally
     * adding them to the node container.
     * 
     * @param graph
     *     - The graph to process.
     * @param nodeIndex
     *     - The index of the node the plug belongs to.
     * @param inputPlugIndex
     *     - The index of the plug.
     */
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

                processGraph(graph.addConnection(parentNodeIndex, parentOutputIndex, nodeIndex, inputPlugIndex));
            }
        }
    }

    /**
     * Finds all of the new graphs that could be creating by adding connections to a
     * newly created node. Axioms are then used to further process the children
     * before finally adding them to the node container.
     * 
     * @param graph
     *     - The graph to process.
     * @param nodeIndex
     *     - The index of the node the plug belongs to.
     * @param inputPlugIndex
     *     - The index of the plug.
     */
    private void addNewNodes(NodeGraph graph, int nodeIndex, int inputPlugIndex)
    {
        IFunction nodeType = graph.getNodeAsFunction(nodeIndex);
        IDataType[] nodeInputs = nodeType.getInputs();

        int functionCount = graph.getEnvironment()
                                 .getFunctions()
                                 .size();
        for (int functionIndex = 0; functionIndex < functionCount; functionIndex++)
        {
            IDataType[] functionOutputs = graph.getEnvironment()
                                               .getFunctions()
                                               .get(functionIndex)
                                               .getOutputs();
            int functionOutputCount = functionOutputs.length;
            for (int functionOutputIndex = 0; functionOutputIndex < functionOutputCount; functionOutputIndex++)
            {
                if (!functionOutputs[functionOutputIndex].equals(nodeInputs[inputPlugIndex]))
                    continue;

                processGraph(graph.addConnectionAndNode(functionIndex, functionOutputIndex, nodeIndex, inputPlugIndex));
            }
        }
    }

    /**
     * Called on newly created child graphs to validate them before adding them to
     * the container. If they are valid, they are added to the node container. If
     * the graph is invalid, nothing happens. The graph is either added as a new
     * node graph or a solution depending on whether open input plugs exist or not.
     * (A solution is defined as a graph with no open input plugs.)
     * 
     * @param graph
     *     - The child graph to process.
     */
    private void processGraph(NodeGraph graph)
    {
        int connectionCount = graph.getConnectionCount();
        int openPlugs = graph.countOpenPlugs();
        if (connectionCount + openPlugs > graph.getEnvironment()
                                               .getMaxDepth())
            return;

        if (!container.getDuplicateFinder()
                      .isUnquie(hasher, graph))
            return;

        for (IAxiom axiom : graph.getEnvironment()
                                 .getAxioms())
            if (!axiom.isValid(graph))
                return;

        if (openPlugs == 0)
        {
            for (ISolutionAxiom axiom : graph.getEnvironment()
                                             .getSolutionAxioms())
                if (!axiom.isValid(graph))
                    return;

            container.addSolution(graph);
        }
        else
        {
            float heuristic = 0f;

            for (IHeuristic h : graph.getEnvironment()
                                     .getHeuristics())
                heuristic += h.estimateHeuristic(graph);

            graph.setHeuristicScore(heuristic);
            container.addNodeGraph(graph);
        }
    }

    /**
     * Checks if the given node is a parent or ansestor of another node. A parent
     * node is defined as a node which is equal to the child node, or connected
     * through a node of 0 or more nodes to the child not, via output-to-input
     * plugs.
     * 
     * @param graph
     *     - The graph to check in.
     * @param parentNode
     *     - The node to check as a parent.
     * @param childNode
     *     - The node to check as a child.
     * @return True if the node is somehow a parent of the child node.
     */
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
