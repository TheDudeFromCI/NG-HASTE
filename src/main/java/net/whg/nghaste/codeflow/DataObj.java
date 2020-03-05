package net.whg.nghaste.codeflow;

import net.whg.nghaste.IDataType;

/**
 * This class is a container which represents a data type which was created
 * dynamically. It holds all of the properties of a basic data type and supports
 * automatic casting up.
 */
public class DataObj implements IDataType
{
    private final String name;
    private final DataObj[] parents;

    /**
     * Creates a new data object instance.
     * 
     * @param name
     *     - The name of this data object.
     * @param parents
     *     - The parents this data object extend from.
     */
    DataObj(String name, DataObj[] parents)
    {
        this.name = name;
        this.parents = parents;
    }

    @Override
    public boolean canConnectTo(IDataType dataType)
    {
        if (dataType == this)
            return true;

        for (DataObj obj : parents)
            if (obj != null && obj.canConnectTo(dataType))
                return true;

        return false;
    }

    /**
     * Gets the name of this data object.
     * 
     * @return The name.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return String.format("DataObj(%s)", name);
    }

    /**
     * Gets the number of parent types this object has.
     * 
     * @return The number of direct parent types.
     */
    public int getParentCount()
    {
        return parents.length;
    }

    /**
     * Gets the parent of this object type at the specified index.
     * 
     * @param index
     *     - The index of the parent to get.
     * @return The direct parent of this object at the given index.
     */
    public DataObj getParent(int index)
    {
        return parents[index];
    }
}
