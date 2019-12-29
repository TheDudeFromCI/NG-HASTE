package net.whg.nghaste;

import java.util.Arrays;

/**
 * A graph hash is a simple wrapper around a byte array with some overrides of
 * the equal and hashcode methods for simple comparisons in maps. A graph hash
 * represents a unquie compressed version of a graph in which two graphs which
 * share the same structure, regardless of the order nodes and connections were
 * added, have the same graph hash. This object is generated as the output of
 * the graph hasher class. Graphs with a similar structure, but not equal, are
 * not promised to have a similar hash.
 * <p>
 * This class is immutable.
 */
public class GraphHash
{
    private final byte[] data;

    /**
     * Creates a new graph hash object.
     * 
     * @param data
     *     - The byte array representing the data.
     */
    public GraphHash(byte[] data)
    {
        this.data = data;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof GraphHash))
            return false;

        GraphHash hash = (GraphHash) o;
        return Arrays.equals(data, hash.data);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(data);
    }
}
