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
public class ComputeStepArgument
{
    public enum ArgumentSource
    {

        CONSTANT, LOCAL, MDD,  GLOBAL
       // constant value, Relative Position within the List of ComputeSteps, Exported MDD symbol, Environment Variable
    };
    private ArgumentSource argumentSource;
    private Object constValue;
    private int variableID;
    private Class arguementType;

    public ArgumentSource getSource()
    {
        return argumentSource;
    }

    public ComputeStepArgument(Class type, ArgumentSource source)
    {
        arguementType = type;
        argumentSource = source;
    }
    
    public Class getType()
    {
        return arguementType;
    }

    // constants
    public Object getConstantValue()
    {
        return constValue;
    }
    public void setConstantValue(Object val)
    {
        constValue = val;
    }

    // global variable
    public void setVariableID(int varID)
    {
        variableID = varID;
    }

    public int getVariableID()
    {
        return variableID;
    }
    

}
