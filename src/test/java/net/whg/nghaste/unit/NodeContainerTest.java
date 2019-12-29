package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.NodeContainer;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;

public class NodeContainerTest
{
    @Test
    public void addElements()
    {
        NodeContainer container = new NodeContainer();
        NodeGraph graph1 = graph(1);
        NodeGraph graph2 = graph(-10);
        NodeGraph graph3 = graph(150);

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
        for (int i = 0; i < 25; i++)
        {
            NodeContainer container = new NodeContainer();

            Thread t1 = startThread(container, 0);
            Thread t2 = startThread(container, 1);
            Thread t3 = startThread(container, 2);
            Thread t4 = startThread(container, 3);

            t1.join();
            t2.join();
            t3.join();
            t4.join();

            assertEquals(20000, container.size());

            float last = Float.MAX_VALUE;
            while (container.size() > 0)
            {
                NodeGraph graph = container.getNodeGraph();
                assertTrue(graph.getHeuristicScore() <= last);

                last = graph.getHeuristicScore();
            }
        }
    }

    private Thread startThread(NodeContainer container, int index)
    {
        Thread t = new Thread(() ->
        {
            Environment env = EnvironmentUtils.quickEnvironment();

            for (int i = 0; i < 10000; i++)
            {
                NodeGraph g = NodeGraph.newGraph(env, 0);
                g.setHeuristicScore(i);
                container.addNodeGraph(g);

                if (i % 2 == 0)
                    assertNotNull(container.getNodeGraph());
            }
        });

        t.setDaemon(true);
        t.start();

        return t;
    }

    @Test
    public void sort_1000random()
    {
        NodeContainer container = new NodeContainer();

        for (int i = 0; i < 1000; i++)
            container.addNodeGraph(graph((float) Math.random()));

        container.sort();

        float last = Float.MAX_VALUE;
        while (container.size() > 0)
        {
            NodeGraph graph = container.getNodeGraph();
            assertTrue(graph.getHeuristicScore() <= last);

            last = graph.getHeuristicScore();
        }
    }

    @Test
    public void update_middleOf10()
    {
        NodeContainer container = new NodeContainer();

        for (int i = 0; i < 10; i++)
            container.addNodeGraph(graph(i));

        NodeGraph g = graph(5);
        container.addNodeGraph(g);

        g.setHeuristicScore(1.5f);
        container.update(g);

        float last = Float.MAX_VALUE;
        while (container.size() > 0)
        {
            NodeGraph graph = container.getNodeGraph();
            assertTrue(graph.getHeuristicScore() <= last);

            last = graph.getHeuristicScore();
        }
    }

    private NodeGraph graph(float h)
    {
        Environment env = EnvironmentUtils.quickEnvironment();

        NodeGraph g = NodeGraph.newGraph(env, 0);
        g.setHeuristicScore(h);
        return g;
    }
}
