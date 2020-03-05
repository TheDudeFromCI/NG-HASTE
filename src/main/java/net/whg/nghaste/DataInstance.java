package net.whg.nghaste;

/**
 * A data instance is a specific instance output of a function. As all data
 * handled by the functions within the algorithm are designed under the premise
 * of being value based only, data instances are immutable objects which
 * represent the output of each function.
 */
public class DataInstance
{
    private final IDataType dataType;
    private final Object value;

    public DataInstance(IDataType dataType, Object value)
    {
        this.dataType = dataType;
        this.value = value;
    }

    /**
     * Gets the data type of this data instance.
     * 
     * @return The data type this instance belongs to.
     */
    public IDataType getType()
    {
        return dataType;
    }

    /**
     * Gets the value of this data instance.
     * 
     * @return The value.
     */
    public Object getValue()
    {
        return value;
    }
}
