package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.impl.Environment;
import net.whg.nghaste.impl.NodeContainer;
import net.whg.nghaste.impl.NodeGraph;
import net.whg.nghaste.impl.SearchTree;
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
        SearchTree tree = new SearchTree();
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        List<NodeGraph> graphs = tree.getOutputGraphs();
        List<NodeGraph> solutions = tree.getSolutions();

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);

            for (NodeGraph g1 : graphs)
                container.addNodeGraph(g1);
            graphs.clear();

            for (NodeGraph g1 : solutions)
                container.addSolution(g1);
            solutions.clear();
        }

        assertEquals(676, graphCount);
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
        SearchTree tree = new SearchTree();
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        List<NodeGraph> graphs = tree.getOutputGraphs();
        List<NodeGraph> solutions = tree.getSolutions();

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);

            for (NodeGraph g1 : graphs)
                container.addNodeGraph(g1);
            graphs.clear();

            for (NodeGraph g1 : solutions)
                container.addSolution(g1);
            solutions.clear();
        }

        assertEquals(678, graphCount);
        assertEquals(667, container.getSolutionCount());
    }

    @Test
    public void heuristic_breadthFirst()
    {
        Environment env = EnvironmentUtils.quickEnvironment(5, builder ->
        { builder.addHeuristic(graph -> -graph.getConnectionCount()); });

        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree();
        container.addNodeGraph(NodeGraph.newGraph(env, 0));

        int connectionLast = 0;

        List<NodeGraph> graphs = tree.getOutputGraphs();
        List<NodeGraph> solutions = tree.getSolutions();

        int graphCount = 0;
        while (container.size() > 0)
        {
            graphCount++;
            NodeGraph g = container.getNodeGraph();

            int con = g.getConnectionCount();
            assertTrue(con >= connectionLast);
            connectionLast = con;

            tree.placeNeighbors(g);

            for (NodeGraph g1 : graphs)
                container.addNodeGraph(g1);
            graphs.clear();

            for (NodeGraph g1 : solutions)
                container.addSolution(g1);
            solutions.clear();
        }

        assertNotEquals(0, connectionLast);

        assertEquals(678, graphCount);
        assertEquals(877, container.getSolutionCount());
    }
}
