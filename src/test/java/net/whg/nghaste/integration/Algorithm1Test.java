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
    @Test(timeout = 10000)
    public void test()
    {
        List<IFunction> functions = EnvironmentUtils.buildFunctionList();
        Environment environment = new Environment(functions, functions.get(0), 5);
        NGHasteAlgorithm algorithm = new NGHasteAlgorithm(environment);

        algorithm.startWorkers(2);
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
}
