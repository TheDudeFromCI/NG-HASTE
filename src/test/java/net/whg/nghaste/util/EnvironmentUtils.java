package net.whg.nghaste.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import net.whg.nghaste.Environment;
import net.whg.nghaste.IDataType;
import net.whg.nghaste.IFunction;

public class EnvironmentUtils
{
    public static Environment quickEnvironment()
    {
        return quickEnvironment(5);
    }

    public static Environment quickEnvironment(int size)
    {
        List<IFunction> functions = buildFunctionList();
        return new Environment(functions, functions.get(0), size);
    }

    public static List<IFunction> buildFunctionList()
    {
        List<IFunction> functions = new ArrayList<>();

        IDataType number = mock(IDataType.class, "type_number");
        IDataType text = mock(IDataType.class, "type_text");
        IDataType bool = mock(IDataType.class, "type_bool");

        IFunction numOut = mock(IFunction.class, "func_numOut");
        when(numOut.getInputs()).thenReturn(new IDataType[] {number});
        when(numOut.getOutputs()).thenReturn(new IDataType[] {});
        functions.add(numOut);

        IFunction add = mock(IFunction.class, "func_add");
        when(add.getInputs()).thenReturn(new IDataType[] {number, number});
        when(add.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(add);

        IFunction numConst = mock(IFunction.class, "func_numConst");
        when(numConst.getInputs()).thenReturn(new IDataType[] {});
        when(numConst.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(numConst);

        IFunction textConst = mock(IFunction.class, "func_textConst");
        when(textConst.getInputs()).thenReturn(new IDataType[] {});
        when(textConst.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(textConst);

        IFunction boolConst = mock(IFunction.class, "func_boolConst");
        when(boolConst.getInputs()).thenReturn(new IDataType[] {});
        when(boolConst.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(boolConst);

        IFunction boolToInt = mock(IFunction.class, "func_boolToInt");
        when(boolToInt.getInputs()).thenReturn(new IDataType[] {bool});
        when(boolToInt.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(boolToInt);

        IFunction intToText = mock(IFunction.class, "func_intToText");
        when(intToText.getInputs()).thenReturn(new IDataType[] {number});
        when(intToText.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(intToText);

        IFunction textToInt = mock(IFunction.class, "func_textToInt");
        when(textToInt.getInputs()).thenReturn(new IDataType[] {text});
        when(textToInt.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(textToInt);

        IFunction textToBool = mock(IFunction.class, "func_textToBool");
        when(textToBool.getInputs()).thenReturn(new IDataType[] {text});
        when(textToBool.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(textToBool);

        IFunction intToBool = mock(IFunction.class, "func_intToBool");
        when(intToBool.getInputs()).thenReturn(new IDataType[] {number});
        when(intToBool.getOutputs()).thenReturn(new IDataType[] {bool});
        functions.add(intToBool);

        IFunction stringLength = mock(IFunction.class, "func_stringLength");
        when(stringLength.getInputs()).thenReturn(new IDataType[] {text});
        when(stringLength.getOutputs()).thenReturn(new IDataType[] {number});
        functions.add(stringLength);

        IFunction getFactors = mock(IFunction.class, "func_getFactors");
        when(getFactors.getInputs()).thenReturn(new IDataType[] {number});
        when(getFactors.getOutputs()).thenReturn(new IDataType[] {number, number, number});
        functions.add(getFactors);

        IFunction concat = mock(IFunction.class, "func_concat");
        when(concat.getInputs()).thenReturn(new IDataType[] {text, text});
        when(concat.getOutputs()).thenReturn(new IDataType[] {text});
        functions.add(concat);

        return functions;
    }
}
