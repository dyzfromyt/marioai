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
// 4 tuple evaluation safe if, Result is Intger, Comparison is Among Integer
// result = a > b? c: d;
public class OpCode4TIfIntInt extends OpCode
{

    private static List<Class> oprandTypeList = null;
    private static String oprandTypeListStr = null;

    static
    {
        oprandTypeList = new ArrayList<Class>();
        oprandTypeList.add(Integer.class);  // result
        oprandTypeList.add(Integer.class); // a
        oprandTypeList.add(Integer.class); // b
        oprandTypeList.add(Integer.class); // c
        oprandTypeList.add(Integer.class); // d
        

        oprandTypeListStr = OpCodeRegistrar.typeListString(oprandTypeList);
        try
        {
            OpCodeRegistrar.registerOpCode(OpCode4TIfIntInt.class.newInstance());
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
        Integer a = (Integer) values.get(0);
        Integer b = (Integer) values.get(1);
        Integer c = (Integer) values.get(2);
        Integer d = (Integer) values.get(3);

        return (a > b ? c : d);
    }
}
