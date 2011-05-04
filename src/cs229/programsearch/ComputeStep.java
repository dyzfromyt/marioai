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
public class ComputeStep
{
    private OpCode opCode;
    private Object evalResult; // type is the same as opCode
    private List<ComputeStepArgument> arguments;
    public ComputeStep()
    {
        arguments = new ArrayList<ComputeStepArgument>();
    }
    public OpCode getOpcode()
    {
        return opCode;
    }
    public Object getEvalResult()
    {
        return evalResult;
    }
    public void setEvalResult(Object result)
    {
        evalResult = result;
    }
    public List<ComputeStepArgument> getArguments()
    {
        return arguments;
    }
    
}
