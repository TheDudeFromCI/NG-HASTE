package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.EnvironmentBuilder;
import net.whg.nghaste.IAxiom;
import net.whg.nghaste.IHeuristic;
import net.whg.nghaste.ISolutionAxiom;
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
                                                  .addAxiom(null)
                                                  .addSolutionAxiom(null)
                                                  .addHeuristic(null)
                                                  .build();

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
                                                  .addAxiom(axiom)
                                                  .addSolutionAxiom(solutionAxiom)
                                                  .addHeuristic(heuristic)
                                                  .addAxiom(axiom)
                                                  .addSolutionAxiom(solutionAxiom)
                                                  .addHeuristic(heuristic)
                                                  .build();

        assertEquals(1, env.getAxioms()
                           .size());
        assertEquals(1, env.getSolutionAxioms()
                           .size());
        assertEquals(1, env.getHeuristics()
                           .size());
    }
}
