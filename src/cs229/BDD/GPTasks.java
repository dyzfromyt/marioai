/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.BDD;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.tasks.Task;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.EvaluationOptions;
import ch.idsia.tools.Evaluator;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 11:26:43 AM
 * Package: ch.idsia.ai.tasks
 */
public class GPTasks implements Task
{

    private EvaluationOptions options;

    public GPTasks(EvaluationOptions evaluationOptions)
    {
        setOptions(evaluationOptions);
    }

    public void evaluate(GPAgent controller, GPStats resultStats)
    {
        double distanceTravelled = 0;
        int actionPerformed = 0;

//        controller.reset();
        options.setAgent(controller);
        Evaluator evaluator = new Evaluator(options);
        List<EvaluationInfo> results = evaluator.evaluate();
        for (EvaluationInfo result : results)
        {
            //if (result.marioStatus == Mario.STATUS_WIN )
            //    Easy.save(options.getAgent(), options.getAgent().getName() + ".xml");
            distanceTravelled += result.computeDistancePassed();
            actionPerformed  += result.totalActionsPerfomed;
        }
        distanceTravelled = distanceTravelled / results.size();
        actionPerformed = actionPerformed/ results.size();
        resultStats.score = (int)distanceTravelled;
        resultStats.numJump = (int) actionPerformed;
    }
    
    public double[] evaluate(Agent controller)
    {
        double distanceTravelled = 0;
//        controller.reset();
        options.setAgent(controller);
        Evaluator evaluator = new Evaluator(options);
        List<EvaluationInfo> results = evaluator.evaluate();
        for (EvaluationInfo result : results)
        {
            //if (result.marioStatus == Mario.STATUS_WIN )
            //    Easy.save(options.getAgent(), options.getAgent().getName() + ".xml");
            distanceTravelled += result.computeDistancePassed();
        }
        distanceTravelled = distanceTravelled / results.size();

        return new double[]
                {
                    distanceTravelled
                };
    }

    public void setOptions(EvaluationOptions options)
    {
        this.options = options;
    }

    public EvaluationOptions getOptions()
    {
        return options;
    }
}

