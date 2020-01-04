package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.NodeContainer;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.SearchTree;
import net.whg.nghaste.util.EnvironmentUtils;

public class SearchTreeAxiomsTest
{
    @Test
    public void axioms_noDoubleConsts()
    {
        Environment env = EnvironmentUtils.quickEnvironment(5, builder -> builder.addAxiom(graph ->
        {
            int numberConsts = 0;
            int textConsts = 0;
            int boolConsts = 0;

            int nodeCount = graph.getNodeCount();
            for (int i = 0; i < nodeCount; i++)
            {
                IFunction type = graph.getNodeAsFunction(i);

                if (type == EnvironmentUtils.FUNC2_NUM_CONST)
                    numberConsts++;

                if (type == EnvironmentUtils.FUNC3_TEXT_CONST)
                    textConsts++;

                if (type == EnvironmentUtils.FUNC4_BOOL_CONST)
                    boolConsts++;
            }

            return numberConsts <= 1 && textConsts <= 1 && boolConsts <= 1;
        }));

        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);
        }

        assertEquals(1109, graphCount);
    }

    @Test
    public void allGraphs_5deep_controlTest()
    {
        Environment env = EnvironmentUtils.quickEnvironment(5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);
        }

        assertEquals(1115, graphCount);
        assertEquals(878, container.getSolutionCount());
    }

    @Test
    public void solutionAxioms_textCastException()
    {
        Environment env = EnvironmentUtils.quickEnvironment(5, builder ->
        {
            builder.addSolutionAxiom(graph ->
            {
                int nodeCount = graph.getNodeCount();
                for (int i = 0; i < nodeCount; i++)
                {
                    IFunction type = graph.getNodeAsFunction(i);

                    if (type == EnvironmentUtils.FUNC8_TEXT_TO_BOOL)
                        return false;

                    if (type == EnvironmentUtils.FUNC7_TEXT_TO_INT)
                        return false;
                }

                return true;
            });
        });
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);
        }

        assertEquals(1115, graphCount);
        assertEquals(668, container.getSolutionCount());
    }

    @Test
    public void heuristic_breadthFirst()
    {
        Environment env = EnvironmentUtils.quickEnvironment(5, builder ->
        { builder.addHeuristic(graph -> -graph.getConnectionCount()); });

        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        int connectionLast = 0;

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();

            int con = g.getConnectionCount();
            assertTrue(con >= connectionLast);
            connectionLast = con;

            tree.placeNeighbors(g);
        }

        assertNotEquals(0, connectionLast);

        assertEquals(1115, graphCount);
        assertEquals(878, container.getSolutionCount());
    }
}
