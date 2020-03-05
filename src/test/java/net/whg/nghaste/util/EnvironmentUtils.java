package net.whg.nghaste.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.whg.nghaste.DataInstance;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;
import net.whg.nghaste.impl.Environment;
import net.whg.nghaste.impl.EnvironmentBuilder;
import net.whg.nghaste.impl.InputFunction;
import net.whg.nghaste.impl.OutputFunction;

public class EnvironmentUtils
{
    public static final IDataType DATA_NUMBER = data();
    public static final IDataType DATA_TEXT = data();
    public static final IDataType DATA_BOOL = data();

    public static final IFunction FUNC0_NUM_OUT = outputFunc(new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC1_ADD =
            func(new IDataType[] {DATA_NUMBER, DATA_NUMBER}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC2_NUM_CONST = inputFunc(new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC3_TEXT_CONST = inputFunc(new IDataType[] {DATA_TEXT});
    public static final IFunction FUNC4_BOOL_CONST = inputFunc(new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC5_BOOL_TO_INT = func(new IDataType[] {DATA_BOOL}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC6_INT_TO_TEXT = func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_TEXT});
    public static final IFunction FUNC7_TEXT_TO_INT = func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC8_TEXT_TO_BOOL = func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC9_INT_TO_BOOL = func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_BOOL});
    public static final IFunction FUNC10_STRING_LENGTH =
            func(new IDataType[] {DATA_TEXT}, new IDataType[] {DATA_NUMBER});
    public static final IFunction FUNC11_GET_FACTORS =
            func(new IDataType[] {DATA_NUMBER}, new IDataType[] {DATA_NUMBER, DATA_NUMBER, DATA_NUMBER});
    public static final IFunction FUNC12_CONCAT =
            func(new IDataType[] {DATA_TEXT, DATA_TEXT}, new IDataType[] {DATA_TEXT});

    private static IDataType data()
    {
        return new IDataType()
        {
            @Override
            public boolean canConnectTo(IDataType dataType)
            {
                return dataType == this;
            }
        };
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

            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }

    private static IFunction inputFunc(IDataType[] outputs)
    {
        return new InputFunction(outputs)
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
    }

    private static IFunction outputFunc(IDataType[] inputs)
    {
        return new OutputFunction(inputs)
        {
            @Override
            public DataInstance[] execute(DataInstance[] inputs)
            {
                return null;
            }
        };
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

        functions.add(FUNC0_NUM_OUT);
        functions.add(FUNC1_ADD);
        functions.add(FUNC2_NUM_CONST);
        functions.add(FUNC3_TEXT_CONST);
        functions.add(FUNC4_BOOL_CONST);
        functions.add(FUNC5_BOOL_TO_INT);
        functions.add(FUNC6_INT_TO_TEXT);
        functions.add(FUNC7_TEXT_TO_INT);
        functions.add(FUNC8_TEXT_TO_BOOL);
        functions.add(FUNC9_INT_TO_BOOL);
        functions.add(FUNC10_STRING_LENGTH);
        functions.add(FUNC11_GET_FACTORS);
        functions.add(FUNC12_CONCAT);

        return functions;
    }
}
