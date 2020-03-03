package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.impl.NodeGraph;
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

    @Test
    public void equals_sameInstance()
    {
        NoUnusedObjectAxiom axiom = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);

        assertEquals(axiom, axiom);
        assertEquals(axiom.hashCode(), axiom.hashCode());
    }

    @Test
    public void equals_diffInstance()
    {
        NoUnusedObjectAxiom axiom1 = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);
        NoUnusedObjectAxiom axiom2 = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);

        assertEquals(axiom1, axiom2);
        assertEquals(axiom1.hashCode(), axiom2.hashCode());
    }

    @Test
    public void notEquals_diffInstance_diffDataType()
    {
        NoUnusedObjectAxiom axiom1 = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);
        NoUnusedObjectAxiom axiom2 = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_NUMBER);

        assertNotEquals(axiom1, axiom2);
        assertNotEquals(axiom1.hashCode(), axiom2.hashCode());
    }

    @Test
    public void notEquals_diffObject()
    {
        NoUnusedObjectAxiom axiom = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);
        assertNotEquals(axiom, new Object());
    }

    @Test
    public void notEquals_null()
    {
        NoUnusedObjectAxiom axiom = new NoUnusedObjectAxiom(EnvironmentUtils.DATA_BOOL);
        assertNotEquals(axiom, null);
    }
}
