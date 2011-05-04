/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.mdp;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationOptions;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class MDPRunning {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Agent controller = new MDPAgent();
        if(args.length > 0)
        {
            controller = AgentsPool.load(args[0]);
            AgentsPool.addAgent(controller);
        }
        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(controller);
        Task task = new ProgressTask(options);
        options.setMaxFPS(false);
        options.setVisualization(true);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setLevelDifficulty(1);
        task.setOptions(options);

        while(true)
        {
            options.setLevelRandSeed((int) (Math.random() * Integer.MAX_VALUE));
            System.out.println("Score: " + task.evaluate(controller)[0]);
        }

    }

}
