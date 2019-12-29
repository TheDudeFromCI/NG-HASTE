package net.whg.nghaste.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.whg.nghaste.Environment;
import net.whg.nghaste.EnvironmentBuilder;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.InputFunction;
import net.whg.nghaste.OutputFunction;

public class EnvironmentUtils
{
    public static final IDataType DATA_NUMBER = data();
    public static final IDataType DATA_TEXT = data();
    public static final IDataType DATA_BOOL = data();

    public static final IFunction FUNC_NUM_OUT = outputFunc(new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_ADD =
            func(new IDataType[] {DATA_NUMBER, DATA_NUMBER}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_NUM_CONST = inputFunc(new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_TEXT_CONST = inputFunc(new IDataType[] {DATA_TEXT});
    public static final IFunction FUNC_BOOL_CONST = inputFunc(new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC_BOOL_TO_INT = func(new IDataType[] {DATA_BOOL}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_INT_TO_TEXT = func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_TEXT});
    public static final IFunction FUNC_TEXT_TO_INT = func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_TEXT_TO_BOOL = func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC_INT_TO_BOOL = func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC_STRING_LENGTH = func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC_GET_FACTORS =
            func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_NUMBER, DATA_NUMBER, DATA_NUMBER});
    public static final IFunction FUNC_CONCAT =
            func(new IDataType[] {DATA_TEXT, DATA_TEXT}, new IDataType[] {DATA_TEXT});

    private static IDataType data()
    {
        return new IDataType()
        {};
    }

    private static IFunction func(IDataType[] inputs, IDataType[] outputs)
    {
        return new IFunction()
        {
            @Override
            public IDataType[] getOutputs()
            {
                return outputs;
            }

            @Override
            public IDataType[] getInputs()
            {
                return inputs;
            }
        };
    }

    private static IFunction inputFunc(IDataType[] outputs)
    {
        return new InputFunction(outputs)
        {};
    }

    private static IFunction outputFunc(IDataType[] inputs)
    {
        return new OutputFunction(inputs)
        {};
    }

    public static Environment quickEnvironment()
    {
        return quickEnvironment(5);
    }

    public static Environment quickEnvironment(int size)
    {
        return quickEnvironment(size, builder ->
        {});
    }

    public static Environment quickEnvironment(int size, Consumer<EnvironmentBuilder> additonalActions)
    {
        List<IFunction> functions = buildFunctionList();

        EnvironmentBuilder builder = new EnvironmentBuilder().setMaxDepth(size);
        for (IFunction function : functions)
            builder.addFunction(function);

        additonalActions.accept(builder);
        return builder.build();
    }

    public static List<IFunction> buildFunctionList()
    {
        List<IFunction> functions = new ArrayList<>();

        functions.add(FUNC_NUM_OUT);
        functions.add(FUNC_ADD);
        functions.add(FUNC_NUM_CONST);
        functions.add(FUNC_TEXT_CONST);
        functions.add(FUNC_BOOL_CONST);
        functions.add(FUNC_BOOL_TO_INT);
        functions.add(FUNC_INT_TO_TEXT);
        functions.add(FUNC_TEXT_TO_INT);
        functions.add(FUNC_TEXT_TO_BOOL);
        functions.add(FUNC_INT_TO_BOOL);
        functions.add(FUNC_STRING_LENGTH);
        functions.add(FUNC_GET_FACTORS);
        functions.add(FUNC_CONCAT);

        return functions;
    }
}
