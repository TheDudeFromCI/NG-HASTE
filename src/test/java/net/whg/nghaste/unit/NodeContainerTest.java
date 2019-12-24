package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.NodeContainer;
import net.whg.nghaste.NodeGraph;

public class NodeContainerTest
{
    @Test
    public void addElements()
    {
        NodeContainer container = new NodeContainer();

        NodeGraph graph1 = NodeGraph.newGraph(1, 0);
        NodeGraph graph2 = NodeGraph.newGraph(1, 0);
        NodeGraph graph3 = NodeGraph.newGraph(1, 0);
        graph1.setHeuristicScore(1);
        graph2.setHeuristicScore(-10);
        graph3.setHeuristicScore(150);

        container.addNodeGraph(graph1);
        container.addNodeGraph(graph2);
        container.addNodeGraph(graph3);

        assertEquals(3, container.size());
        assertEquals(graph3, container.getNodeGraph());
        assertEquals(graph1, container.getNodeGraph());
        assertEquals(graph2, container.getNodeGraph());
        assertNull(container.getNodeGraph());
    }

    @Test
    public void addElements_Multithread() throws InterruptedException
    {
        NodeContainer container = new NodeContainer();

        boolean[] isNull = new boolean[4];
        Thread t1 = startThread(container, isNull, 0);
        Thread t2 = startThread(container, isNull, 1);
        Thread t3 = startThread(container, isNull, 2);
        Thread t4 = startThread(container, isNull, 3);

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        assertFalse(isNull[0]);
        assertFalse(isNull[1]);
        assertFalse(isNull[2]);
        assertFalse(isNull[3]);
        assertEquals(20000, container.size());

        float last = Float.MAX_VALUE;
        while (container.size() > 0)
        {
            NodeGraph graph = container.getNodeGraph();
            assertTrue(graph.getHeuristicScore() <= last);

            last = graph.getHeuristicScore();
        }
    }

    private Thread startThread(NodeContainer container, boolean[] isNull, int index)
    {
        Thread t = new Thread(() ->
        {
            for (int i = 0; i < 10000; i++)
            {
                NodeGraph graph = NodeGraph.newGraph(1, 0);
                graph.setHeuristicScore(i);
                container.addNodeGraph(graph);

                if (i % 2 == 0)
                    if (container.getNodeGraph() == null)
                        isNull[index] = true;
            }
        });

        t.setDaemon(true);
        t.start();

        return t;
    }
}
