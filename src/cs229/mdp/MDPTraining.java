/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.mdp;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.ai.ForwardJumpingAgent;
import ch.idsia.ai.agents.ai.RandomAgent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.mario.simulation.BasicSimulator;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.mario.simulation.SimulationOptions;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.EvaluationOptions;
import cs229.common.ActionMappingMario1;
//import cs229.common.FeatureMappingMario1;
import cs229.common.ActionMappingMario2;
import cs229.common.FeatureMappingMario2;
import cs229.common.FeatureMappingMario3;
import cs229.common.TransProbEntry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
/**
 *
 * @author Administrator
 */
public class MDPTraining 
{
    //public static MarioMDP problem= null;
    public static void main(String [] args) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        MarioMDP trainProblem = MarioMDP.getMDPProblem(); // training Problem
        
        //Agent controller = new ForwardJumpingAgent();
        MDPAgent mdpAgent = new MDPAgent(true);
        Agent controller = mdpAgent;

        if (args.length > 0)
        {
            controller = AgentsPool.load(args[0]);
            AgentsPool.addAgent(controller);
        }
        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(controller);
        Task task = new ProgressTask(options);
        options.setMaxFPS(true);
        options.setVisualization(false);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setLevelDifficulty(3);
        task.setOptions(options);

        long beginTime = System.currentTimeMillis();
        int i = 0;
        while(true)
        {
            if(System.in.available()!=0)
            {
                System.out.println("Stopping");
                break;
            }
            options.setLevelRandSeed((int) (Math.random() * Integer.MAX_VALUE));
            double sc = task.evaluate(controller)[0];
            System.out.println("Training Iter: " + i + " Score: " + sc + " Time: " + (System.currentTimeMillis()-beginTime)/1000.0);
            i++;
            
            trainProblem.copyFrom(mdpAgent.problem);
            
            trainProblem.trainToConverge();
            
            mdpAgent.problem.copyFrom(trainProblem);
        }

        mdpAgent.problem.save("D:\\marioPolicy_v2");
        System.out.println("Saved");
    }

    public static void train(MarioComponent s)
    {
//        if(problem == null)
//        {
//            return;
//        }
//        byte[] feature = problem.mapGameToFeature(s);
//        int sid = problem.getStateID(feature);
//        ActionValue result = new ActionValue();
//        problem.getBestAction(s, result);
//        problem.setValue(sid, result.value);
    }



    public static void dustbin()
    {
//        double bestActionVal = -Double.MAX_VALUE;
//        double gamma = problem.getDiscountFactor();
//
//        byte[] feature = problem.mapGameToFeature(s);
//        int sid = problem.getStateID(feature);
//
//        for (int a = 0; a < problem.getNumAction(); a++)
//        {
//            MarioComponent sp = (MarioComponent) s.makeTrainingCopy(null);
//            boolean[] keys = problem.getActionFromID(a);
//            sp.levelScene.mario.keys = keys;
//            for (int t = 0; t < MarioMDP.TicksToNextState; t++)
//            {
//                sp.levelScene.tick();
//            }
//            byte[] featurep = problem.mapGameToFeature(sp);
//            int spid = problem.getStateID(featurep);
//            double reward = problem.getReward(featurep, sp, a);
//
//            problem.transProb.incObsFrequency(sid, a, spid);
//            problem.transProb.updateProb(sid, a);
//            double actionValue = 0.0;
//            double possibleNextStateValue = 0.0;
//            for (Map.Entry<Integer, TransProbEntry> nextState : problem.transProb.getNextStateProb(sid, a))
//            {
//                double prob = nextState.getValue().prob;
//                int nextStateID = nextState.getValue().state;
//                possibleNextStateValue += prob * problem.getValue(nextStateID);
//
//            }
//            actionValue = reward + gamma * possibleNextStateValue;
//
//            if (actionValue > bestActionVal)
//            {
//                bestActionVal = actionValue;
//            }
//        }
    }

}
