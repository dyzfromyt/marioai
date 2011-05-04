package ch.idsia.mario.engine.sprites;

import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.LevelScene;
import ch.idsia.mario.engine.level.SpriteTemplate;

import cs229.common.Copyable;
import java.awt.*;

public abstract class Sprite implements Copyable
{
    public static final int KIND_NONE = 0;
    public static final int KIND_MARIO = -31;
    public static final int KIND_GOOMBA = 2;
    public static final int KIND_GOOMBA_WINGED = 3;
    public static final int KIND_RED_KOOPA = 4;
    public static final int KIND_RED_KOOPA_WINGED = 5;
    public static final int KIND_GREEN_KOOPA = 6;
    public static final int KIND_GREEN_KOOPA_WINGED = 7;
    public static final int KIND_BULLET_BILL = 8;
    public static final int KIND_SPIKY = 9;
    public static final int KIND_SPIKY_WINGED = 10;
//    public static final int KIND_ENEMY_FLOWER = 11;
    public static final int KIND_ENEMY_FLOWER = 12;
    public static final int KIND_SHELL = 13;
    public static final int KIND_MUSHROOM = 14;
    public static final int KIND_FIRE_FLOWER = 15;    
    public static final int KIND_PARTICLE = 21;
    public static final int KIND_SPARCLE = 22;
    public static final int KIND_COIN_ANIM = 20;
    public static final int KIND_FIREBALL = 25;

    public static final int KIND_UNDEF = -42;

    //public static SpriteContext spriteContext;
    public LevelScene world;
    public byte kind = KIND_UNDEF;
    
    public float xOld, yOld, x, y, xa, ya;
    public int mapX, mapY;
    
    public int xPic, yPic;
    public int wPic = 32;
    public int hPic = 32;
    public int xPicO, yPicO;
    public boolean xFlipPic = false;
    public boolean yFlipPic = false;
    public Image[][] sheet;
    public boolean visible = true;
    public int templateX, templateY;
    public int layer = 1;

    protected Sprite(LevelScene world)
    {
        this.world = world;
    }
    
    public void move()
    {
        x+=xa;
        y+=ya;
    }
    
    public void render(Graphics og, float alpha)
    {
        if (!visible) return;
        
//        int xPixel = (int)(xOld+(x-xOld)*alpha)-xPicO;
//        int yPixel = (int)(yOld+(y-yOld)*alpha)-yPicO;

        int xPixel = (int)x-xPicO;
        int yPixel = (int)y-yPicO;


        og.drawImage(sheet[xPic][yPic], xPixel+(xFlipPic?wPic:0), yPixel+(yFlipPic?hPic:0), xFlipPic?-wPic:wPic, yFlipPic?-hPic:hPic, null);
        if (GlobalOptions.Labels)
            og.drawString("" + xPixel + "," + yPixel, xPixel, yPixel);
    }
    
    public final void tick()
    {
        xOld = x;
        yOld = y;
        mapX = (int)(xOld / 16);
        mapY = (int)(yOld / 16);
        move();
    }

    public final void tickNoMove()
    {
        xOld = x;
        yOld = y;
    }

//    public float getX(float alpha)
//    {
//        return (xOld+(x-xOld)*alpha)-xPicO;
//    }
//
//    public float getY(float alpha)
//    {
//        return (yOld+(y-yOld)*alpha)-yPicO;
//    }

    public void collideCheck()
    {
    }

    public void bumpCheck(int xTile, int yTile)
    {
    }

    public boolean shellCollideCheck(Shell shell)
    {
        return false;
    }

    public void release(Mario mario)
    {
    }

    public boolean fireballCollideCheck(Fireball fireball)
    {
        return false;
    }

    public static void makeTrainingCopy(Object parent, Sprite result, Sprite source)
    {
        result.kind=source.kind;
        result.xOld = source.xOld;
        result.yOld = source.yOld;
        result.x = source.x;
        result.y = source.y;
        result.xa = source.xa;
        result.ya = source.ya;
        result.mapX = source.mapX;
        result.mapY = source.mapY;

        result.xPic = source.xPic;
        result.yPic = source.yPic;
        result.wPic = source.wPic;
        result.hPic = source.hPic;
        result.xPicO = source.xPicO;
        result.yPicO = source.yPicO;
        result.xFlipPic = source.xFlipPic;
        result.yFlipPic = source.yFlipPic;
        result.sheet = source.sheet;
        result.visible = source.visible;
        result.templateX = source.templateX;
        result.templateY = source.templateY;
        result.layer = source.layer;

    }
//    public Object makeTrainingCopy(Object parent)
//    {
//        Sprite result = new Sprite((LevelScene)parent);
//        makeTrainingCopy(parent, result, this);
//        return result;
//    }

    
}