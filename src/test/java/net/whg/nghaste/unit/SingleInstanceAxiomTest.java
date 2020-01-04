package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;
import net.whg.nghaste.util.SingleInstanceAxiom;

public class SingleInstanceAxiomTest
{
    @Test
    public void singleOutput_isValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(1, 0, 1, 0);
        graph = graph.addConnectionAndNode(2, 0, 2, 1);

        SingleInstanceAxiom axiom = new SingleInstanceAxiom(EnvironmentUtils.DATA_NUMBER);
        assertTrue((axiom.isValid(graph)));
    }

    @Test
    public void doubleOutput_notValid()
    {
        NodeGraph graph = NodeGraph.newGraph(EnvironmentUtils.quickEnvironment(), 0);
        graph = graph.addConnectionAndNode(1, 0, 0, 0);
        graph = graph.addConnectionAndNode(2, 0, 1, 0);
        graph = graph.addConnection(2, 0, 1, 1);

        SingleInstanceAxiom axiom = new SingleInstanceAxiom(EnvironmentUtils.DATA_NUMBER);
        assertFalse((axiom.isValid(graph)));
    }

    @Test
    public void equals_sameInstance()
    {
        SingleInstanceAxiom axiom = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);

        assertEquals(axiom, axiom);
        assertEquals(axiom.hashCode(), axiom.hashCode());
    }

    @Test
    public void equals_diffInstance()
    {
        SingleInstanceAxiom axiom1 = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);
        SingleInstanceAxiom axiom2 = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);

        assertEquals(axiom1, axiom2);
        assertEquals(axiom1.hashCode(), axiom2.hashCode());
    }

    @Test
    public void notEquals_diffInstance_diffDataType()
    {
        SingleInstanceAxiom axiom1 = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);
        SingleInstanceAxiom axiom2 = new SingleInstanceAxiom(EnvironmentUtils.DATA_NUMBER);

        assertNotEquals(axiom1, axiom2);
        assertNotEquals(axiom1.hashCode(), axiom2.hashCode());
    }

    @Test
    public void notEquals_diffObject()
    {
        SingleInstanceAxiom axiom = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);
        assertNotEquals(axiom, new Object());
    }

    @Test
    public void notEquals_null()
    {
        SingleInstanceAxiom axiom = new SingleInstanceAxiom(EnvironmentUtils.DATA_BOOL);
        assertNotEquals(axiom, null);
    }
}
