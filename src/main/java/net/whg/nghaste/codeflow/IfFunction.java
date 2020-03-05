package net.whg.nghaste.codeflow;

import java.util.ArrayList;
import java.util.List;
import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;

/**
 * An if function is used to allow a specific value to be returned value based
 * on an input boolean value.
 */
public class IfFunction implements IFunction
{
    /**
     * Generates a list of if functions based on a given object heirarchy. A new if
     * statement function is generated for each object in the heirarchy.
     * 
     * @param obj
     *     - The heirarchy.
     * @return A list of if statements to add to the environment.
     */
    public static List<IfFunction> generateFunctions(ObjectHeirarchy obj)
    {
        List<IfFunction> functions = new ArrayList<>();

        DataObj bool = obj.getObject("Boolean");
        for (DataObj data : obj)
            functions.add(new IfFunction(bool, data));

        return functions;
    }

    private final IDataType[] inputs;
    private final IDataType[] outputs;

    private IfFunction(DataObj bool, DataObj dataObj)
    {
        inputs = new IDataType[] {bool, dataObj, dataObj};
        outputs = new IDataType[] {dataObj};
    }

    @Override
    public IDataType[] getInputs()
    {
        return inputs;
    }

    @Override
    public IDataType[] getOutputs()
    {
        return outputs;
    }

    @Override
    public DataInstance[] execute(DataInstance[] inputs)
    {
        boolean b = (boolean) inputs[0].getValue();

        IDataType dataType = inputs[1].getType();
        Object value = (b ? inputs[1] : inputs[2]).getValue();

        DataInstance[] out = new DataInstance[1];
        out[0] = new DataInstance(dataType, value);

        return out;
    }
}
