package ch.idsia.mario.engine.level;

import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.sprites.Enemy;
import ch.idsia.mario.engine.sprites.FlowerEnemy;
import ch.idsia.mario.engine.sprites.Sprite;
import cs229.common.Copyable;

public class SpriteTemplate implements Copyable
{
    public int lastVisibleTick = -1;
    //public Sprite sprite;
    public boolean isDead = false;
    public boolean isInstantiated = false;
    private boolean winged;

    public int thisX;
    public int thisY;

    public int getType() {
        return type;
    }

    private int type;
    
    public Object makeTrainingCopy(Object parent)
    {
        SpriteTemplate result = new SpriteTemplate();
        result.lastVisibleTick = lastVisibleTick;
        result.isDead = isDead;
        result.winged = winged;
        result.thisX = thisX;
        result.thisY = thisY;
        result.isInstantiated = isInstantiated;
        result.type = type;
        return result;

    }
    protected SpriteTemplate()
    {

    }
    public SpriteTemplate(int type, boolean winged)
    {
        this.type = type;
        this.winged = winged;
    }
    
    public void spawn(LevelScene world, int x, int y, int dir)
    {
        if (isDead) return;
        this.isInstantiated = true;

        Sprite sprite = null;
        if (type==Enemy.ENEMY_FLOWER)
        {
            sprite = new FlowerEnemy(world, x*16+15, y*16+24, x, y);
        }
        else
        {
//            sprite = new Enemy(world, x*16+8, y*16+15, dir, type, winged);
            sprite = new Enemy(world, x*16+8, y*16+15, dir, type, winged, x, y);
        }

        sprite.templateX = thisX;
        sprite.templateY = thisY;
        world.addSprite(sprite);
    }


}