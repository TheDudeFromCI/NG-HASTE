package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IDataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.IHeuristic;
import net.whg.nghaste.ISolutionAxiom;
import net.whg.nghaste.impl.Environment;
import net.whg.nghaste.impl.EnvironmentBuilder;
import net.whg.nghaste.impl.OutputFunction;
import net.whg.nghaste.util.EnvironmentUtils;

public class EnvironmentBuilderTest
{
    @Test
    public void buildEnv_hasInputAndOutputs_valid()
    {
        Environment env = new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                                  .build();

        assertTrue(env.getFunctions()
                      .contains(EnvironmentUtils.FUNC0_NUM_OUT));
        assertTrue(env.getFunctions()
                      .contains(EnvironmentUtils.FUNC2_NUM_CONST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildEnv_noOutput_invalid()
    {
        new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildEnv_noInput_invalid()
    {
        new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildEnv_2Outputs_invalid()
    {
        IFunction function = new OutputFunction(new IDataType[] {EnvironmentUtils.DATA_TEXT})
        {
            @Override
            public IDataInstance[] execute(IDataInstance[] inputs)
            {
                return null;
            }
        };

        new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                .addFunction(function)
                                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildEnv_functionNoPlugs_invalid()
    {
        IFunction function = mock(IFunction.class);
        when(function.getInputs()).thenReturn(new IDataType[0]);
        when(function.getOutputs()).thenReturn(new IDataType[0]);

        new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                .addFunction(function)
                                .build();
    }

    @Test(expected = NullPointerException.class)
    public void buildEnv_functionNullInputs_invalid()
    {
        IFunction function = mock(IFunction.class);
        when(function.getInputs()).thenReturn(null);
        when(function.getOutputs()).thenReturn(new IDataType[0]);

        new EnvironmentBuilder().addFunction(function);
    }

    @Test(expected = NullPointerException.class)
    public void buildEnv_functionNullOutputs_invalid()
    {
        IFunction function = mock(IFunction.class);
        when(function.getInputs()).thenReturn(new IDataType[0]);
        when(function.getOutputs()).thenReturn(null);

        new EnvironmentBuilder().addFunction(function);
    }

    @Test
    public void buildEnv_addAxiomsAndHeuristics()
    {
        IAxiom axiom = mock(IAxiom.class);
        ISolutionAxiom solutionAxiom = mock(ISolutionAxiom.class);
        IHeuristic heuristic = mock(IHeuristic.class);

        Environment env = new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                                  .addAxiom(axiom)
                                                  .addSolutionAxiom(solutionAxiom)
                                                  .addHeuristic(heuristic)
                                                  .build();

        assertTrue(env.getAxioms()
                      .contains(axiom));
        assertTrue(env.getSolutionAxioms()
                      .contains(solutionAxiom));
        assertTrue(env.getHeuristics()
                      .contains(heuristic));
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildEnv_negativeDepth()
    {
        new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                .setMaxDepth(-1)
                                .build();
    }

    @Test
    public void addNullAxiomsAndHeuristics()
    {
        Environment env = new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                                  .addFunction(null)
                                                  .addAxiom(null)
                                                  .addSolutionAxiom(null)
                                                  .addHeuristic(null)
                                                  .build();

        assertEquals(2, env.getFunctions()
                           .size());
        assertEquals(0, env.getAxioms()
                           .size());
        assertEquals(0, env.getSolutionAxioms()
                           .size());
        assertEquals(0, env.getHeuristics()
                           .size());
    }

    @Test
    public void addDuplicateAxiomsAndHeuristics()
    {
        IAxiom axiom = mock(IAxiom.class);
        ISolutionAxiom solutionAxiom = mock(ISolutionAxiom.class);
        IHeuristic heuristic = mock(IHeuristic.class);

        Environment env = new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                                  .addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addAxiom(axiom)
                                                  .addSolutionAxiom(solutionAxiom)
                                                  .addHeuristic(heuristic)
                                                  .addAxiom(axiom)
                                                  .addSolutionAxiom(solutionAxiom)
                                                  .addHeuristic(heuristic)
                                                  .build();

        assertEquals(2, env.getFunctions()
                           .size());
        assertEquals(1, env.getAxioms()
                           .size());
        assertEquals(1, env.getSolutionAxioms()
                           .size());
        assertEquals(1, env.getHeuristics()
                           .size());
    }

    @Test
    public void addFunction_requiredComponents()
    {
        IAxiom axiom1 = mock(IAxiom.class);

        IDataType dataType = mock(IDataType.class);
        when(dataType.getRequiredAxioms()).thenReturn(new IAxiom[] {axiom1});
        when(dataType.getRequiredSolutionAxioms()).thenReturn(new ISolutionAxiom[0]);
        when(dataType.getRequiredHeuristics()).thenReturn(new IHeuristic[0]);

        IAxiom axiom2 = mock(IAxiom.class);
        ISolutionAxiom solutionAxiom = mock(ISolutionAxiom.class);
        IHeuristic heuristic = mock(IHeuristic.class);

        IFunction function = mock(IFunction.class);
        when(function.getInputs()).thenReturn(new IDataType[] {dataType});
        when(function.getOutputs()).thenReturn(new IDataType[] {EnvironmentUtils.DATA_NUMBER});
        when(function.getRequiredAxioms()).thenReturn(new IAxiom[] {axiom2});
        when(function.getRequiredSolutionAxioms()).thenReturn(new ISolutionAxiom[] {solutionAxiom});
        when(function.getRequiredHeuristics()).thenReturn(new IHeuristic[] {heuristic});

        Environment env = new EnvironmentBuilder().addFunction(EnvironmentUtils.FUNC2_NUM_CONST)
                                                  .addFunction(EnvironmentUtils.FUNC0_NUM_OUT)
                                                  .addFunction(function)
                                                  .build();

        assertEquals(3, env.getFunctions()
                           .size());
        assertEquals(2, env.getAxioms()
                           .size());
        assertEquals(1, env.getSolutionAxioms()
                           .size());
        assertEquals(1, env.getHeuristics()
                           .size());
    }
}
