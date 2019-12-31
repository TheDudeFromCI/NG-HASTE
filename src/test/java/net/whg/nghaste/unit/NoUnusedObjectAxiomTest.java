package net.whg.nghaste.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;
import net.whg.nghaste.util.NoUnusedObjectAxiom;

public class NoUnusedObjectAxiomTest
{
    @Test
    public void singleOutput_isValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);

        NoUnusedObjectAxiom axiom = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_NUMBER);
        assertTrue((axiom.isValid(graph)));
    }

    @Test
    public void doubleOutput_notValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(11, 0, 0, 0);

        NoUnusedObjectAxiom axiom = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_NUMBER);
        assertFalse((axiom.isValid(graph)));
    }
}
