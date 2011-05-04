/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class OpCodeEquals extends OpCode
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
            OpCodeRegistrar.registerOpCode(OpCodeEquals.class.newInstance());
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
        Integer v2 = (Integer) values.get(1);
        return v1 == v2;
    }
}
