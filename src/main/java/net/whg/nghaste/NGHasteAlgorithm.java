package net.whg.nghaste;

import java.util.ArrayList;
import java.util.List;

/**
 * The NG-HASTE algorithm is a logic-based probleming solving algorithm. The
 * algorithm is designed to solve problems in a logical way by generating code
 * at runtime which is capable of preforming a task, such as solving a specific
 * instance of a problem. The algorithm is built heavily around the idea of
 * preformaing on a massive search tree, pruning and taking shortcuts where
 * possible to find a solution, or better solutions.
 * <p>
 * This class is an implementation of that algorithm.
 */
public class NGHasteAlgorithm
{
    private final NodeContainer container = new NodeContainer();
    private final List<Worker> workers = new ArrayList<>();
    private final SearchTree searchTree;
    private final Environment environment;

    /**
     * Creates a new instance of the NG-HASTE algorithm, and initializes it.
     * 
     * @param environment
     *     - The environment to build this instance with. This is used to determine
     *     how the algorithm should solve.
     */
    public NGHasteAlgorithm(Environment environment)
    {
        this.environment = environment;
        searchTree = new SearchTree(environment);

        initialize();
    }

    /**
     * Initializes the algorithm by adding a new node graph containing only the
     * inital output function and adds it to the node container.
     */
    private void initialize()
    {
        int nodeType = environment.getIndexOf(environment.getOutputFunction());
        int numSize = environment.getMinByteCount();
        NodeGraph graph = NodeGraph.newGraph(numSize, nodeType);
        container.addNodeGraph(graph);
    }

    /**
     * Gets the number of workers currently active.
     * 
     * @return The number of active workers for this algorithm.
     */
    public int getWorkerCount()
    {
        return workers.size();
    }

    /**
     * Starts a new set of workers for this algorithm. If there are workers which
     * are already active, this function starts adds the new workers to the current
     * list. It is recommended to have at most one worker per logical core on the
     * current machine.
     * 
     * @param workers
     *     - The number of new workers to start.
     */
    public void startWorkers(int workers)
    {
        for (int i = 0; i < workers; i++)
            this.workers.add(new Worker(container, searchTree));
    }

    /**
     * Disposes all currently active workers and waits for all threads to finish.
     */
    public void disposeWorkers()
    {
        for (Worker worker : workers)
            worker.dispose();

        workers.clear();
    }

    /**
     * Gets the environment this algorithm instance is currently operating within.
     * 
     * @return The environment.
     */
    public Environment getEnvironment()
    {
        return environment;
    }

    /**
     * Gets the number of solutions currently discovered.
     * 
     * @return The number of discovered solutions.
     */
    public int getSolutionCount()
    {
        return container.getSolutionCount();
    }

    /**
     * Gets the solution at the specified index within this algorithm instance.
     * 
     * @param index
     *     - The index of the solution.
     * @return The node graph solution.
     */
    public NodeGraph getSolution(int index)
    {
        return container.getSolution(index);
    }

    /**
     * Removes a solution from this algorithm instance. Graphs which are removed
     * from the solution list are no longer capable of being rediscovered.
     * 
     * @param graph
     *     - The graph to remove.
     */
    public void removeSolution(NodeGraph graph)
    {
        container.removeSolution(graph);
    }
}
