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
public class OpCodeGreatThan extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Boolean.class);
        oprandTypeList.add(Integer.class);
        oprandTypeList.add(Integer.class);

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCodeGreatThan.class.newInstance());
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
        Integer v1 = (Integer) values.get(0);
        Integer v2 = (Integer) values.get(1);
        return v1 > v2;
    }

    public String getOperandTypeStr()
    {
        return oprandTypeListStr;
    }
}
