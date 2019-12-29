package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import net.whg.nghaste.Connection;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;

public class NodeGraphTest
{
    private NodeGraph graph(int bytes, int nodeType)
    {
        IFunction func = mock(IFunction.class);
        when(func.getInputs()).thenReturn(new IDataType[0]);
        when(func.getOutputs()).thenReturn(new IDataType[0]);

        Environment env = EnvironmentUtils.quickEnvironment(1 << (bytes * 7));

        NodeGraph g = NodeGraph.newGraph(env, nodeType);
        return g;
    }

    @Test
    public void create()
    {
        NodeGraph graph = graph(4, 27);

        assertEquals(1, graph.getNodeCount());
        assertEquals(0, graph.getConnectionCount());
        assertEquals(27, graph.getNodeType(0));
        assertEquals(4, graph.getNumberSize());
    }

    @Test
    public void create_connectionAddNode()
    {
        NodeGraph graph = graph(2, 42);
        graph = graph.addConnectionAndNode(18, 0, 0, 2);

        assertEquals(2, graph.getNodeCount());
        assertEquals(1, graph.getConnectionCount());
        assertEquals(42, graph.getNodeType(0));
        assertEquals(18, graph.getNodeType(1));

        Connection conn = new Connection();
        graph.getConnection(0, conn);

        assertEquals(1, conn.getOutputNode());
        assertEquals(0, conn.getOutputPlug());
        assertEquals(0, conn.getInputNode());
        assertEquals(2, conn.getInputPlug());
    }

    @Test
    public void create_connectionAddNode_2deep()
    {
        NodeGraph graph = graph(1, 5);
        graph = graph.addConnectionAndNode(7, 0, 0, 2);
        graph = graph.addConnectionAndNode(9, 0, 1, 1);

        assertEquals(3, graph.getNodeCount());
        assertEquals(2, graph.getConnectionCount());
        assertEquals(5, graph.getNodeType(0));
        assertEquals(7, graph.getNodeType(1));
        assertEquals(9, graph.getNodeType(2));

        Connection conn = new Connection();
        graph.getConnection(0, conn);

        assertEquals(1, conn.getOutputNode());
        assertEquals(0, conn.getOutputPlug());
        assertEquals(0, conn.getInputNode());
        assertEquals(2, conn.getInputPlug());

        graph.getConnection(1, conn);

        assertEquals(2, conn.getOutputNode());
        assertEquals(0, conn.getOutputPlug());
        assertEquals(1, conn.getInputNode());
        assertEquals(1, conn.getInputPlug());
    }

    @Test
    public void create_connection()
    {
        NodeGraph graph = graph(4, 5);
        graph = graph.addConnectionAndNode(4, 13, 0, 12);
        graph = graph.addConnectionAndNode(19, 3, 1, 8);
        graph = graph.addConnection(2, 19, 0, 1);

        assertEquals(3, graph.getNodeCount());
        assertEquals(3, graph.getConnectionCount());

        Connection conn = new Connection();
        graph.getConnection(2, conn);

        assertEquals(2, conn.getOutputNode());
        assertEquals(19, conn.getOutputPlug());
        assertEquals(0, conn.getInputNode());
        assertEquals(1, conn.getInputPlug());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void create_connection_outputNode_outOfBounds()
    {
        NodeGraph graph = graph(1, 0);
        graph.addConnection(-1, 0, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void create_connection_inputNode_outOfBounds()
    {
        NodeGraph graph = graph(1, 0);
        graph.addConnection(0, 0, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void create_connectionAndNode_inputNode_outOfBounds()
    {
        NodeGraph graph = graph(1, 0);
        graph.addConnectionAndNode(5, 0, 5, 0);
    }

    @Test
    public void create_oneByte_largeValue()
    {
        NodeGraph graph = graph(1, 200);
        assertEquals(200, graph.getNodeType(0));
    }
}
