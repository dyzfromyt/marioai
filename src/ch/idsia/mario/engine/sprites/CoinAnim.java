package ch.idsia.mario.engine.sprites;

import ch.idsia.mario.engine.Art;
import ch.idsia.mario.engine.LevelScene;


public class CoinAnim extends Sprite
{
    private int life = 16;

    private CoinAnim(LevelScene world)
    {
        super(world);
    }
    public CoinAnim(LevelScene world, int xTile, int yTile)
    {
        super(world);
        kind = KIND_COIN_ANIM;
        sheet = Art.level;
        wPic = hPic = 16;

        x = xTile * 16;
        y = yTile * 16 - 16;
        xa = 0;
        ya = -6f;
        xPic = 0;
        yPic = 2;
    }

    public void move()
    {
        if (life-- < 0)
        {
            world.removeSprite(this);
            for (int xx = 0; xx < 2; xx++)
                for (int yy = 0; yy < 2; yy++)
                    world.addSprite(new Sparkle(world, (int)x + xx * 8 + (int) (Math.random() * 8), (int)y + yy * 8 + (int) (Math.random() * 8), 0, 0, 0, 2, 5));
        }

        xPic = life & 3;

        x += xa;
        y += ya;
        ya += 1;
    }

    public Object makeTrainingCopy(Object parent)
    {
        CoinAnim result = new CoinAnim((LevelScene) parent);
        Sprite.makeTrainingCopy(parent, result, this);

        result.life = life;

        return result;
    }
}