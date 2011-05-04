/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.mdp;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.environments.Environment;
import cs229.common.ActionMappingMario1;
//import cs229.common.FeatureMappingMario1;
import cs229.common.ActionMappingMario2;
import cs229.common.FeatureMappingMario2;
import cs229.common.FeatureMappingMario3;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class MDPAgent implements Agent
{
    public MarioMDP problem;
    private String agentName = "";
    public MDPAgent(String policyFileName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        agentName = "MarioMDPAgent";
        this.problem = MarioMDP.getMDPProblem();
        problem.load(policyFileName);
    }
    public MDPAgent() throws FileNotFoundException, FileNotFoundException, IOException, ClassNotFoundException
    {
        this("D:\\marioPolicy_v2");
        agentName = "MarioMDPAgent_" + "D:\\marioPolicy_v2";
    }

    public MDPAgent(boolean empty)
    {
        agentName = "MarioMDPAgent";
        this.problem = MarioMDP.getMDPProblem();
    }
    public void loadMDPModel(MarioMDP p)
    {
        
    }
    public void reset()
    {
        
    }

    public boolean[] getAction(Environment observation)
    {
        ActionValue result = new ActionValue();

        problem.getBestAction((MarioComponent)observation, result);
        return problem.getActionFromID(result.action);
    }

//    public boolean[] getActionOld(Environment observation)
//    {
//        //byte [] feature = problem.mapGameToFeature(observation);
//        int numActions = problem.getNumAction();
//        int bestAction = -1;
//        double bestVal = - Double.MAX_VALUE;
//
//        for(int a = 0 ; a < numActions ; a ++)
//        {
//            byte[] features = problem.mapGameToFeature(observation);
//            int sid = problem.getStateID(features);
//
//            MarioComponent sp = (MarioComponent)((MarioComponent) observation).makeTrainingCopy(null);
//            boolean[] keys = problem.getActionFromID(a);
//            float oldX = sp.levelScene.mario.x;
//            float oldY = sp.levelScene.mario.y;
//            sp.levelScene.mario.keys = keys;
//            for(int t = 0 ; t < MarioMDP.TicksToNextState ; t++)
//            {
//                sp.levelScene.tick();
//            }
//
//            byte[] featurep = problem.mapGameToFeature(sp);
//            int spid = problem.getStateID(featurep);
//            double actionVal = problem.getValue(spid);
//            actionVal += problem.getReward(sid, sp, a);
//
//            //System.out.println(sp.levelScene.levelLength + " " + sp.levelScene.mario.x);//debug
//
//            System.out.print(a + " " + actionVal + " "); // debug
//            if(actionVal > bestVal )
//            {
//                bestAction = a;
//                bestVal = actionVal;
//            }
//        }
//        System.out.println("");
//        return problem.getActionFromID(bestAction);
//    }

    public AGENT_TYPE getType()
    {
        return AGENT_TYPE.AI;
    }

    public String getName()
    {
        return agentName;
    }

    public void setName(String name)
    {
        agentName = name;
    }

}
