/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;


import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
// type cast from integer to float
public class OpCodeTypeCastIntFloat extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;
    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Float.class); // result is float
        oprandTypeList.add(Integer.class);

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCodeTypeCastIntFloat.class.newInstance());
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Bug");
        }

    }
    public String getOperandTypeStr()
    {
        return oprandTypeListStr;
    }
    public List<Class> getOperandType()
    {
        return oprandTypeList;
    }

    public Object eval(List<Object> values)
    {
        Integer v1 = (Integer) values.get(0);
        return v1.floatValue();
    }
}
