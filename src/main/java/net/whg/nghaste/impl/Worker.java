package net.whg.nghaste.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A worker class is a small implementation of the basic part of the algorithm
 * which serves the purpose of processing node graphs in a parallel manner by
 * pulling a node graph from the container, handling it, and passing all
 * children back into the container.
 */
class Worker extends Thread
{
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    private final NodeContainer container;
    private boolean running = true;

    /**
     * Creates a new worker object.
     * 
     * @param container
     *     - The container to pull node graphs out of and store new node graphs
     *     into.
     */
    Worker(NodeContainer container)
    {
        this.container = container;

        setDaemon(true);
        start();
    }

    /**
     * Disposes this worker and closes the thread. This function waits for the
     * thread to fully stop before returning.
     */
    void dispose()
    {
        running = false;

        try
        {
            join();
        }
        catch (InterruptedException e)
        {
            logger.error("Failed to stop worker thread!", e);
            Thread.currentThread()
                  .interrupt();
        }
    }

    @Override
    public void run()
    {
        SearchTree searchTree = new SearchTree();
        List<NodeGraph> graphs = searchTree.getOutputGraphs();
        List<NodeGraph> solutions = searchTree.getSolutions();

        while (running)
        {
            NodeGraph graph = container.getNodeGraph();

            if (graph == null)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread()
                          .interrupt();
                }

                continue;
            }

            searchTree.placeNeighbors(graph);
            container.finishGraph();

            for (NodeGraph g : graphs)
                container.addNodeGraph(g);
            graphs.clear();

            for (NodeGraph g : solutions)
                container.addSolution(g);
            solutions.clear();
        }
    }
}
