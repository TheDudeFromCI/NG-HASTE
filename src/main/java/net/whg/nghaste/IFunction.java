package net.whg.nghaste;

/**
 * A function is a node type in a node graph. It takes a set of input data types
 * and a set of output data types to define the implementation within the
 * NG-HASTE algorithm. It is also called periodically to execute a set set of
 * defined inputs to calculate the outputs. All implementations of this
 * interface must follow the property in which it contains a static internal
 * state and that the same inputs will always produce the same outputs.<br>
 * <br>
 * Exceptions to the internal state rule exist for input functions which take
 * their value from the lookup table when digging through the search tree, and
 * for output functions, which preform no action and solely exist to validate
 * the output condition for the algorithm graph.<br>
 * <br>
 * This function may be called continuously after the algorithm is finished for
 * execution of the output algorithm through the rest of the application.
 */
public interface IFunction
{
    /**
     * Gets the input types for this function.
     * 
     * @return An array of inputs used when this function
     */
    IDataType[] getInputs();

    /**
     * Gets the output types for this function.
     * 
     * @return An array of outputs used when this function
     */
    IDataType[] getOutputs();
}
