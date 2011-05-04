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
// 3 tuple evaluation safe if, Result is Float, 
// result = cond ? c: d;

public class OpCode3TIfFloat extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Float.class);  // result
        oprandTypeList.add(Boolean.class); // cond
        oprandTypeList.add(Float.class); // c
        oprandTypeList.add(Float.class); // d

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCode3TIfFloat.class.newInstance());
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
        Boolean cond = (Boolean) values.get(0);
        Float c = (Float) values.get(1);
        Float d = (Float) values.get(2);

        return (cond.booleanValue() ? c : d);
    }

}
