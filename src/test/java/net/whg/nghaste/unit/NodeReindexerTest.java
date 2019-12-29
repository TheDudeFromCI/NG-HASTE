package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.NodeReindexer;
import net.whg.nghaste.util.EnvironmentUtils;

public class NodeReindexerTest
{
    @Test
    public void noDuplicateIndices()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);

        Map<Integer, Integer> indices = new HashMap<>();
        NodeReindexer.reindex(graph, indices);

        assertEquals(indices.size(), graph.getNodeCount());

        for (int nodeIndex = 0; nodeIndex < graph.getNodeCount(); nodeIndex++)
            assertTrue(indices.containsKey(nodeIndex));

        for (Integer a : indices.keySet())
        {
            int count = 0;
            for (Integer b : indices.keySet())
            {
                if (indices.get(a)
                           .equals(indices.get(b)))
                    count++;
            }

            assertEquals(1, count);
        }
    }

    @Test
    public void consistentIndices()
    {
        NodeGraph graph1 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph1 = graph1.addConnectionAndNode(1, 0, 0, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 1);

        NodeGraph graph2 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph2 = graph2.addConnectionAndNode(1, 0, 0, 0);
        graph2 = graph2.addConnectionAndNode(2, 0, 1, 1);
        graph2 = graph2.addConnectionAndNode(2, 0, 1, 0);

        Map<Integer, Integer> indices1 = new HashMap<>();
        NodeReindexer.reindex(graph1, indices1);

        Map<Integer, Integer> indices2 = new HashMap<>();
        NodeReindexer.reindex(graph2, indices2);

        assertEquals(indices1.get(0), indices2.get(0));
        assertEquals(indices1.get(1), indices2.get(1));
        assertEquals(indices1.get(2), indices2.get(3));
        assertEquals(indices1.get(3), indices2.get(2));
    }
}
