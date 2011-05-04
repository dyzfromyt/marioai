/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.engine.sprites.Sprite;
import ch.idsia.mario.environments.Environment;
import cs229.programsearch.EvalEnv;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GPAgent implements Agent
{
    String agentName ="GPAgent";
    public GPTree tree;
    EvalEnv env;
    boolean[] keys;
    public void reset()
    {
        keys = new boolean[Environment.numberOfButtons];
        env = genExampleEnv();
        for (int i = 0; i < keys.length; i++)
        {
            keys[i] = false;
        }
    }

    public GPAgent()
    {
        reset();
    }
    public static EvalEnv convObs(Environment observation)
    {
        // 2 possible conversion mode:
        // 1: should have mario states, 22x22 mario Complete Obs ,
        // 2: should have mario states, 22x22 mario TerrainObs + 5 nearest enemies in front
        //  type system double as semantics meaning system: boolean: mario states, int: TerrainObs, float: enemy
        
        EvalEnv result = new EvalEnv();
        return result;
    }

    public static List<Point2D> convertFromEnemyFloat(float [] enemy, float [] mario)
    {
        List<Point2D> result = new ArrayList<Point2D> ();
        float mariox = mario[0];
        float marioy = mario[1];
        for(int i = 0 ; i < enemy.length ; i+=3)
        {
            Point2D newEntry = new Point2D();
            // convert to relative position
            newEntry.enemyKind = (int) enemy[i];
            newEntry.x = enemy[i + 1]- mariox;
            newEntry.y = enemy[i + 2]- marioy;

            newEntry.distance = newEntry.dist(mariox, marioy);
            if(newEntry.x < 0)
            {
                // enemy on the left of mario
                // triple its distance to decrease its importance
                newEntry.distance = newEntry.distance *3;
            }
            result.add(newEntry);
        }
        // sort the min distance first
        Collections.sort(result);
        return result;
    }



    // selector method...
    public static EvalEnv genExampleEnv()
    {
        return genExampleEnv3();
        //return genExampleEnv2();
    }
    public static void convObs(Environment observation, EvalEnv result)
    {
        convObs4(observation, result); // reduced cell obs
        //convObs3(observation, result);
        //convObs2(observation, result);
    }
    // 2 possible conversion mode:
    // 1: should have mario states, 22x22 mario Complete Obs ,
    // 2: should have mario states, 22x22 mario TerrainObs + 5 nearest enemies in front
    //  type system double as semantics meaning system: boolean: mario states, int: TerrainObs, float: enemy
    public static EvalEnv genExampleEnv2()
    {
        EvalEnv result = new EvalEnv();
        result.createVariable(Boolean.class, true);
        result.createVariable(Boolean.class, true);
        for (int x = 0; x < Environment.HalfObsWidth * 2; x++)
        {
            for (int y = 0; y < Environment.HalfObsHeight * 2; y++)
            {
                result.createVariable(Integer.class, 0);
            }
        }

        for (int i = 0; i < 5; i++)
        {
            result.createVariable(Integer.class, -1);
            result.createVariable(Float.class, -100.0);
            result.createVariable(Float.class, 10.0);
        }
        return result;
    }

    public static void convObs2(Environment observation, EvalEnv result)
    {
        int varID = 0;
        result.setVariableValue(varID++, observation.mayMarioJump());
        result.setVariableValue(varID++, observation.isMarioOnGround());
        byte[][] obsState = observation.getLevelSceneObservationZ(1);
        for(int x = 0 ; x < Environment.HalfObsWidth *2 ; x++)
        {
            for (int y = 0; y < Environment.HalfObsHeight * 2; y++)
            {
                result.setVariableValue(varID++, (int)obsState[x][y]);
            }
        }
        
        float [] marioCoord = observation.getMarioFloatPos();
        float [] enemyCoord = observation.getEnemiesFloatPos();
        List<Point2D> relativePos = convertFromEnemyFloat(enemyCoord, marioCoord);
        for(int i = 0 ; i < 5; i ++)
        {
            if(i >= relativePos.size())
            {
                result.setVariableValue(varID++, -1);
                result.setVariableValue(varID++, (float)-100.0);
                result.setVariableValue(varID++, (float)10.0);
            }
            else
            {
                result.setVariableValue(varID++, (int) relativePos.get(i).enemyKind);
                result.setVariableValue(varID++, relativePos.get(i).x);
                result.setVariableValue(varID++, relativePos.get(i).y);
            }
        }

    }

    public static EvalEnv genExampleEnv3()
    {
        EvalEnv result = new EvalEnv();
        result.createVariable(Boolean.class, true);
        result.createVariable(Boolean.class, true);
        int leftBorder = Environment.HalfObsWidth - 2;
        int rightBorder = Environment.HalfObsWidth + 4;
        int topBorder = Environment.HalfObsHeight - 2;
        int bottomBorder = Environment.HalfObsHeight + 4;

        for (int x = leftBorder; x < rightBorder; x++)
        {
            for (int y = topBorder; y < bottomBorder; y++)
            {
                result.createVariable(Integer.class, 0);
            }
        }

        for (int i = 0; i < 5; i++)
        {
            result.createVariable(Integer.class, -1);
            result.createVariable(Float.class, -100.0);
            result.createVariable(Float.class, 10.0);
        }
        return result;
    }

    public static void convObs3(Environment observation, EvalEnv result)
    {
        int varID = 0;
        result.setVariableValue(varID++, observation.mayMarioJump());
        result.setVariableValue(varID++, observation.isMarioOnGround());
        byte[][] obsState = observation.getLevelSceneObservationZ(1);
        
        int leftBorder = Environment.HalfObsWidth - 2;
        int rightBorder = Environment.HalfObsWidth + 4;
        int topBorder = Environment.HalfObsHeight - 2;
        int bottomBorder = Environment.HalfObsHeight + 4;

        for (int x = leftBorder; x < rightBorder; x++)
        {
            for (int y = topBorder; y < bottomBorder; y++)
            {
                result.setVariableValue(varID++, (int) obsState[x][y]);
            }
        }

        float[] marioCoord = observation.getMarioFloatPos();
        float[] enemyCoord = observation.getEnemiesFloatPos();
        List<Point2D> relativePos = convertFromEnemyFloat(enemyCoord, marioCoord);
        for (int i = 0; i < 5; i++)
        {
            if (i >= relativePos.size())
            {
                result.setVariableValue(varID++, -1);
                result.setVariableValue(varID++, (float) -100.0);
                result.setVariableValue(varID++, (float) 10.0);
            }
            else
            {
                result.setVariableValue(varID++, (int) relativePos.get(i).enemyKind);
                result.setVariableValue(varID++, relativePos.get(i).x);
                result.setVariableValue(varID++, relativePos.get(i).y);
            }
        }

    }
    public static void convObs4(Environment observation, EvalEnv result)
    {
        int varID = 0;
        result.setVariableValue(varID++, observation.mayMarioJump());
        result.setVariableValue(varID++, observation.isMarioOnGround());
        byte[][] obsState = observation.getLevelSceneObservationZ(1);

        int leftBorder = Environment.HalfObsWidth - 2;
        int rightBorder = Environment.HalfObsWidth + 4;
        int topBorder = Environment.HalfObsHeight - 2;
        int bottomBorder = Environment.HalfObsHeight + 4;

        for (int x = leftBorder; x < rightBorder; x++)
        {
            for (int y = topBorder; y < bottomBorder; y++)
            {
                result.setVariableValue(varID++, (int) convertToFeatureCell(obsState[x][y]));
            }
        }

        float[] marioCoord = observation.getMarioFloatPos();
        float[] enemyCoord = observation.getEnemiesFloatPos();
        List<Point2D> relativePos = convertFromEnemyFloat(enemyCoord, marioCoord);
        for (int i = 0; i < 5; i++)
        {
            if (i >= relativePos.size())
            {
                result.setVariableValue(varID++, -1);
                result.setVariableValue(varID++, (float) -100.0);
                result.setVariableValue(varID++, (float) 10.0);
            }
            else
            {
                result.setVariableValue(varID++, (int) relativePos.get(i).enemyKind);
                result.setVariableValue(varID++, relativePos.get(i).x);
                result.setVariableValue(varID++, relativePos.get(i).y);
            }
        }

    }
    
    private static byte convertToFeatureCell(byte input)
    {
        byte result = input;
        switch (result)
        {
            case 16:// breakable brick
                result = 1;
                break;
            case 21: // brick with question
                result = 1;
                break;
            case -10: //border
                result = 1;
                break;
            case -11: // half border
                result = 2;
                break;
            case 20: //fower pot
                result = 1;
                break;
            case Sprite.KIND_FIREBALL:
            case Sprite.KIND_GOOMBA:
            case Sprite.KIND_SPIKY:
            case (Sprite.KIND_BULLET_BILL):
            case (Sprite.KIND_GOOMBA_WINGED):
            case (Sprite.KIND_GREEN_KOOPA):
            case (Sprite.KIND_GREEN_KOOPA_WINGED):
            case (Sprite.KIND_RED_KOOPA):
            case (Sprite.KIND_RED_KOOPA_WINGED):
            case (Sprite.KIND_SHELL):
            case (Sprite.KIND_ENEMY_FLOWER):
            case (Sprite.KIND_SPIKY_WINGED):
            case 1: // enemy
                result = 3;
                break;
            case Sprite.KIND_FIRE_FLOWER:
            case Sprite.KIND_MUSHROOM:
            case 0: // nothing interesting
                result = 0;
                break;

            default:
                result = 0;
            // TODO: handle the unknown state cells
            //throw new RuntimeException("unknown state cell code: " + result);
        }
        return result;
    }

    public boolean[] getAction(Environment observation)
    {
        convObs(observation, env);
        boolean shouldJump = (Boolean)tree.root.getValue(env);

        keys[Mario.KEY_JUMP] = shouldJump;
        keys[Mario.KEY_RIGHT] = true;
        keys[Mario.KEY_SPEED] = true;
        return keys;
    }

    public AGENT_TYPE getType()
    {
        return AGENT_TYPE.AI;
    }

    public String getName()
    {
        return agentName;
    }

    public void setName(String name)
    {
        agentName = name;
    }

}
