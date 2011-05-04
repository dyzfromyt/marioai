/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.BDD;

import ch.idsia.tools.*;
import cs229.programsearch.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GPDemo
{

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        OpCodeRegistrar.populateInitalOpCodePool();


        EvalEnv exampleEnv = GPAgent.genExampleEnv();


        //Agent controller = new ForwardJumpingAgent();
        GPAgent agent = new GPAgent();

        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(agent);
        GPTasks task = new GPTasks(options);

        options.setMaxFPS(false);
        options.setVisualization(true);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setLevelDifficulty(1);
        task.setOptions(options);

        long beginTime = System.currentTimeMillis();

        List<GPParams> paramsList = new ArrayList<GPParams>();
        paramsList.add(GPParams.setting1());
        paramsList.add(GPParams.setting2());
        paramsList.add(GPParams.setting3());

        // loaded the harvested
        PopulationPool bestPool = PopulationPool.Load(new Scanner(new File("/home/yanzhu/Downloads/report.txt")));

        // test the harvested

        // clear stats
        for (PopulationEntry entry : bestPool.pool)
        {
            entry.stats = new GPStats();
        }
        bestPool.printStats();

        while(true)
        {
            int curSeed = (int) (Math.random() * Integer.MAX_VALUE);
            options.setLevelRandSeed(curSeed);
            PopulationEntry entry = bestPool.pool.get(1);
            //System.out.print(GPNodeUtil.toDot(entry.tree.root));
            {
                agent.tree = entry.tree;
                entry.stats = new GPStats();
                GPStats curResult = new GPStats();
                task.evaluate(agent, curResult);
                entry.stats.add(curResult);
            }
        }
    }
}
