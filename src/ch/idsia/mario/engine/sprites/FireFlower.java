package ch.idsia.mario.engine.sprites;

import ch.idsia.mario.engine.Art;
import ch.idsia.mario.engine.LevelScene;


public class FireFlower extends Sprite
{
    private int width = 4;
    int height = 24;

    public int facing;

    public boolean avoidCliffs = false;
    private int life;

    private FireFlower(LevelScene world)
    {
        super(world);
    }
    
    public FireFlower(LevelScene world, int x, int y)
    {
        super(world);
        kind = KIND_FIRE_FLOWER;
        sheet = Art.items;

        this.x = x;
        this.y = y;
        xPicO = 8;
        yPicO = 15;

        xPic = 1;
        yPic = 0;
        height = 12;
        facing = 1;
        wPic  = hPic = 16;
        life = 0;
    }

    public void collideCheck()
    {
        float xMarioD = world.mario.x - x;
        float yMarioD = world.mario.y - y;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < world.mario.height)
            {
                world.mario.getFlower();
                world.removeSprite(this);
            }
        }
    }

    public void move()
    {
        if (life<9)
        {
            layer = 0;
            y--;
            life++;
            return;
        }
    }

    public Object makeTrainingCopy(Object parent)
    {
        FireFlower result = new FireFlower((LevelScene) parent);
        Sprite.makeTrainingCopy(parent, result, this);

        result.width = width;
        result.height = height;
        result.facing = facing;
        result.avoidCliffs = avoidCliffs;
        result.life = life;

        return result;
    }

}