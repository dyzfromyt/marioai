/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class EvalEnv
{
    private ArrayList<Object> variableValue; 
    private ArrayList<Class> variableType;
    private byte [][] mapCell;
    private int levelLength = -1;
    private int levelHeight = 15;

    // currently not used
    public EvalEnv()
    {
            variableValue = new ArrayList<Object>();
            variableType = new ArrayList<Class>();
    }
    public EvalEnv(int levelLength)
    {
        this();
        mapCell = new byte [levelLength][15] ;
        for(int x = 0 ; x < levelLength ;x ++)
        {
            for (int y = 0; y < levelHeight; y++)
            {
                mapCell [x][y]=-128;
            }
        }
    }

    // currently not used
    public Object getMapCellValue(int x, int y)
    {
        if(x>= 0 && x < levelLength)
        {
            if(y >=0 && y < levelHeight)
            {
                return mapCell[x][y];
            }
        }
        return -128;
    }
    
    public Object getVariableValue(int variableID)
    {
        return variableValue.get(variableID);
    }

    // should only be called during init
    public void createVariable(Class type, Object initialValue)
    {
        variableType.add(type);
        variableValue.add(initialValue);
    }
    public void setVariableValue(int variableID, Object val)
    {
        if (variableType.get(variableID).isInstance(val))
        {
            variableValue.set(variableID, val);
        }
        else
        {
            throw new RuntimeException("bug: assigning none compatible types");
        }
    }

    public List<Integer> getVariablesOfType(Class type)
    {
        List<Integer> result = new ArrayList<Integer>();
        int varID = 0;
        for(Class c : variableType)
        {
            if(type.equals(c))
            {
                result.add(varID);
            }
            varID ++;
        }
        
        return result;
    }
    

}
