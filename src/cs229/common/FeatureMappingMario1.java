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
 * @author Administrator
 */
public class FeatureMappingMario1 implements FeatureMapping
{
    public static int gapThreshold=3; // the gap size that mario should avoid
    // offset for 6 directly sampled cells
    public static int[] offsetX =
    {
        -1, 1, 0, 0, 1, 1
    };
    public static int[] offsetY =
    {
        0, 0, 1, -1, 1, -1
    };
    //  6  7  8   9 10  11
    
    public int largeGapDistance(byte [][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        int gapSize = 0;
        for(int x=anchorX; x< Environment.HalfObsWidth*2 ; x++)
        {
            // type 1,2,3,4 is what mario can stand on safely
            if(state[x][anchorY]>0 &&state[x][anchorY]< 5)
            {
                distance ++;
                gapSize = 0; // reset gap size, ignore previous small gaps, assume mario can jump over it
                continue;
            }
            if(state[x][anchorY]==0)
            {
                gapSize++;
                if(gapSize >=gapThreshold)
                {
                    return distance;
                }
            }
        }

        return approxLog(distance);
    }
    public int gapDistance(byte [][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        for(int x=anchorX; x< Environment.HalfObsWidth*2 ; x++)
        {
            // type 1,2,3,4 is what mario can stand on safely
            if(state[x][anchorY]>0 &&state[x][anchorY]< 5)
            {
                distance ++;
                continue;
            }
            break;
        }
        return approxLog(distance);
    }
    public int approxLog(int distance)
    {
        // collapse the range of distance, similar to log function
        if(distance >= 5)
            distance =3;
        else if(distance >= 2)
            distance =2;
        return distance;
    }
    
    // get how far ahead are clear to run
    public int clearDistance(byte [][] state, int anchorX, int anchorY)
    {
        int distance = 0;
        for(int x=anchorX; x< Environment.HalfObsWidth*2 ; x++)
        {
            if(state[x][anchorY]==0)
            {
                distance ++;
                continue;
            }
            break;
        }
        return approxLog(distance);
    }


    public static int [] featureElementRange={2,2, 4,4,4,4, 4,4,4,4,4,4};
    public int getNumberOfID()
    {
        int state = 1;
        for(int i=0 ; i < featureLength; i++)
        {
            state *=featureElementRange[i];
        }
        return state;
    }

    public int featureToID(byte [] feature)
    {
        int state = 0;
        for(int i=featureLength-1 ; i >= 0; i--)
        {
            // right most bit is the most significant bit... 
            state += feature[i];
            if(i>0)
            {
                state *=featureElementRange[i-1];
            }
        }
        return state;
    }
    public byte[] idToFeature(int id)
    {
        byte[] result = new byte[featureLength];
        for(int i=0 ; i< featureLength; i++)
        {
            result[i] = (byte)(id % featureElementRange[i]);
            id = id / featureElementRange[i];
        }
        return result;
    }
    public int getFeatureLength()
    {
        return featureLength;
    }
    
    private static int featureLength = 12;


    public byte[] map(Environment observation)
    {
        byte[][] state = observation.getCompleteObservation();
        //byte[][] state = observation.getLevelSceneObservation();
        int halfObsWidth = Environment.HalfObsWidth;
        int halfObsHeight = Environment.HalfObsHeight;
        int marioMode = observation.getMarioMode();
        boolean mayMarioJump = observation.mayMarioJump();
        boolean isMarioOnGround = observation.isMarioOnGround();
        return map(state, halfObsWidth, halfObsHeight, marioMode, mayMarioJump , isMarioOnGround);
    }
    public byte[] map(byte[][] state, int  halfObsWidth, int halfObsHeight, int marioMode, boolean mayMarioJump, boolean isMarioOnGround)
    {
        byte[] result = new byte[featureLength];
        int marioPosX = halfObsWidth;
        int marioPosY = halfObsHeight;

        int counter = 0;

        // convert all state cell code to feature cell code
        for(int x=0; x< halfObsWidth*2 ; x++)
        {
            for(int y=0; y< halfObsHeight*2 ; y++)
            {
                state[x][y]= convertToFeatureCell(state[x][y]);
            }
        }
        
        if(marioMode == 0)
        {
            // TO TRY, small mario, cannot break bricks, treat breakable brick differently? 
        }

        // feature space: 2 2 4^4 4^6
        result[counter++]=(byte)(mayMarioJump?1:0); //0
        result[counter++]=(byte)(isMarioOnGround?1:0); //1
        //result[counter++]=(byte)clearDistance(state, marioPosX+1, marioPosY+2);
        result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY+1); //2
        result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY); //3
        result[counter++]=(byte)clearDistance(state, marioPosX+2, marioPosY-1); //4
        result[counter++]=(byte)clearDistance(state, marioPosX+1, marioPosY-2); //5
        //result[counter++]=(byte)gapDistance(state, marioPosX+2, marioPosY-1);
        //result[counter++]=(byte)gapDistance(state, marioPosX+1, marioPosY-2);

        // copy the direct sampled cells
        for (int i = 0; i < offsetX.length; i++)
        {
            result[counter++] = state[marioPosX + offsetX[i]][marioPosY + offsetY[i]];
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

    public double reward(byte[] feature, MarioComponent trueState, int action)
    {
        double reward = 0;
        if(action == ActionMappingMario1.RIGHT)
        {
            reward +=1;
        }
        if(action == ActionMappingMario1.RIGHT_FAST)
        {
            reward +=3;
        }

        if(action == ActionMappingMario1.JUMP)
        {
            reward -=1;
        }
        if(action == ActionMappingMario1.LEFT_FAST_JUMP)
        {
            reward -=1;
        }
        if(action == ActionMappingMario1.RIGHT_FAST_JUMP)
        {
            reward -=1;
        }

        // if blocked on right
        if(feature[7]!=0)
        {
            reward -=4;
        }
        // enemy on the right 
        if(feature[7]==3)
        {
            reward -=2;
        }
        // enemy above
        if(feature[9]==3)
        {
            reward -=2;
        }
        // enemy right and above
        if(feature[11]==3)
        {
            reward -=2;
        }

        if (trueState.levelScene.mario.getStatus() == Mario.STATUS_DEAD)
        {
            reward -= 100; // comment out for now
        }

        // death is not handled here
        return reward;
    }

    public String getName()
    {
        return "FeatureMappingMario1";
    }

}
