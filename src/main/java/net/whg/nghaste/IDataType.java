package net.whg.nghaste;

/**
 * A data type represents a type of data which can be passed from function to
 * function when constructing the graph. This class is not the value of
 * information being passed around through the graph but simply a blueprint for
 * assigning what form the data is in.
 */
public interface IDataType extends IRuleHolder
{
    /**
     * Checks if this data type is capable of connecting to given data type or not.
     * This function is called on output plugs to determine if they are capable of
     * connecting to a given input plug.
     * <p>
     * In most cases, this method should return true for instances of this same data
     * type.
     * 
     * @param dataType
     *     - The data type to check against.
     * @return True if a connection can be made from an output plug with this data
     *     type to an input plug with the provided data type. False otherwise.
     */
    boolean canConnectTo(IDataType dataType);
}
