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
public class GPReporting {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        OpCodeRegistrar.populateInitalOpCodePool();


        EvalEnv exampleEnv = GPAgent.genExampleEnv();


        //Agent controller = new ForwardJumpingAgent();
        GPAgent agent = new GPAgent();

        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(agent);
        GPTasks task = new GPTasks(options);

        options.setMaxFPS(true);
        options.setVisualization(false);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setLevelDifficulty(3);
        task.setOptions(options);

        long beginTime = System.currentTimeMillis();

        List<GPParams> paramsList = new ArrayList<GPParams>();
        paramsList.add(GPParams.setting1());
        paramsList.add(GPParams.setting2());
        paramsList.add(GPParams.setting3());

        // loaded the harvested
        PopulationPool bestPool = PopulationPool.Load(new Scanner(new File("D:\\harvested22x22.txt")));

        // test the harvested

        // clear stats
        for (PopulationEntry entry : bestPool.pool)
        {
            entry.stats = new GPStats();
        }
        bestPool.printStats();
        for(int i = 0 ; i < 10; i ++)
        {
            int curSeed = (int) (Math.random() * Integer.MAX_VALUE);
            options.setLevelRandSeed(curSeed);
            int popProgress = 0;
            for (PopulationEntry entry : bestPool.pool)
            {
                popProgress++;
                agent.tree = entry.tree;
                entry.stats = new GPStats();
                GPStats curResult = new GPStats();
                task.evaluate(agent, curResult);
                entry.stats.add(curResult);
                //if (popProgress % 100 == 0)
                {
                    System.out.println("" + popProgress + "/" + bestPool.pool.size() + " Score: " + entry.stats.score + " Time: " + (System.currentTimeMillis() - beginTime) / 1000.0);
                }
            }
        }
        bestPool.sortPopulation();
        bestPool.printStats();
        Writer writer = new FileWriter(new File("d:\\report22x22.txt"));
        bestPool.save(writer);
        writer.flush();
        writer.close();
    }
}
