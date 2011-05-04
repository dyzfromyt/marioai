/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import ch.idsia.ai.agents.*;
import ch.idsia.ai.tasks.*;
import ch.idsia.tools.*;
import cs229.programsearch.*;
import java.util.*;
import java.io.*;
import java.io.FileWriter;

/**
 *
 * @author Administrator
 */
public class GPTraining
{
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
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
        for(GPParams params : paramsList)
        {
            for(int batch = 0; batch < batchPerSet ; batch ++)
            {

                PopulationPool pop = new PopulationPool();
                pop.initPop(1000, exampleEnv, params);
                for(int i = 0 ; i <  params.maxGeneration ; i++)
                {
                    String populationFileBaseName = "D:\\mario\\" + params.paramName + "\\" + batch + "\\pop";
                    File outputFile = new File(populationFileBaseName + "." + i + ".txt");
                    File dir = new File(outputFile.getParent());
                    dir.mkdirs();


                    int curSeed = (int) (Math.random() * Integer.MAX_VALUE);
                    options.setLevelRandSeed(curSeed);
                    int popProgress = 0;
                    for(PopulationEntry entry : pop.pool)
                    {
                        popProgress ++;
                        agent.tree = entry.tree;
                        task.evaluate(agent, entry.stats);
                        if(popProgress % 100 ==0)
                        {
                            System.out.println("" + popProgress + "/" + pop.pool.size() + " Score: " + entry.stats.score + " Time: " + (System.currentTimeMillis() - beginTime) / 1000.0);
                        }
                    }

                    System.out.println("--------- " + i + "/" + params.maxGeneration + " ---------------");
                    pop.sortPopulation();
                    pop.printStats();


                    FileWriter writer = new FileWriter(outputFile);
                    pop.save(writer);
                    writer.flush();
                    writer.close();

                    if (System.in.available() != 0)
                    {
                        System.out.println("Stopped");
                        break;
                    }
                    if(i + 1 < params.maxGeneration)
                    {
                        // only do population update if there is next....
                        System.out.println("Generate new population");
                        pop.nextPopulation(exampleEnv, params);
                    }
                }
            }
        }
    }

}
