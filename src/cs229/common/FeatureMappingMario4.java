/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.common;

import ch.idsia.mario.engine.MarioComponent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.engine.sprites.Sprite;
import ch.idsia.mario.environments.Environment;

/**
 *
 * @author Stephen
 */
public class FeatureMappingMario4 implements FeatureMapping
{

    public static int gapThreshold = 3; // the gap size that mario should avoid
    // offset for 6 directly sampled cells
    public static int[] offsetX =
    {
        0, -1, 1
    };
    public static int[] offsetY =
    {
        0, 0, 0
    };
    //  6  7  8   9 10  11

    public Boolean platformCheck(byte[][] state, int anchorX, int anchorY)
    {
        int jumpHeight = 3;
        for (int y = anchorY - 1; y >= anchorY - jumpHeight && y >= 0; y--)
        {
            if (state[anchorX][y] == 1 || state[anchorX][y] == 2)
            {
                return true;
            }
        }
        return false;
    }

    // true if has hole
    public Boolean holeCheck(byte[][] state, int anchorX, int anchorY)
    {
        for (int y = anchorY + 1; y < Environment.HalfObsHeight * 2; y++)
        {
            if (state[anchorX][y] == 1 || state[anchorX][y] == 2)
            {
                return false;
            }
        }
        //System.out.println("hole");
        return true;
    }

    public int terrainCheck(byte[][] state, int anchorX, int anchorY)
    {
        // anchorX is column of interest
        // anchorY is Mario's y pos
        // return 0 if normal, 1 if mario can jump onto something, 2 if jump down, 3 if hole
        // may need to special case mario's column, should just be a boolean for half-border check
        // (0,0) is top left corner

        int jumpHeight = 3;
        for (int y = anchorY - 1; y >= anchorY - jumpHeight && y >= 0; y--)
        {
            if (state[anchorX][y] == 1 || state[anchorX][y] == 2)
            {
                return 1;
            }
        }

        if (state[anchorX][anchorY + 1] == 1 || state[anchorX][anchorY + 1] == 2) // ground, nothing above
        {
            return 0;
        }
        else if (state[anchorX][anchorY + 1] == 0)
        {
            for (int y = anchorY + 2; y < Environment.HalfObsHeight * 2; y++)
            {
                if (state[anchorX][y] == 1 || state[anchorX][y] == 2)
                {
                    return 2;
                }
            }
            //System.out.println("hole");
            return 3; // hole
        }
        // ignore above + hole + no ground case
        System.out.println("ahh");
        return 0; // shouldn't happen
    }

    public int largeGapDistance(byte[][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        int gapSize = 0;
        for (int x = anchorX; x < Environment.HalfObsWidth * 2; x++)
        {
            // type 1,2,3,4 is what mario can stand on safely
            if (state[x][anchorY] > 0 && state[x][anchorY] < 5)
            {
                distance++;
                gapSize = 0; // reset gap size, ignore previous small gaps, assume mario can jump over it
                continue;
            }
            if (state[x][anchorY] == 0)
            {
                gapSize++;
                if (gapSize >= gapThreshold)
                {
                    return distance;
                }
            }
        }

        return approxLog(distance);
    }

    public int gapDistance(byte[][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        for (int x = anchorX; x < Environment.HalfObsWidth * 2; x++)
        {
            // type 1,2,3,4 is what mario can stand on safely
            if (state[x][anchorY] > 0 && state[x][anchorY] < 5)
            {
                distance++;
                continue;
            }
            break;
        }
        return approxLog(distance);
    }

    public int approxLog(int distance)
    {
        // collapse the range of distance, similar to log function
        if (distance >= 5)
        {
            distance = 3;
        }
        else if (distance >= 2)
        {
            distance = 2;
        }
        return distance;
    }

    public int enemyDistance(byte[][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        // ignore obstacles for now
        for (int x = anchorX; x < Environment.HalfObsWidth * 2; x++)
        {
            if (state[x][anchorY] == 3)
            {
                break;
            }
            distance++;
        }
        return approxLog(distance);
    }

    // get how far ahead are clear to run
    public int clearDistance(byte[][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        for (int x = anchorX; x < Environment.HalfObsWidth * 2; x++)
        {
            if (state[x][anchorY] == 0)
            {
                distance++;
                continue;
            }
            break;
        }
        return approxLog(distance);
    }
    //public static int [] featureElementRange={2,2, 4,4,4,4, 4,4,4,4,4,4};
    public static int[] featureElementRange =
    {
        2, 2,
        2, 2, 2, 2, 2,
        4, 4, 4,
        4, 4, 4// mario cell 13, right cell 15
    };

    public int getNumberOfID()
    {
        int state = 1;
        for (int i = 0; i < featureElementRange.length; i++)
        {
            state *= featureElementRange[i];
        }
        return state;
    }

    public int featureToID(byte[] feature)
    {
        int state = 0;
        for (int i = featureElementRange.length - 1; i >= 0; i--)
        {
            // right most bit is the most significant bit...
            state += feature[i];
            if (i > 0)
            {
                state *= featureElementRange[i - 1];
            }
        }
        return state;
    }

    public byte[] idToFeature(int id)
    {
        byte[] result = new byte[featureElementRange.length];
        for (int i = 0; i < featureElementRange.length; i++)
        {
            result[i] = (byte) (id % featureElementRange[i]);
            id = id / featureElementRange[i];
        }
        return result;
    }

    public int getFeatureLength()
    {
        return featureElementRange.length;
    }

    public byte[] map(Environment observation)
    {
        byte[][] state = observation.getCompleteObservation();
        //byte[][] state = observation.getLevelSceneObservation();
        int halfObsWidth = Environment.HalfObsWidth;
        int halfObsHeight = Environment.HalfObsHeight;
        int marioMode = observation.getMarioMode();
        boolean mayMarioJump = observation.mayMarioJump();
        boolean isMarioOnGround = observation.isMarioOnGround();
        return map(state, halfObsWidth, halfObsHeight, marioMode, mayMarioJump, isMarioOnGround);
    }
    private int holeCheckStart = -1;
    private final int NumHoleCheck = 5;

    private int enemyDistStart = -1;
    private static final int NumEnemyDistStart = 3;

    public byte[] map(byte[][] state, int halfObsWidth, int halfObsHeight, int marioMode, boolean mayMarioJump, boolean isMarioOnGround)
    {
        byte[] result = new byte[featureElementRange.length];
        int marioPosX = halfObsWidth;
        int marioPosY = halfObsHeight;

        int counter = 0;

        // convert all state cell code to feature cell code
        for (int x = 0; x < halfObsWidth * 2; x++)
        {
            for (int y = 0; y < halfObsHeight * 2; y++)
            {
                state[x][y] = convertToFeatureCell(state[x][y]);
            }
        }

        if (marioMode == 0)
        {
            // TO TRY, small mario, cannot break bricks, treat breakable brick differently?
        }

        // IGNORE: feature space: 2 2 4^4 4^6
        // NEW: feature space 2 2 4^4 4^3 4
        result[counter++] = (byte) (mayMarioJump ? 1 : 0); //0
        result[counter++] = (byte) (isMarioOnGround ? 1 : 0); //1
        //result[counter++]=(byte)clearDistance(state, marioPosX+1, marioPosY+2);
//        int pc1 = platformCheck(state, marioPosX, marioPosY)?1:0;
//        int pc2 = platformCheck(state, marioPosX+1, marioPosY)?1:0;
//        int pc3 = platformCheck(state, marioPosX+2, marioPosY)?1:0;
//        int pc4 = platformCheck(state, marioPosX+3, marioPosY)?1:0;
//        result[counter++]=(byte)(pc1); //2
//        result[counter++]=(byte)(pc2); //3
//        result[counter++]=(byte) (pc3); //4
//        result[counter++]=(byte)(pc4); //5


        holeCheckStart = counter;
        for (int i = 0; i < NumHoleCheck; i++)
        {
            int hc1 = holeCheck(state, marioPosX + i, marioPosY) ? 1 : 0;
            result[counter++] = (byte) (hc1); //2
        }

        enemyDistStart = counter;
        for (int i = 0; i < NumEnemyDistStart ; i++)
        {
            result[counter++] = (byte) enemyDistance(state, marioPosX + 1, marioPosY+1-i);
        }
        
        //result[counter++]=(byte)enemyDistance(state, marioPosX, marioPosY-1); //11
        //result[counter++]=(byte)enemyDistance(state, marioPosX, marioPosY+1); //12

        //result[counter++] = state[marioPosX + 1][marioPosY]; // 13

        //System.out.println(pc1 + " " + pc2 + " " + pc3 + " " + pc4 + " " + hc1 + " " + hc2 + " " + hc3 + " " + hc4);
        //System.out.println(state[marioPosX][marioPosY] + " stateright " + state[marioPosX+1][marioPosY] + " stateup " + state[marioPosX][marioPosY-1] + " stateupright " + state[marioPosX+1][marioPosY-1]);

        // feature mapping 1
        //result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY+1); //2
        //result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY); //3
        //result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY-1); //4
        //result[counter++]=(byte)clearDistance(state, marioPosX+1, marioPosY-2); //5
        //result[counter++]=(byte)gapDistance(state, marioPosX+2, marioPosY-1);
        //result[counter++]=(byte)gapDistance(state, marioPosX+1, marioPosY-2);

        // copy the direct sampled cells
        for (int i = 0; i < offsetX.length; i++)
        {
            result[counter++] = state[marioPosX + offsetX[i]][marioPosY + offsetY[i]];
        }
        if (counter != featureElementRange.length)
        {
            throw new RuntimeException("wrong feature length");
        }
        return result;
    }

    private byte convertToFeatureCell(byte input)
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
    public static final int RIGHT_CELL = 12;
    public static final int MARIO_CELL = 10;

    public double reward(byte[] feature, MarioComponent trueState, int action)
    {
        double reward = 0;

        for (int i = holeCheckStart; i < holeCheckStart + NumHoleCheck; i++)
        {
            if (feature[i] == 0)
            {
                // if not a hole
                reward += 1;
            }
        }

        for (int i = enemyDistStart; i < enemyDistStart + NumEnemyDistStart; i++)
        {
            // reward for being away from enemy
            reward += feature[i];
        }
      
        
        // cannot jump, on groud, press jump = -3
        boolean actionJump = (action == ActionMappingMario3.SLOW_JUMP|| action == ActionMappingMario3.FAST_JUMP);
        if (actionJump)
        {
            reward -= 1;
        }

        if (feature[0] == 0 && feature[1] == 1 && actionJump)
        {
            reward = -3;
        }
        // cannot jump, in air, release press jump = -3
        if (feature[0] == 0 && feature[1] == 0 && !actionJump)
        {
            reward = -3;
        }

        //
        // debug
        //System.out.println(feature[2] + " " + feature[3] + " " + feature[4] + " " + feature[5] + " " + feature[6] + " " + feature[7] + " " + feature[8] + " " + feature[9] + " " + feature[13]);


        // if mario hit by something
        if (feature[MARIO_CELL] != 0)
        {
            //System.out.println("Mario hit by something");
            reward -= 100;
        }

        // enemy on the right
        //System.out.println("R:\t" + feature[9]);
        //System.out.println("RD:\t" + feature[12]);

        // blocked on the right
        if (feature[RIGHT_CELL] != 0)
        {
            //System.out.println("blocked ");
            reward -= 20;
            if (action == ActionMappingMario2.JUMP)
            {
                reward += 2;
            }
        }
        if (feature[RIGHT_CELL] == 3)
        {
            //System.out.println("Enemy on the right");
            reward -= 2;
        }

        // this is the next state value, not current state, so should not use it
//        if (trueState.levelScene.mario.getStatus() == Mario.STATUS_DEAD)
//        {
//            reward -= 100;
//        }

        //System.out.println(reward + " " + action);
        return reward;
    }

    public String getName()
    {
        return "FeatureMappingMario4";
    }
}
