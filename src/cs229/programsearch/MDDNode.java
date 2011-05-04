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
public class MDDNode
{
    private List<ComputeStep> programList;
    private int id;
    public MDDNode(int nodeID)
    {
        programList = new ArrayList<ComputeStep>();
        id = nodeID;
    }
    // compute steps segment
    public List<ComputeStep> getComputeSteps()
    {
        return programList;
    }
    public Object getArgumentValue(EvalEnv env, MDDEnv mddenv, int instP, ComputeStepArgument arg)
    {
        if (arg.getSource() == ComputeStepArgument.ArgumentSource.CONSTANT)
        {
            return arg.getConstantValue();
        }
        else if (arg.getSource() == ComputeStepArgument.ArgumentSource.GLOBAL)
        {
            return env.getVariableValue(arg.getVariableID());
        }
        else if (arg.getSource() == ComputeStepArgument.ArgumentSource.LOCAL)
        {
            instP -= arg.getVariableID();
            return programList.get(instP).getEvalResult();
        }
        else if (arg.getSource() == ComputeStepArgument.ArgumentSource.MDD)
        {
            Object result = mddenv.envVar.get(arg.getVariableID());
            if(result == null)
            {
                throw new RuntimeException("bug: unknow exported variable");
            }
            return result;
        }
        throw new RuntimeException("bug: unknown Argument source");
    }

    public void eval(EvalEnv global, MDDEnv mddenv)
    {
        int instrPointer = 0;
        for(ComputeStep step : programList)
        {
            List<ComputeStepArgument> args = step.getArguments();
            List<Object> vals = new ArrayList<Object>();
            for(ComputeStepArgument arg : args)
            {
                vals.add(getArgumentValue(global, mddenv, instrPointer, arg));
            }
            Object result = step.getOpcode().eval(vals);
            step.setEvalResult(result);
            instrPointer++;
        }
    }
    // jump segment
//    public boolean isConditional();
//    public void setIsConditional(boolean isCond, ComputeStep cond);
//
//    public MDDNode getThenBranch(); // double as unconditional jump branch
//    public MDDNode getElseBranch();
//    public void setThenBranch(MDDNode target);
//    public void setElseBranch(MDDNode target);
//

}
