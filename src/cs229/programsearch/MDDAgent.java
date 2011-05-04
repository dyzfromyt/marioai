/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import cs229.common.Utils;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class MDDAgent implements Agent
{
    private MDD program;
    private String agentName;
    public void reset()
    {
        
    }

    public boolean[] getAction(Environment observation)
    {
        byte [][] obs = observation.getCompleteObservation();
        byte [] allCells = Utils.convertArray2Dto1D(obs);
        int numVar = allCells.length;

//        List<MDDNode> executionPath = new ArrayList<MDDNode> ();
//        MDDNode curNode = program.getEntryPoint();
//        executionPath.add(curNode);
//        while(!curNode.isTerminal())
//        {
//            int varID = curNode.getVarID();
//            byte varValue = allCells[varID];
//            curNode = curNode.getNextNode(varValue);
//            executionPath.add(curNode);
//        }
//
//        // depends on how the result, the execution path should be rewarded or punished
//        for(MDDNode node : executionPath)
//        {
//
//        }
//        int nodeVal = curNode.getValue();

        boolean[] keys = new boolean[Environment.numberOfButtons];
//        if (nodeVal > 0)
//        {
//            keys[Mario.KEY_JUMP] = true;
//        }
//        else
//        {
//            keys[Mario.KEY_JUMP] = false;
//        }
        return keys;
    }

    public AGENT_TYPE getType()
    {
        return AGENT_TYPE.AI;
    }

    public String getName()
    {
        return "MDDAgent";
    }

    public void setName(String name)
    {
        agentName = name;
    }

    

}
