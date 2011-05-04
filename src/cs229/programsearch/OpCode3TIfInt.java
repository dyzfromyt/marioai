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
public class OpCode3TIfInt extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Integer.class);  // result
        oprandTypeList.add(Boolean.class); // cond
        oprandTypeList.add(Integer.class); // c
        oprandTypeList.add(Integer.class); // d

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCode3TIfInt.class.newInstance());
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
        Integer c = (Integer) values.get(1);
        Integer d = (Integer) values.get(2);

        return (cond.booleanValue() ? c : d);
    }
}
