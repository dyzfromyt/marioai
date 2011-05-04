/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.environments.Environment;

/**
 *
 * @author Administrator
 */
public interface FeatureMapping
{
    public byte [] map(Environment observation);
    public String getName();
    public int getFeatureLength();
    public int featureToID(byte [] feature);
    public byte[] idToFeature(int id);
    public int getNumberOfID();
    public double reward(byte [] feature, MarioComponent trueState, int action);
            
}
