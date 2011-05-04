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
public class ActionMappingMario1 implements ActionMapping
{

    public static final int IDLE = 0;
    public static final int RIGHT = 1;
    public static final int RIGHT_FAST = 2;
    public static final int LEFT = 3;
    public static final int LEFT_FAST = 4;
    public static final int JUMP = 5;
    public static final int RIGHT_FAST_JUMP = 6;
    public static final int LEFT_FAST_JUMP = 7;
    public static final int FIRE = 8;
    // we do not map the DOWN, "duck" key, because there is no way to observe that the mario is sliding...

    public int map(boolean[] keys)
    {
        return actionToID(keys);
    }

    public String getName()
    {
        return "ActionMappingMario1";
    }

    public int actionToID(boolean[] keys)
    {
        if(keys[Mario.KEY_LEFT] && keys[Mario.KEY_SPEED] && keys[Mario.KEY_JUMP])
        {
            return LEFT_FAST_JUMP;
        }
        if (keys[Mario.KEY_RIGHT] && keys[Mario.KEY_SPEED] && keys[Mario.KEY_JUMP])
        {
            return RIGHT_FAST_JUMP;
        }
        if (keys[Mario.KEY_JUMP])
        {
            return JUMP;
        }
        if (keys[Mario.KEY_RIGHT] && keys[Mario.KEY_SPEED] )
        {
            return RIGHT_FAST;
        }
        if (keys[Mario.KEY_LEFT] && keys[Mario.KEY_SPEED])
        {
            return LEFT_FAST;
        }
        if (keys[Mario.KEY_SPEED])
        {
            return FIRE;
        }
        if (keys[Mario.KEY_RIGHT])
        {
            return RIGHT;
        }
        if (keys[Mario.KEY_LEFT])
        {
            return LEFT;
        }
        return IDLE;
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
                break;
            case RIGHT:
                keys[Mario.KEY_RIGHT] = true;
                break;
            case RIGHT_FAST:
                keys[Mario.KEY_RIGHT] = true;
                keys[Mario.KEY_SPEED] = true;
                break;
            case LEFT:
                keys[Mario.KEY_LEFT] = true;
                break;
            case LEFT_FAST:
                keys[Mario.KEY_LEFT] = true;
                keys[Mario.KEY_SPEED] = true;
                break;
            case JUMP:
                keys[Mario.KEY_JUMP] = true;
                break;
            case RIGHT_FAST_JUMP:
                keys[Mario.KEY_RIGHT] = true;
                keys[Mario.KEY_SPEED] = true;
                keys[Mario.KEY_JUMP] = true;
                break;
            case LEFT_FAST_JUMP:
                keys[Mario.KEY_LEFT] = true;
                keys[Mario.KEY_SPEED] = true;
                keys[Mario.KEY_JUMP] = true;
                break;
            case FIRE:
                keys[Mario.KEY_SPEED] = true;
                break;
            default:
                throw new RuntimeException("Undefined Action ID");
        }
        return keys;
    }

    public int getNumberOfID()
    {
        return 9;
    }
}
