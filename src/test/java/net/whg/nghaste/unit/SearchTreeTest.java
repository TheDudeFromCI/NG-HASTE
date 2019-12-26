package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.NodeContainer;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.SearchTree;

public class SearchTreeTest
{
    private List<IFunction> buildFunctionList()
    {
        List<IFunction> functions = new ArrayList<>();

        IDataType number = mock(IDataType.class);
        IDataType text = mock(IDataType.class);
        IDataType bool = mock(IDataType.class);

        IFunction numOut = mock(IFunction.class);
        when(numOut.getInputs()).thenReturn(new IDataType[] {number});
        when(numOut.getOutputs()).thenReturn(new IDataType[] {});
        functions.add(numOut);

        IFunction add = mock(IFunction.class);
        when(add.getInputs()).thenReturn(new IDataType[] {number, number});
        when(add.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(add);

        IFunction numConst = mock(IFunction.class);
        when(numConst.getInputs()).thenReturn(new IDataType[] {});
        when(numConst.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(numConst);

        IFunction textConst = mock(IFunction.class);
        when(textConst.getInputs()).thenReturn(new IDataType[] {});
        when(textConst.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(textConst);

        IFunction boolConst = mock(IFunction.class);
        when(boolConst.getInputs()).thenReturn(new IDataType[] {});
        when(boolConst.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(boolConst);

        IFunction boolToInt = mock(IFunction.class);
        when(boolToInt.getInputs()).thenReturn(new IDataType[] {bool});
        when(boolToInt.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(boolToInt);

        IFunction intToText = mock(IFunction.class);
        when(intToText.getInputs()).thenReturn(new IDataType[] {number});
        when(intToText.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(intToText);

        IFunction textToInt = mock(IFunction.class);
        when(textToInt.getInputs()).thenReturn(new IDataType[] {text});
        when(textToInt.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(textToInt);

        IFunction textToBool = mock(IFunction.class);
        when(textToBool.getInputs()).thenReturn(new IDataType[] {text});
        when(textToBool.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(textToBool);

        IFunction intToBool = mock(IFunction.class);
        when(intToBool.getInputs()).thenReturn(new IDataType[] {number});
        when(intToBool.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(intToBool);

        IFunction stringLength = mock(IFunction.class);
        when(stringLength.getInputs()).thenReturn(new IDataType[] {text});
        when(stringLength.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(stringLength);

        IFunction getFactors = mock(IFunction.class);
        when(getFactors.getInputs()).thenReturn(new IDataType[] {number});
        when(getFactors.getOutputs()).thenReturn(new IDataType[] {number, number, number});
        functions.add(getFactors);

        IFunction concat = mock(IFunction.class);
        when(concat.getInputs()).thenReturn(new IDataType[] {text, text});
        when(concat.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(concat);

        return functions;
    }

    @Test
    public void search1_onlyOutput()
    {
        List<IFunction> functions = buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        tree.placeNeighbors(NodeGraph.newGraph(env, 0));

        assertEquals(7, container.size());
        assertEquals(1, container.getSolutionCount());

        for (int i = 0; i < 7; i++)
        {
            NodeGraph g = container.getNodeGraph();
            assertEquals(2, g.getNodeCount());
            assertEquals(1, g.getConnectionCount());
            assertEquals(0, g.getNodeType(0));
            assertNotEquals(0, g.getNodeType(1));
        }
    }

    @Test
    public void search2_2Layers()
    {
        List<IFunction> functions = buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 5);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        tree.placeNeighbors(NodeGraph.newGraph(env, 0));

        List<NodeGraph> outputs = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            outputs.add(container.getNodeGraph());

        for (int i = 0; i < 7; i++)
            tree.placeNeighbors(outputs.get(i));

        assertEquals(43, container.size());
    }

    @Test(timeout = 3000)
    public void maxDepth()
    {
        List<IFunction> functions = buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 3);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        container.addNodeGraph(NodeGraph.newGraph(env, 0));
        while (container.size() > 0)
        {
            NodeGraph g = container.getNodeGraph();
            assertTrue(g.getConnectionCount() + g.countOpenPlugs() <= 3);

            tree.placeNeighbors(g);
        }
    }

    @Test(timeout = 3000)
    public void publishSolution()
    {
        List<IFunction> functions = buildFunctionList();
        Environment env = new Environment(functions, functions.get(0), 3);
        NodeContainer container = new NodeContainer();
        SearchTree tree = new SearchTree(container);

        container.addNodeGraph(NodeGraph.newGraph(env, 0));
        while (container.size() > 0)
        {
            NodeGraph g = container.getNodeGraph();
            tree.placeNeighbors(g);
        }

        int solCount = container.getSolutionCount();
        assertTrue(solCount > 0);

        for (int i = 0; i < solCount; i++)
            assertEquals(0, container.getSolution(i)
                                     .countOpenPlugs());
    }
}
