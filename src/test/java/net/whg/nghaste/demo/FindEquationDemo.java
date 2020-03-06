package net.whg.nghaste.demo;

import net.whg.nghaste.codeflow.ObjectHeirarchy;
import net.whg.nghaste.codeflow.SimpleInput;
import net.whg.nghaste.codeflow.SimpleOutput;
import net.whg.nghaste.impl.EnvironmentBuilder;
import net.whg.nghaste.impl.NGHasteAlgorithm;
import net.whg.nghaste.impl.NodeGraph;
import net.whg.nghaste.libs.math.AddIntFunction;

public class FindEquationDemo
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Starting NG-HASTE.");

        ObjectHeirarchy heirarchy = new ObjectHeirarchy();

        EnvironmentBuilder env = new EnvironmentBuilder();
        env.addFunction(new SimpleInput(heirarchy.getObject("Integer"), 1))
           .addFunction(new SimpleInput(heirarchy.getObject("Integer"), 2))
           .addFunction(new SimpleInput(heirarchy.getObject("Integer"), 3))
           .addFunction(new SimpleOutput(heirarchy.getObject("Integer")))
           .addFunction(new AddIntFunction(heirarchy))
           .setMaxDepth(10);

        NGHasteAlgorithm nghaste = new NGHasteAlgorithm(env.build());
        nghaste.startWorkers(8);

        while (nghaste.getRemainingGraphs() > 0)
        {
            Thread.sleep(1000);

            System.out.println(nghaste.getRemainingGraphs() + " => " + nghaste.getSolutionCount());
        }

        nghaste.disposeWorkers();

        int solCount = nghaste.getSolutionCount();
        System.out.println("Done. " + solCount + " solutions.");

        for (int i = 0; i < solCount; i++)
        {
            NodeGraph graph = nghaste.getSolution(i);
            System.out.println(graph);
            Thread.sleep(100);
        }
    }
}
