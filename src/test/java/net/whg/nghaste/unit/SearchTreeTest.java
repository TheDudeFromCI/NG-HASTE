package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockingDetails;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.NodeContainer;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.SearchTree;
import net.whg.nghaste.util.EnvironmentUtils;

public class SearchTreeTest
{
    @Test
    public void search1_onlyOutput()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        tree.placeNeighbors(NodeGraph.newGraph(env, 0));

        assertEquals(7, container.size());
        assertEquals(1, container.getSolutionCount());

        for (int i = 0; i < 7; i++)
        {
            NodeGraph g = container.getNodeGraph();
            assertEquals(2, g.getNodeCount());
            assertEquals(1, g.getConnectionCount());
            assertEquals(0, g.getNodeType(0));
            assertNotEquals(0, g.getNodeType(1));
        }
    }

    @Test
    public void search2_2Layers()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        tree.placeNeighbors(NodeGraph.newGraph(env, 0));

        List<NodeGraph> outputs = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            outputs.add(container.getNodeGraph());

        for (int i = 0; i < 7; i++)
            tree.placeNeighbors(outputs.get(i));

        assertEquals(43, container.size());
    }

    @Test
    public void search3_matchingDataTypes()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        tree.placeNeighbors(NodeGraph.newGraph(env, 0));

        List<NodeGraph> outputs = new ArrayList<>();
        while (container.size() > 0)
        {
            NodeGraph graph = container.getNodeGraph();
            IFunction function = graph.getNodeAsFunction(1);

            for (IDataType dataType : function.getInputs())
            {
                String name = mockingDetails(dataType).getMockCreationSettings()
                                                      .getMockName()
                                                      .toString();

                if (name.equals("type_text"))
                {
                    outputs.add(graph);
                    break;
                }
            }
        }

        assertTrue(outputs.size() > 0);

        for (int i = 0; i < outputs.size(); i++)
            tree.placeNeighbors(outputs.get(i));

        while (container.size() > 0)
        {
            NodeGraph graph = container.getNodeGraph();
            IFunction function = graph.getNodeAsFunction(2);

            boolean hasText = false;
            for (IDataType dataType : function.getOutputs())
            {
                String name = mockingDetails(dataType).getMockCreationSettings()
                                                      .getMockName()
                                                      .toString();

                if (name.equals("type_text"))
                    hasText = true;
            }

            assertTrue(hasText);
        }
    }

    @Test(timeout = 3000)
    public void maxDepth()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 3);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        container.addNodeGraph(NodeGraph.newGraph(env, 0));
        while (container.size() > 0)
        {
            NodeGraph g = container.getNodeGraph();
            assertTrue(g.getConnectionCount() + g.countOpenPlugs() <= 3);

            tree.placeNeighbors(g);
        }
    }

    @Test(timeout = 3000)
    public void publishSolution()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 3);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        container.addNodeGraph(NodeGraph.newGraph(env, 0));
        while (container.size() > 0)
        {
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);
        }

        int solCount = container.getSolutionCount();
        assertTrue(solCount > 0);

        for (int i = 0; i < solCount; i++)
            assertEquals(0, container.getSolution(i)
                                     .countOpenPlugs());
    }
}
