package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import net.whg.nghaste.GraphHash;
import net.whg.nghaste.GraphHasher;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;

public class GraphHasherTest
{
    @Test
    public void compare_equalGraphs()
    {
        NodeGraph graph1 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph1 = graph1.addConnectionAndNode(1, 0, 0, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 1);

        NodeGraph graph2 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph2 = graph2.addConnectionAndNode(1, 0, 0, 0);
        graph2 = graph2.addConnectionAndNode(2, 0, 1, 1);
        graph2 = graph2.addConnectionAndNode(2, 0, 1, 0);

        GraphHasher hasher = new GraphHasher();
        GraphHash hash1 = hasher.createHash(graph1);
        GraphHash hash2 = hasher.createHash(graph2);

        assertEquals(hash1, hash2);
    }

    @Test
    public void compare_unequalGraphs()
    {
        NodeGraph graph1 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph1 = graph1.addConnectionAndNode(1, 0, 0, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 0);
        graph1 = graph1.addConnectionAndNode(2, 0, 1, 1);

        NodeGraph graph2 = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph2 = graph2.addConnectionAndNode(1, 0, 0, 0);
        graph2 = graph2.addConnectionAndNode(1, 0, 1, 0);
        graph2 = graph2.addConnectionAndNode(2, 0, 1, 1);
        graph2 = graph2.addConnectionAndNode(2, 0, 2, 1);

        GraphHasher hasher = new GraphHasher();
        GraphHash hash1 = hasher.createHash(graph1);
        GraphHash hash2 = hasher.createHash(graph2);

        assertNotEquals(hash1, hash2);
    }
}
