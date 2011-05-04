/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.mario.simulation.SimulationOptions;
import ch.idsia.tools.EvaluationInfo;
import cs229.mdp.TrainingMarioComponent;

/**
 *
 * @author Administrator
 */
public class TrainingSimulator implements Simulation
{
    SimulationOptions simulationOptions = null;
    private TrainingMarioComponent marioComponent;

    public TrainingSimulator(SimulationOptions simulationOptions)
    {
        //GlobalOptions.VisualizationOn = simulationOptions.isVisualization();
        //this.marioComponent = GlobalOptions.getMarioComponent();
        this.marioComponent = new TrainingMarioComponent();
        this.setSimulationOptions(simulationOptions);
    }

    private MarioComponent prepareMarioComponent()
    {
        Agent agent = simulationOptions.getAgent();
        agent.reset();
        marioComponent.setAgent(agent);
        return marioComponent;
    }

    public void setSimulationOptions(SimulationOptions simulationOptions)
    {
        this.simulationOptions = simulationOptions;
    }
    public EvaluationInfo simulateOneLevel()
    {
        prepareMarioComponent();

        marioComponent.setZLevelScene(simulationOptions.getZLevelMap());
        marioComponent.setZLevelEnemies(simulationOptions.getZLevelEnemies());
        marioComponent.startLevel(simulationOptions.getLevelRandSeed(), simulationOptions.getLevelDifficulty(), simulationOptions.getLevelType(), simulationOptions.getLevelLength(),
                simulationOptions.getTimeLimit());
        marioComponent.setPaused(simulationOptions.isPauseWorld());
        marioComponent.setZLevelEnemies(simulationOptions.getZLevelEnemies());
        marioComponent.setZLevelScene(simulationOptions.getZLevelMap());

        marioComponent.resetMario(simulationOptions.getMarioMode());
        marioComponent.setMarioInvulnerable(simulationOptions.isMarioInvulnerable());

        return marioComponent.run1(simulationOptions.currentTrial++, simulationOptions.getNumberOfTrials());
    }
}
