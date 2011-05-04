/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class TransProb implements Serializable
{
    public HashMap<Integer, ArrayList<HashMap<Integer, TransProbEntry>>> matrixEntries;
    private HashMap<Integer, ArrayList<Integer>> saTotalCount;
    public TransProb()
    {
        matrixEntries = new HashMap<Integer, ArrayList<HashMap<Integer, TransProbEntry>>>();
        saTotalCount = new HashMap<Integer, ArrayList<Integer>>();
    }

    public void updateProb(int state, int action)
    {
        ArrayList<Integer> saTotalCountStateData = saTotalCount.get(state);
        if (saTotalCountStateData == null)
        {
            //saTotalCountStateData = new ArrayList<Integer>();
            throw new RuntimeException("Unseen state");
        }
        if(saTotalCountStateData.size() <= action)
        {
            throw new RuntimeException("Unseen action");
        }
        
//        while (saTotalCountStateData.size() <= action)
//        {
//            saTotalCountStateData.add(0);
//        }
        int totalCount = saTotalCountStateData.get(action);
        if (totalCount == 0)
        {
            throw new RuntimeException("zero total");
        }

        for (Map.Entry<Integer, TransProbEntry> saspData : matrixEntries.get(state).get(action).entrySet())
        {
            saspData.getValue().prob = ((double) saspData.getValue().obsFrequency) / ((double) totalCount);
        }
    }
    public Set<Map.Entry<Integer, TransProbEntry>> getNextStateProb(int state, int action)
    {
        return matrixEntries.get(state).get(action).entrySet();
    }
    public  TransProb makeACopy()
    {
        TransProb result = new TransProb();
        for(Map.Entry<Integer, ArrayList<Integer>> saTotalCountStateData : saTotalCount.entrySet())
        {
                ArrayList<Integer> resultSaTotalCountStateData = new ArrayList<Integer>();
                result.saTotalCount.put(saTotalCountStateData.getKey(), resultSaTotalCountStateData);
                for(Integer i : saTotalCountStateData.getValue())
                {
                    int count = i;
                    resultSaTotalCountStateData.add(count);
                }
        }


        for (Map.Entry<Integer, ArrayList<HashMap<Integer, TransProbEntry>>> stateData : matrixEntries.entrySet())
        {
            ArrayList<HashMap<Integer, TransProbEntry>> resultStateData = new ArrayList<HashMap<Integer, TransProbEntry>>();
            result.matrixEntries.put(stateData.getKey(), resultStateData);
            
            for(HashMap<Integer, TransProbEntry> saData : stateData.getValue())
            {
                HashMap<Integer, TransProbEntry> resultSaData = new HashMap<Integer, TransProbEntry>();
                resultStateData.add(resultSaData);
                for (Map.Entry<Integer, TransProbEntry> saspData : saData.entrySet())
                {
                    resultSaData.put(saspData.getKey(), saspData.getValue().makeACopy());
                }
            }
        }
        return result;
    }

    public void updateProb()
    {
        for (Map.Entry<Integer, ArrayList<HashMap<Integer, TransProbEntry>>> stateData : matrixEntries.entrySet())
        {
            int actionIndex = 0;
            for (HashMap<Integer, TransProbEntry> saData : stateData.getValue())
            {
                //int totalCount = saTotalCount.get(stateData.getKey()).get(actionIndex);
                actionIndex++;
//                for (Map.Entry<Integer, TransProbEntry> saspData : saData.entrySet())
//                {
//                    saspData.getValue().prob = ((double) saspData.getValue().obsFrequency) / ((double)totalCount);
//                }
                updateProb(stateData.getKey(), actionIndex);
            }
        }
    }
    
    public void incObsFrequency(int state, int action, int newState)
    {
        // increase total count
        ArrayList<Integer> saTotalCountStateData = saTotalCount.get(state);
        if (saTotalCountStateData == null)
        {
            saTotalCountStateData = new ArrayList<Integer>();
            saTotalCount.put(state, saTotalCountStateData);
        }
        while (saTotalCountStateData.size() <= action)
        {
            saTotalCountStateData.add(0);
        }
        int curTotalCount = saTotalCountStateData.get(action);
        curTotalCount ++;
        saTotalCountStateData.set(action, curTotalCount);

        // increase count
        ArrayList<HashMap<Integer, TransProbEntry>> stateData = matrixEntries.get(state);
        if(stateData == null)
        {
            stateData = new ArrayList<HashMap<Integer, TransProbEntry>>();
            matrixEntries.put(state, stateData);
        }
        while(stateData.size() <= action)
        {
            stateData.add(new HashMap<Integer, TransProbEntry>());
        }
        HashMap<Integer, TransProbEntry> saData = stateData.get(action);
        TransProbEntry entryData = saData.get(newState);
        if(entryData == null)
        {
            entryData = new TransProbEntry();
            entryData.state = newState;
            saData.put(newState, entryData);
        }
        entryData.obsFrequency++;
    }

    public void save(Writer outStream)
    {
        //saveHashMap<Integer, TransProbEntry>(matrixEntries, outStream);
    }

    public static TransProb load(InputStream inStream)
    {
        Scanner scanner = new Scanner(inStream);
        TransProb result = new TransProb();
        return null;
    }


}
