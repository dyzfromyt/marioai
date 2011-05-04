/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;


import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class OpCode4TIfFloatFloat extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Float.class);  // result
        oprandTypeList.add(Float.class); // a
        oprandTypeList.add(Float.class); // b
        oprandTypeList.add(Float.class); // c
        oprandTypeList.add(Float.class); // d

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCode4TIfFloatFloat.class.newInstance());
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
        Float a = (Float) values.get(0);
        Float b = (Float) values.get(1);
        Float c = (Float) values.get(2);
        Float d = (Float) values.get(3);

        return (a > b ? c : d);
    }
}
