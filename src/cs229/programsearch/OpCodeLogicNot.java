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
public class OpCodeLogicNot extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Boolean.class);
        oprandTypeList.add(Boolean.class);

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCodeLogicNot.class.newInstance());
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Bug");
        }
    }

    public List<Class> getOperandType()
    {
        return oprandTypeList;
    }

    public Object eval(List<Object> values)
    {
        Boolean v1 = (Boolean) values.get(0);
        return !v1 ;
    }

    public String getOperandTypeStr()
    {
        return oprandTypeListStr;
    }
}
