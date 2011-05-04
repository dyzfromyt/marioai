/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.mdp;

import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

/**
 *
 * @author Administrator
 */
public class TrainingMarioComponent extends MarioComponent
{
    public TrainingMarioComponent()
    {
        super(null);
        throw new RuntimeException("not used");
    }
    public EvaluationInfo run1(int currentTrial, int totalNumberOfTrials)
    {
        System.out.println("Trial: " + currentTrial +"/" + totalNumberOfTrials);
        running = true;
        adjustFPS();
        EvaluationInfo evaluationInfo = new EvaluationInfo();

        // Remember the starting time
        long tm = System.currentTimeMillis();
        long tick = tm;
        int marioStatus = Mario.STATUS_RUNNING;

        //mario = ((LevelScene) levelScene).mario;
        int totalActionsPerfomed = 0;
// TODO: Manage better place for this:
        levelScene.mario.resetCoins();

        while (running)
        {

            levelScene.tick();
            
            boolean[] action = agent.getAction(this/*DummyEnvironment*/);
            if (action != null)
            {
                for (int i = 0; i < Environment.numberOfButtons; ++i)
                {
                    if (action[i])
                    {
                        ++totalActionsPerfomed;
                        break;
                    }
                }
            }
            else
            {
                System.err.println("Null Action received. Skipping simulation...");
                stop();
            }


            //Apply action;
//            levelScene.keys = action;
            levelScene.mario.keys = action;
            levelScene.mario.cheatKeys = cheatAgent.getAction(null);

            // Win or Die without renderer!! independently.
            marioStatus =  levelScene.mario.getStatus();
            if (marioStatus != Mario.STATUS_RUNNING)
            {
                stop();
                System.out.println("Mario Died at Frame: " + frame);
            }

            // Advance the frame
            frame++;
            System.out.println("Frame: " + frame);
        }
//=========
        evaluationInfo.agentType = agent.getClass().getSimpleName();
        evaluationInfo.agentName = agent.getName();
        evaluationInfo.marioStatus = levelScene.mario.getStatus();
        evaluationInfo.livesLeft = levelScene.mario.lives;
        evaluationInfo.lengthOfLevelPassedPhys = levelScene.mario.x;
        evaluationInfo.lengthOfLevelPassedCells = levelScene.mario.mapX;
        evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
        evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
        evaluationInfo.timeSpentOnLevel = levelScene.getStartTime();
        evaluationInfo.timeLeft = levelScene.getTimeLeft();
        evaluationInfo.totalTimeGiven = levelScene.getTotalTime();
        evaluationInfo.numberOfGainedCoins = levelScene.mario.coins;
//        evaluationInfo.totalNumberOfCoins   = -1 ; // TODO: total Number of coins.
        evaluationInfo.totalActionsPerfomed = totalActionsPerfomed; // Counted during the play/simulation process
        evaluationInfo.totalFramesPerfomed = frame;
        evaluationInfo.marioMode = levelScene.mario.getMode();
        evaluationInfo.killsTotal = levelScene.mario.world.killedCreaturesTotal;
//        evaluationInfo.Memo = "Number of attempt: " + Mario.numberOfAttempts;
        return evaluationInfo;
    }

}
