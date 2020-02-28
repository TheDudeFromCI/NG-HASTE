package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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

    @Test
    public void equals_sameInstance()
    {
        SingleConstAxiom axiom = new SingleConstAxiom();

        assertEquals(axiom, axiom);
        assertEquals(axiom.hashCode(), axiom.hashCode());
    }

    @Test
    public void equals_diffInstance()
    {
        SingleConstAxiom axiom1 = new SingleConstAxiom();
        SingleConstAxiom axiom2 = new SingleConstAxiom();

        assertEquals(axiom1, axiom2);
        assertEquals(axiom1.hashCode(), axiom2.hashCode());
    }

    @Test
    public void notEquals_diffObject()
    {
        SingleConstAxiom axiom = new SingleConstAxiom();
        assertNotEquals(axiom, new Object());
    }

    @Test
    public void notEquals_null()
    {
        SingleConstAxiom axiom = new SingleConstAxiom();
        assertNotEquals(axiom, null);
    }
}
