package ch.idsia.mario.engine.sprites;

import ch.idsia.mario.engine.Art;
import ch.idsia.mario.engine.LevelScene;

public class Particle extends Sprite
{
    public int life;

    private Particle(LevelScene world)
    {
        super(world);
    }
    public Particle(LevelScene world, int x, int y, float xa, float ya)
    {
        this(world, x, y, xa, ya, (int)(Math.random()*2), 0);
    }

    public Particle(LevelScene world, int x, int y, float xa, float ya, int xPic, int yPic)
    {
        super(world);
        kind = KIND_PARTICLE;
        sheet = Art.particles;
        this.x = x;
        this.y = y;
        this.xa = xa;
        this.ya = ya;
        this.xPic = xPic;
        this.yPic = yPic;
        this.xPicO = 4;
        this.yPicO = 4;
        
        wPic = 8;
        hPic = 8;
        life = 10;
    }

    public void move()
    {
        if (life--<0) world.removeSprite(this);
        x+=xa;
        y+=ya;
        ya*=0.95f;
        ya+=3;
    }

    public Object makeTrainingCopy(Object parent)
    {
        Particle result = new Particle((LevelScene) parent);
        Sprite.makeTrainingCopy(parent, result, this);

        result.life = life;

        return result;
    }
}