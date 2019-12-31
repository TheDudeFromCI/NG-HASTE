package net.whg.nghaste.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;
import net.whg.nghaste.util.SingleConstAxiom;

public class SingleConstAxiomTest
{
    @Test
    public void singleConst_isValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnection(2, 0, 1, 1);

        SingleConstAxiom axiom = new SingleConstAxiom();
        assertTrue((axiom.isValid(graph)));
    }

    @Test
    public void doubleConst_notValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 1);

        SingleConstAxiom axiom = new SingleConstAxiom();
        assertFalse((axiom.isValid(graph)));
    }
}
