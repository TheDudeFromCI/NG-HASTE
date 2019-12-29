package net.whg.nghaste.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.DuplicateFinder;
import net.whg.nghaste.GraphHasher;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;

public class DuplicateFinderTest
{
    @Test
    public void addGraphs()
    {
        DuplicateFinder dup = new DuplicateFinder();
        GraphHasher hasher = new GraphHasher();

        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);

        assertTrue(dup.isUnquie(hasher, graph));

        graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnection(2, 0, 1, 1);

        assertTrue(dup.isUnquie(hasher, graph));

        graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);

        assertFalse(dup.isUnquie(hasher, graph));
    }

    @Test
    public void remainingDuplicates()
    {
        DuplicateFinder dup = new DuplicateFinder();
        GraphHasher hasher = new GraphHasher();

        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);

        assertTrue(dup.isUnquie(hasher, graph));
        assertEquals(0, dup.getRemainingDuplicateTypes());

        graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);

        assertTrue(dup.isUnquie(hasher, graph));
        assertEquals(1, dup.getRemainingDuplicateTypes());

        graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);

        assertFalse(dup.isUnquie(hasher, graph));
        assertEquals(0, dup.getRemainingDuplicateTypes());
    }

    @Test
    public void duplicate_alternatingConnections()
    {
        DuplicateFinder dup = new DuplicateFinder();
        GraphHasher hasher = new GraphHasher();

        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnection(2, 0, 1, 1);

        assertTrue(dup.isUnquie(hasher, graph));
        assertEquals(1, dup.getRemainingDuplicateTypes());

        graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);
        graph = graph.addConnection(2, 0, 1, 0);

        assertFalse(dup.isUnquie(hasher, graph));
        assertEquals(0, dup.getRemainingDuplicateTypes());
    }
}
