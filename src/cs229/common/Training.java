/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;
import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.tools.EvaluationOptions;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;

/**
 *
 * @author Stephen
 */
public class Training {

        public static void main(String[] args) {
        Agent controller = new HumanKeyboardAgent();
        if (args.length > 0) {
            controller = AgentsPool.load (args[0]);
            AgentsPool.addAgent(controller);
        }
        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(controller);
        Task task = new ProgressTask(options);
        options.setMaxFPS(false);
        options.setVisualization(true);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setLevelRandSeed((int) (Math.random () * Integer.MAX_VALUE));
        options.setLevelDifficulty(3);
        task.setOptions(options);

        // task.evaluate will call evaluate in Evaluator.java
        // evaluate will call simulateonelevel in basic simulator
        // this will call run1 in MarioComponent
        // need to output (action, observation) pair to disk
        System.out.println ("Score: " + task.evaluate (controller)[0]);
        }
}
