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
public class OpCodeSubstractFloat extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;
    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Float.class);
        oprandTypeList.add(Float.class);
        oprandTypeList.add(Float.class);

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCodeSubstractFloat.class.newInstance());
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
        Float v1 = (Float) values.get(0);
        Float v2 = (Float) values.get(1);
        return v1 - v2;
    }
}
