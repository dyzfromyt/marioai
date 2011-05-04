/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.mdp;

import ch.idsia.mario.engine.MarioComponent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import ch.idsia.mario.environments.Environment;
import cs229.common.*;
import cs229.common.ActionMappingMario3;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class MarioMDP implements MDPProblem
{

    public FeatureMapping featureMap;
    public ActionMapping actionMap;
    public double[] valueFunction;
    public double[] rewardFunction;
    public int[] rewardCount;
    
    public TransProb transProb;
    private int numState;
    private int numAction;
    public static final int TicksToNextState = 7;
    public static final double Epsilon = 0.001;
    public static final int maxMDPValueIterationNum = 500;

    // selector method
    public static MarioMDP getMDPProblem()
    {
        return getMDPProblem4();
    }

    public static MarioMDP getMDPProblem2()
    {
        return new MarioMDP(new FeatureMappingMario3(), new ActionMappingMario2());
    }

    public static MarioMDP getMDPProblem3()
    {
        return new MarioMDP(new FeatureMappingMario4(), new ActionMappingMario3());
    }
    
    public static MarioMDP getMDPProblem4()
    {
        return new MarioMDP(new FeatureMappingMario5(), new ActionMappingMario3());
    }

    public static MarioMDP getMDPProblem1()
    {
        return new MarioMDP(new FeatureMappingMario2(), new ActionMappingMario1());
    }

    private MarioMDP(FeatureMapping featureMap, ActionMapping actionMap)
    {
        this.transProb = new TransProb();
        this.featureMap = featureMap;
        this.actionMap = actionMap;
        numState = featureMap.getNumberOfID();
        numAction = actionMap.getNumberOfID();

        rewardCount = new int[numState * numAction];
        rewardFunction = new double[numState * numAction];
        valueFunction = new double[numState];

        for (int i = 0; i < numState; i++)
        {
            valueFunction[i] = 0.0;
            int baseState = i * numAction;
            for (int a = 0; a < numAction; a++)
            {
                rewardFunction[baseState + a] = 0.0;
                rewardCount[baseState + a] = 0;
            }
        }
    }

    public void save(String fileBaseName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        saveValueFunction(fileBaseName + ".v");
        saveTransProb(fileBaseName + ".t");
    }

    private void saveTransProb(String fileName) throws FileNotFoundException, IOException
    {
        ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(fileName));
        outFile.writeObject(transProb);
        outFile.flush();
        outFile.close();
    }

    private void saveValueFunction(String fileName) throws FileNotFoundException, IOException
    {
        int nonZero = 0;
        int pos = 0;
        int neg = 0;
        //DataOutputStream doc = new DataOutputStream(new FileOutputStream(fileName));
        FileWriter doc = new FileWriter(fileName);
        doc.write("" + numState);
        doc.write("\n");
        for (int i = 0; i < numState; i++)
        {
            doc.write("" + valueFunction[i]);
            doc.write(" ");
            int baseState = i * numAction;
            for (int a = 0; a < numAction; a++)
            {
                doc.write("" + rewardFunction[baseState + a]);
                doc.write(" ");
                doc.write("" + rewardCount[baseState + a]);
                doc.write(" ");
            }
            doc.write("\n");
            if (valueFunction[i] != 0)
            {
                nonZero++;
                if (valueFunction[i] > 0)
                {
                    pos++;
                }
                else if (valueFunction[i] < 0)
                {
                    neg++;
                }
            }
            //doc.writeDouble(valueFunction[i]);
        }

        System.out.println("nonzero/pos/neg value entries: " + nonZero + " " + pos + " " + neg);

        doc.flush();
        doc.close();
    }

    public void load(String fileBaseName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        loadValueFunction(fileBaseName + ".v");
        loadTransProb(fileBaseName + ".t");
    }

    private void loadValueFunction(String fileName) throws FileNotFoundException, IOException
    {
        //DataInputStream doc = new DataInputStream(new FileInputStream(fileName));

        Scanner doc = new Scanner(new File(fileName));

        numState = doc.nextInt();
        for (int i = 0; i < numState; i++)
        {
            valueFunction[i] = doc.nextDouble();
            int baseState = i * numAction;
            for (int a = 0; a < numAction; a++)
            {
                rewardFunction[baseState + a] = doc.nextDouble();
                rewardCount[baseState + a] = doc.nextInt();
            }
        }
        doc.close();
    }

    private void loadTransProb(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(fileName));
        transProb = (TransProb) inFile.readObject();
        inFile.close();
    }

    public void trainToConverge()
    {
        ActionValue result = new ActionValue();

        for(int curValueIterationCount = 0 ; curValueIterationCount< maxMDPValueIterationNum; curValueIterationCount++)
        {
            //System.out.println("Training iter:" + curValueIterationCount);
            
            double maxChange = -Double.MAX_VALUE;
            
            // state loop
            for (Map.Entry<Integer, ArrayList<HashMap<Integer, TransProbEntry>>> stateData : transProb.matrixEntries.entrySet())
            {
                int state = stateData.getKey();
                int actionIndex = 0;

                int bestAction = -1;
                double bestActionVal = -Double.MAX_VALUE;
                double gamma = getDiscountFactor();

                // action loop
                for (HashMap<Integer, TransProbEntry> saData : stateData.getValue())
                {
                    actionIndex++;


                    double actionValue = 0.0;
                    double possibleNextStateValue = 0.0;

                    Set<Map.Entry<Integer, TransProbEntry>> nextStateSet = saData.entrySet();
                    //System.out.println(nextStateSet.size());
                    if (nextStateSet.size() == 0)
                    {
                        continue;
                    }

                    // nextState loop
                    for (Map.Entry<Integer, TransProbEntry> nextState : nextStateSet)
                    {
                        double prob = nextState.getValue().prob;
                        int nextStateID = nextState.getValue().state;
                        possibleNextStateValue += prob * getValue(nextStateID);
                    }
                    double reward = rewardFunction[state * numAction + actionIndex];
                    actionValue = reward + gamma * possibleNextStateValue;
                    if (actionValue > bestActionVal)
                    {
                        bestActionVal = actionValue;
                        bestAction = actionIndex;
                    }
                    //System.out.println(actionIndex + " " + actionValue);
                }
                double oldVal = getValue(state);
                setValue(state, bestActionVal);
                double diff = Math.abs(bestActionVal - oldVal);
                if(diff > maxChange)
                {
                    //System.out.println(maxChange + " " + diff);
                    maxChange = diff;
                }
            }

            if(maxChange < Epsilon)
            {
                System.out.println("Training iter:" + curValueIterationCount);
                System.out.println("converged");
                break;
            }
        }
        

    }

    public void getBestAction(MarioComponent s, ActionValue result)
    {
        int bestAction = -1;
        double bestActionVal = -Double.MAX_VALUE;
        double gamma = getDiscountFactor();

        byte[] feature = mapGameToFeature(s);
        int sid = getStateID(feature);

        for (int a = 0; a < getNumAction(); a++)
        {
            MarioComponent sp = (MarioComponent) s.makeTrainingCopy(null);
            boolean[] keys = getActionFromID(a);
            sp.levelScene.mario.keys = keys;
            for (int t = 0; t < MarioMDP.TicksToNextState; t++)
            {
                sp.levelScene.tick();
            }
            byte[] featurep = mapGameToFeature(sp);
            int spid = getStateID(featurep);

            //double reward = getReward(featurep, sp, a);
            
            double reward = getReward(feature, s, a);
            //System.out.println("Action " + a);
            
            // update observed reward
            int rewardStateAction = sid * numAction + a;
            double pastTotalReward = rewardFunction[rewardStateAction] * rewardCount[rewardStateAction];
            rewardCount[rewardStateAction] ++;
            rewardFunction[rewardStateAction] = (pastTotalReward + reward)/ rewardCount[rewardStateAction];

            // update observed state transition frequency
            transProb.incObsFrequency(sid, a, spid);
            transProb.updateProb(sid, a);
            double actionValue = 0.0;
            double possibleNextStateValue = 0.0;

            Set<Map.Entry<Integer, TransProbEntry>> nextStateSet = transProb.getNextStateProb(sid, a);
            if (nextStateSet.size() == 0)
            {
                throw new RuntimeException("empty next state set");
            }

            for (Map.Entry<Integer, TransProbEntry> nextState : nextStateSet)
            {
                double prob = nextState.getValue().prob;
                int nextStateID = nextState.getValue().state;
                possibleNextStateValue += prob * getValue(nextStateID);

            }
            actionValue = reward + gamma * possibleNextStateValue;
            if (actionValue > bestActionVal)
            {
                bestActionVal = actionValue;
                bestAction = a;
            }
        }
        result.value = bestActionVal;
        result.action = bestAction;
    }

    public int getNumState()
    {
        return numState;
    }

    public int getStateID(byte[] feature)
    {
        return featureMap.featureToID(feature);
    }

    public byte[] getFeatureFromStateID(int state)
    {
        return featureMap.idToFeature(state);
    }

    public int getNumAction()
    {
        return actionMap.getNumberOfID();
    }

    public double getReward(int state, MarioComponent sp, int action)
    {
        return featureMap.reward(getFeatureFromStateID(state), sp, action);
    }

    public double getReward(byte[] feature, MarioComponent sp, int action)
    {
        return featureMap.reward(feature, sp, action);
    }

    public byte[] mapGameToFeature(Environment observation)
    {
        return featureMap.map(observation);
    }

    public boolean[] getActionFromID(int actionid)
    {
        return actionMap.idToAction(actionid);
    }

    public double getValue(int state)
    {
        return valueFunction[state];
    }

    public void setValue(int state, double val)
    {
        valueFunction[state] = val;
    }

    public double getDiscountFactor()
    {
        return 0.995;
    }

    public void copyFrom(MarioMDP p)
    {
        if (numState != p.numState)
        {
            throw new RuntimeException("trying to copy different model");
        }

        this.transProb = p.transProb.makeACopy();

        for (int i = 0; i < numState; i++)
        {
            this.valueFunction[i] = p.valueFunction[i];
            int baseState = i * numAction;
            for (int a = 0; a < numAction; a++)
            {
                this.rewardFunction[baseState + a] = p.rewardFunction[baseState + a];
                this.rewardCount[baseState + a] = p.rewardCount[baseState + a];

            }

        }

    }
}
