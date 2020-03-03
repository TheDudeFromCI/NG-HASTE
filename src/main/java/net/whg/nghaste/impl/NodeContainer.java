package net.whg.nghaste.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The node container object is responsible for storing a list of NodeGraphs
 * which are ready to be processed. This container also stores node graph
 * solutions which are waiting to be collected. This class is thread safe.
 */
public class NodeContainer
{
    private final PriorityBlockingQueue<NodeGraph> queue = new PriorityBlockingQueue<>();
    private final List<NodeGraph> solutions = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger totalGraphs = new AtomicInteger(0);
    private final AtomicInteger unprocessedGraphs = new AtomicInteger(0);

    /**
     * Adds a new NodeGraph to this node container queue. The node graph is assumed
     * to have a heuristic value assigned, and no longer edited after this point.
     * 
     * @param graph
     *     - The graph to add.
     */
    public void addNodeGraph(NodeGraph graph)
    {
        queue.add(graph);
        unprocessedGraphs.incrementAndGet();
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
        NodeGraph graph = queue.poll();

        if (graph != null)
            totalGraphs.incrementAndGet();

        return graph;
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
            if (queue.remove(graph))
                queue.add(graph);
        }
    }

    /**
     * Gets the number of node graphs currently within this container queue. This
     * does not count the number of solutions.
     * 
     * @return The number of non-solution node graphs in this container.
     */
    public int size()
    {
        return queue.size();
    }

    /**
     * Gets the number of solutions currently within this container.
     * 
     * @return The number of solutions.
     */
    public int getSolutionCount()
    {
        return solutions.size();
    }

    /**
     * Gets the solution at the specified index of this container.
     * 
     * @param index
     *     - The index of the solution.
     * @return The node graph solution.
     */
    public NodeGraph getSolution(int index)
    {
        return solutions.get(index);
    }

    /**
     * Removes a given solution from this container. Does nothing if solution is
     * null.
     * 
     * @param solution
     *     - The solution to remove.
     */
    public void removeSolution(NodeGraph solution)
    {
        if (solution == null)
            return;

        solutions.remove(solution);
    }

    /**
     * Adds a new solution to this node container.
     * 
     * @param solution
     *     - The solution to add.
     */
    public void addSolution(NodeGraph solution)
    {
        solutions.add(solution);
    }

    /**
     * This function returns the total number of graphs which have been searched, or
     * are in the middle of being searched. This value is incremented by one each
     * time a graph is pulled from the queue for handling. The value does not change
     * when {@link #getNodeGraph()} returns null.
     * 
     * @return The total number of graphs which have been searched.
     */
    public int getTotalGraphsSearched()
    {
        return totalGraphs.get();
    }

    /**
     * Gets the current number of active solutions which have not yet finished being
     * processed. This value is changed constantly as more of the search space is
     * explored. This value is calculated by adding the number of unprocessed graphs
     * within the node container with the number of workers currently in the middle
     * of processing a solution. When this value is equal to 0, the entire search
     * space has been explored.
     * 
     * @return The number of unprocessed solutions which exist in the current
     *     instant.
     */
    public int getRemainingGraphs()
    {
        return unprocessedGraphs.get();
    }

    /**
     * This method should be called by the worker threads after finishing processing
     * on a graph. This method is used for keeping an up to date count on
     * unprocessed graphs.
     */
    void finishGraph()
    {
        unprocessedGraphs.decrementAndGet();
    }
}
