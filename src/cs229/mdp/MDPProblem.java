/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.mdp;

import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.environments.Environment;

/**
 *
 * @author Administrator
 */
public interface MDPProblem
{
    public int getNumState();
    public int getStateID(byte [] feature);
    public byte [] getFeatureFromStateID(int state);
    public int getNumAction();
    public double getReward(int state, MarioComponent sp, int action);
    public double getReward(byte[] feature, MarioComponent sp, int action);
    public byte[] mapGameToFeature(Environment observation);
    public double getValue(int state);
    public void setValue(int state, double val);
    public boolean[] getActionFromID(int actionid);
    public double getDiscountFactor();
}
