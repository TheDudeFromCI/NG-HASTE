package net.whg.nghaste.integration;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.NGHasteAlgorithm;
import net.whg.nghaste.NodeGraph;
import net.whg.nghaste.util.EnvironmentUtils;

public class Algorithm1Test
{
    @Test
    public void test()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment environment = new Environment(functions, functions.get(0), 5);
        NGHasteAlgorithm algorithm = new NGHasteAlgorithm(environment);

        algorithm.startWorkers(1);
        await().atMost(8, TimeUnit.SECONDS)
               .until(() -> algorithm.getSolutionCount() >= 10);
        algorithm.disposeWorkers();

        int solCount = algorithm.getSolutionCount();
        for (int i = 0; i < solCount; i++)
        {
            NodeGraph solution = algorithm.getSolution(i);
            assertEquals(0, solution.countOpenPlugs());
        }
    }

    @Test
    public void test_multithreaded()
    {
        for (int i = 0; i < 25; i++)
        {
            List<IFunction> functions = EnvironmentUtils.buildFunctionList();
            Environment environment = new Environment(functions, functions.get(0), 3);
            NGHasteAlgorithm algorithm = new NGHasteAlgorithm(environment);

            algorithm.startWorkers(3);
            await().atMost(13, TimeUnit.SECONDS)
                   .until(() -> algorithm.getRemainingGraphs() == 0);
            algorithm.disposeWorkers();

            assertEquals(31, algorithm.getSolutionCount());
        }
    }
}
