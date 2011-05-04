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
public class GPHarvest
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

        int batchPerSet = 30;
        String resultDir = "D:\\tesMario\\mario_22x22";
        File resultDirFile = new File(resultDir);

        PopulationPool bestPool = new PopulationPool();

        // harvest the best
        for (File params : resultDirFile.listFiles())
        {
            for (File batch : params.listFiles())
            {
                int maxIter = -1;
                File maxIterFile = null;
                for (File result : batch.listFiles())
                {
                    String iter = result.getName();
                    String[] parts = iter.split("\\.");
                    int curIter = Integer.parseInt(parts[1]);
                    if (curIter > maxIter)
                    {
                        maxIter = curIter;
                        maxIterFile = result;
                    }
                }
                {
                    if (maxIterFile == null)
                    {
                        continue;
                    }
                    System.out.println("Param:\t" + params.getName());
                    System.out.println("Batch:\t" + batch.getName());
                    System.out.println("result:\t" + maxIterFile.getName());
                    PopulationPool pop = PopulationPool.Load(new Scanner(maxIterFile));
                    pop.printStats();
                    // first top 2
                    bestPool.pool.add(pop.pool.get(0));
                    bestPool.pool.add(pop.pool.get(1));
                }
            }
        }

        // save the harvested
        Writer writer = new FileWriter(new File("d:\\harvested22x22.txt"));
        bestPool.save(writer);
        writer.flush();
        writer.close();
    }
}
