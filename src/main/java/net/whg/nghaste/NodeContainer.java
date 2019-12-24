package net.whg.nghaste;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * The node container object is responsible for storing a list of NodeGraphs
 * which are ready to be processed. This class is thread safe.
 */
public class NodeContainer
{
    private final PriorityBlockingQueue<NodeGraph> queue = new PriorityBlockingQueue<>();

    /**
     * Adds a new NodeGraph to this node container. The node graph is assumed to
     * have a heuristic value assigned, and no longer edited after this point.
     * 
     * @param graph
     *     - The graph to add.
     */
    public void addNodeGraph(NodeGraph graph)
    {
        queue.add(graph);
    }

    /**
     * Gets the next node graph in the queue. This is the graph with the highest
     * heuristic value.
     * 
     * @return The node graph in this container with the highest heuristic value, or
     *     null if there are no node graphs left in this container.
     */
    public NodeGraph getNodeGraph()
    {
        return queue.poll();
    }

    /**
     * This function will sort all Node Graphs within this container. This method
     * should be called if the heuristics of NodeGraphs have changed. This method is
     * extremely slow, operating in O(n * log n) time and should be called
     * sparingly.
     * 
     * @see {@link #update(NodeGraph)}
     */
    public void sort()
    {
        synchronized (queue)
        {
            Object[] array = queue.toArray();
            queue.clear();

            for (Object o : array)
                queue.add((NodeGraph) o);
        }
    }

    /**
     * This function updates the location of a single Node Graph within the queue.
     * This method should be called if the heuristics of a graph has changed after
     * being added to this container.<br>
     * <br>
     * If a vast majority of the graphs within this container need to be updated, it
     * might be much more efficient to call {@link #sort()} on this container.
     * 
     * @param graph
     *     - The graph to update.
     */
    public void update(NodeGraph graph)
    {
        synchronized (queue)
        {
            if (queue.contains(graph))
            {
                queue.remove(graph);
                queue.add(graph);
            }
        }
    }

    /**
     * Gets the number of node graphs currently within this container.
     * 
     * @return The number of node graphs in this container.
     */
    public int size()
    {
        return queue.size();
    }
}
