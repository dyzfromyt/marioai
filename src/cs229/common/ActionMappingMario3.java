/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.common;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;

/**
 *
 * @author Administrator
 */
public class ActionMappingMario3 implements ActionMapping
{

    public static final int IDLE = 0;
    public static final int FAST = 1;
    public static final int SLOW_JUMP = 2;
    public static final int FAST_JUMP = 3;
    // we do not map the DOWN, "duck" key, because there is no way to observe that the mario is sliding...

    public int map(boolean[] keys)
    {
        return actionToID(keys);
    }

    public String getName()
    {
        return "ActionMappingMario3";
    }

    public int actionToID(boolean[] keys)
    {
        throw new RuntimeException("not implemented");
    }

    public boolean[] idToAction(int id)
    {
        boolean[] keys = new boolean[Environment.numberOfButtons];
        for (int i = 0; i < Environment.numberOfButtons; i++)
        {
            keys[i] = false;
        }
        switch (id)
        {
            case IDLE:
                keys[Mario.KEY_RIGHT] = true;
                //keys[Mario.KEY_SPEED] = true;
                //keys[Mario.KEY_JUMP] = false;
                break;
            case FAST:
                keys[Mario.KEY_RIGHT] = true;
                keys[Mario.KEY_SPEED] = true;
                //keys[Mario.KEY_JUMP] = true;
                break;
            case SLOW_JUMP:
                keys[Mario.KEY_RIGHT] = true;
                keys[Mario.KEY_SPEED] = false;
                keys[Mario.KEY_JUMP] = true;
                break;
            case FAST_JUMP:
                keys[Mario.KEY_RIGHT] = true;
                keys[Mario.KEY_SPEED] = true;
                keys[Mario.KEY_JUMP] = true;
                break;

            default:
                throw new RuntimeException("Undefined Action ID:" + id);
        }
        return keys;
    }

    public int getNumberOfID()
    {
        return 4;
    }
}
