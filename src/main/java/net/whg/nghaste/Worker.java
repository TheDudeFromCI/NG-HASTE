package net.whg.nghaste;

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
    private final SearchTree searchTree;
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
        this.searchTree = new SearchTree(container);

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
        }
    }
}
