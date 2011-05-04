/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;


import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 *
 * @author Administrator
 */
public class OpCodeRegistrar
{
    private static HashMap<String, List<OpCode>> exactTypeTable;
    private static HashMap<String, List<OpCode>> resultTypeTable;
    static
    {
        exactTypeTable = new HashMap<String, List<OpCode>>();
        resultTypeTable = new HashMap<String, List<OpCode>>();
        populateInitalOpCodePool();

    }
    public static void populateInitalOpCodePool()
    {
        Object newCode = new OpCodeDiv();
        newCode = new OpCodeDivFloat();
        newCode = new OpCodeEquals();
        newCode = new OpCodeGreatThan();
        newCode = new OpCodeGreatThanFloat();
        newCode = new OpCodeLogicAnd();
        newCode = new OpCodeLogicNot();
        newCode = new OpCodeLogicOr();
        newCode = new OpCodeMult();
        newCode = new OpCodeMultFloat();
        newCode = new OpCodePlus();
        newCode = new OpCodePlusFloat();
        newCode = new OpCodeSubstract();
        newCode = new OpCodeSubstractFloat();
        newCode = new OpCodeTypeCastFloatInt();
        newCode = new OpCodeTypeCastIntFloat();
        newCode = new OpCode3TIfInt();
        newCode = new OpCode3TIfFloat();
        
        //newCode = new OpCode();
        //newCode = new OpCode();
    }
    public static void registerOpCode(OpCode applicant)
    {
        {
            String paramStr = applicant.getOperandTypeStr();
            if(!exactTypeTable.containsKey(paramStr))
            {
                exactTypeTable.put(paramStr, new ArrayList<OpCode>());
            }
            exactTypeTable.get(paramStr).add(applicant);
        }
        
        {
            String resultTypeStr = applicant.getOperandType().get(0).getName();
            if (!resultTypeTable.containsKey(resultTypeStr))
            {
                resultTypeTable.put(resultTypeStr, new ArrayList<OpCode>());
            }
            resultTypeTable.get(resultTypeStr).add(applicant);
        }
    }

    public static List<OpCode> findResultType(String resultType)
    {
        return resultTypeTable.get(resultType);
    }
    public static List<OpCode> findExactTypeMatch(String typeStr)
    {
        if (!exactTypeTable.containsKey(typeStr))
        {
            exactTypeTable.put(typeStr, new ArrayList<OpCode>());
        }
        return exactTypeTable.get(typeStr);
    }
    public static String typeListString(List<Class> typeList)
    {
        StringBuilder sb = new StringBuilder();
        for(Class c : typeList)
        {
            sb.append(c.getName());
            sb.append(",");
        }
        return sb.toString();
    }

    static void printTypeToOpCodeList()
    {
        for(Map.Entry<String, List<OpCode>> kvp : exactTypeTable.entrySet())
        {
            System.out.println("Signature : " + kvp.getKey());
            for(OpCode op : kvp.getValue())
            {
                System.out.println(op.toString());
            }
        }
    }


}
