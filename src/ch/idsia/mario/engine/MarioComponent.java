package ch.idsia.mario.engine;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.human.CheaterKeyboardAgent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.GameViewer;
import ch.idsia.tools.tcp.ServerAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import cs229.common.*;
import cs229.mdp.MDPTraining;

public class MarioComponent extends JComponent implements Runnable, /*KeyListener,*/ FocusListener, Environment,Copyable
{
    private static final long serialVersionUID = 790878775993203817L;
    public static final int TICKS_PER_SECOND = GlobalOptions.ourFPS;
    //public static final int TICKS_PER_SECOND = 100000;

    protected boolean running = false;
    protected int width, height;
    protected GraphicsConfiguration graphicsConfiguration;
    //private Scene levelScene;
    protected boolean focused = false;

    protected int frame;
    int delay;
    Thread animator;

    protected int ZLevelEnemies = 1;
    protected int ZLevelScene = 1;

    public Object makeTrainingCopy(Object parent)
    {
        MarioComponent result = new MarioComponent(parent);
        
        result.running = running;
        result.width=width;
        result.height=height;
        //private GraphicsConfiguration graphicsConfiguration;
        result.levelScene = (LevelScene)levelScene.makeTrainingCopy(result);
        result.focused = focused;
        result.frame = frame;
        result.delay = delay;
        //Thread animator;
        result.ZLevelEnemies = ZLevelEnemies;
        result.ZLevelScene = ZLevelScene;
        //result.mario = levelScene.mario;

        return result;
    }
    public void setGameViewer(GameViewer gameViewer) {
        this.gameViewer = gameViewer;
    }

    private GameViewer gameViewer = null;

    protected Agent agent = null;
    protected CheaterKeyboardAgent cheatAgent = null;

    private KeyAdapter prevHumanKeyBoardAgent;
    //private Mario mario = null;
    public LevelScene levelScene = null;

    protected MarioComponent(Object parent)
    {
        
    }
    public MarioComponent(int width, int height) {
        adjustFPS();

        this.setFocusable(true);
        this.setEnabled(true);
        this.width = width;
        this.height = height;

        Dimension size = new Dimension(width, height);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setFocusable(true);

        if (this.cheatAgent == null)
        {
            this.cheatAgent = new CheaterKeyboardAgent();
            this.addKeyListener(cheatAgent);
        }        

        GlobalOptions.registerMarioComponent(this);
    }

    public void adjustFPS() {
        int fps = GlobalOptions.FPS;
        delay = (fps > 0) ? (fps >= GlobalOptions.InfiniteFPS) ? 0 : (1000 / fps) : 100;
        //delay = 0; // NEW
        //        System.out.println("Delay: " + delay);
    }

    public void paint(Graphics g) {
    }

    public void update(Graphics g) {
    }

    public void init() {
        graphicsConfiguration = getGraphicsConfiguration();
//        if (graphicsConfiguration != null) {
            Art.init(graphicsConfiguration);
//        }
    }

    public void start() {
        if (!running) {
            running = true;
            animator = new Thread(this, "Game Thread");
            animator.start();
        }
    }

    public void stop() {
        running = false;
    }

    public void run() {

    }

    public void resetMario(int marioMode)
    {
        levelScene.mario.resetStatic(marioMode);
    }
    public EvaluationInfo run1(int currentTrial, int totalNumberOfTrials) {
        running = true;
        adjustFPS();
        EvaluationInfo evaluationInfo = new EvaluationInfo();

        VolatileImage image = null;
        Graphics g = null;
        Graphics og = null;

        image = createVolatileImage(320, 240);
        g = getGraphics();
        og = image.getGraphics();

        if (!GlobalOptions.VisualizationOn) {
            String msgClick = "Vizualization is not available";
            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
            drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
        }

        addFocusListener(this);

        // Remember the starting time
        long tm = System.currentTimeMillis();
        long tick = tm;
        int marioStatus = Mario.STATUS_RUNNING;

        //mario = ((LevelScene) levelScene).mario;
        int totalActionsPerfomed = 0;
// TODO: Manage better place for this:
        levelScene.mario.resetCoins();

        while (/*Thread.currentThread() == animator*/ running) {
            // Display the next frame of animation.
//                repaint();

            MDPTraining.train(this);
            
            levelScene.tick();
            if (gameViewer != null && gameViewer.getContinuousUpdatesState())
                gameViewer.tick();

            float alpha = 0;

//            og.setColor(Color.RED);
            if (GlobalOptions.VisualizationOn) {
                og.fillRect(0, 0, 320, 240);
                levelScene.render(og, alpha);
            }

            if (agent instanceof ServerAgent && !((ServerAgent) agent).isAvailable()) {
                System.err.println("Agent became unavailable. Simulation Stopped");
                running = false;
                break;
            }

            boolean[] action = agent.getAction(this/*DummyEnvironment*/);
            if (action != null)
            {
                for (int i = 0; i < Environment.numberOfButtons; ++i)
                    if (action[i])
                    {
                        ++totalActionsPerfomed;
                        break;
                    }
            }
            else
            {
                System.err.println("Null Action received. Skipping simulation...");
                stop();
            }


            //Apply action;
//            levelScene.keys = action;
            ((LevelScene) levelScene).mario.keys = action;
            ((LevelScene) levelScene).mario.cheatKeys = cheatAgent.getAction(null);

            if (GlobalOptions.VisualizationOn) {

                String msg = "Agent: " + agent.getName();
                LevelScene.drawStringDropShadow(og, msg, 0, 7, 5);

                msg = "Selected Actions: ";
                LevelScene.drawStringDropShadow(og, msg, 0, 8, 6);

                msg = "";
                if (action != null)
                {
                    for (int i = 0; i < Environment.numberOfButtons; ++i)
                        msg += (action[i]) ? levelScene.keysStr[i] : "      ";
                }
                else
                    msg = "NULL";                    
                drawString(og, msg, 6, 78, 1);

                if (!this.hasFocus() && tick / 4 % 2 == 0) {
                    String msgClick = "CLICK TO PLAY";
//                    og.setColor(Color.YELLOW);
//                    og.drawString(msgClick, 320 + 1, 20 + 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 1);
                    drawString(og, msgClick, 160 - msgClick.length() * 4, 110, 7);
                }
                og.setColor(Color.DARK_GRAY);
                LevelScene.drawStringDropShadow(og, "FPS: ", 33, 2, 7);
                LevelScene.drawStringDropShadow(og, ((GlobalOptions.FPS > 99) ? "\\infty" : GlobalOptions.FPS.toString()), 33, 3, 7);

                msg = totalNumberOfTrials == -2 ? "" : currentTrial + "(" + ((totalNumberOfTrials == -1) ? "\\infty" : totalNumberOfTrials) + ")";

                LevelScene.drawStringDropShadow(og, "Trial:", 33, 4, 7);
                LevelScene.drawStringDropShadow(og, msg, 33, 5, 7);


                if (width != 320 || height != 240) {
                        g.drawImage(image, 0, 0, 640 * 2, 480 * 2, null);
                } else {
                    g.drawImage(image, 0, 0, null);
                }
            } else {
                // Win or Die without renderer!! independently.
                marioStatus = ((LevelScene) levelScene).mario.getStatus();
                if (marioStatus != Mario.STATUS_RUNNING)
                    stop();
            }
            // Delay depending on how far we are behind.
            if (delay > 0)
                try {
                    tm += delay;
                    Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }
            // Advance the frame
            frame++;
        }
//=========
        evaluationInfo.agentType = agent.getClass().getSimpleName();
        evaluationInfo.agentName = agent.getName();
        evaluationInfo.marioStatus = levelScene.mario.getStatus();
        evaluationInfo.livesLeft = levelScene.mario.lives;
        evaluationInfo.lengthOfLevelPassedPhys = levelScene.mario.x;
        evaluationInfo.lengthOfLevelPassedCells = levelScene.mario.mapX;
        evaluationInfo.totalLengthOfLevelCells = levelScene.level.getWidthCells();
        evaluationInfo.totalLengthOfLevelPhys = levelScene.level.getWidthPhys();
        evaluationInfo.timeSpentOnLevel = levelScene.getStartTime();
        evaluationInfo.timeLeft = levelScene.getTimeLeft();
        evaluationInfo.totalTimeGiven = levelScene.getTotalTime();
        evaluationInfo.numberOfGainedCoins = levelScene.mario.coins;
//        evaluationInfo.totalNumberOfCoins   = -1 ; // TODO: total Number of coins.
        evaluationInfo.totalActionsPerfomed = totalActionsPerfomed; // Counted during the play/simulation process
        evaluationInfo.totalFramesPerfomed = frame;
        evaluationInfo.marioMode = levelScene.mario.getMode();
        evaluationInfo.killsTotal = levelScene.mario.world.killedCreaturesTotal;
//        evaluationInfo.Memo = "Number of attempt: " + Mario.numberOfAttempts;
        if (agent instanceof ServerAgent && levelScene.mario.keys != null /*this will happen if client quits unexpectedly in case of Server mode*/)
            ((ServerAgent)agent).integrateEvaluationInfo(evaluationInfo);
        return evaluationInfo;
    }

    private void drawString(Graphics g, String text, int x, int y, int c) {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
        }
    }

    public void startLevel(long seed, int difficulty, int type, int levelLength, int timeLimit) {
        levelScene = new LevelScene(graphicsConfiguration, this, seed, difficulty, type, levelLength, timeLimit);
        levelScene = ((LevelScene) levelScene);
        levelScene.init();
        //this.mario = levelScene.mario;
    }

    public void levelFailed() {
//        levelScene = mapScene;
        levelScene.mario.lives--;
        stop();
    }

    public void focusGained(FocusEvent arg0) {
        focused = true;
    }

    public void focusLost(FocusEvent arg0) {
        focused = false;
    }

    public void levelWon() {
        stop();
//        levelScene = mapScene;
//        mapScene.levelWon();
    }

    public void toTitle() {
//        Mario.resetStatic();
//        levelScene = new TitleScene(this, graphicsConfiguration);
//        levelScene.init();
    }

    public List<String> getTextObservation(boolean Enemies, boolean LevelMap, boolean Complete, int ZLevelMap, int ZLevelEnemies) {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).LevelSceneAroundMarioASCII(Enemies, LevelMap, Complete, ZLevelMap, ZLevelEnemies);
        else {
            return new ArrayList<String>();
        }
    }

    public String getBitmapEnemiesObservation()
    {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).bitmapEnemiesObservation(1);
        else {
            //
            return new String();
        }                
    }

    public String getBitmapLevelObservation()
    {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).bitmapLevelObservation(1);
        else {
            //
            return null;
        }
    }

    // Chaning ZLevel during the game on-the-fly;
    public byte[][] getMergedObservationZ(int zLevelScene, int zLevelEnemies) {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).mergedObservation(zLevelScene, zLevelEnemies);
        return null;
    }

    public byte[][] getLevelSceneObservationZ(int zLevelScene) {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).levelSceneObservation(zLevelScene);
        return null;
    }

    public byte[][] getEnemiesObservationZ(int zLevelEnemies) {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).enemiesObservation(zLevelEnemies);
        return null;
    }

    public int getKillsTotal() {
        //return mario.world.killedCreaturesTotal;
        return levelScene.killedCreaturesTotal;
    }

    public int getKillsByFire() {
        //return mario.world.killedCreaturesByFireBall;
        return levelScene.killedCreaturesByFireBall;
    }

    public int getKillsByStomp() {
        //return mario.world.killedCreaturesByStomp;
        return levelScene.killedCreaturesByStomp;
    }

    public int getKillsByShell() {
        //return mario.world.killedCreaturesByShell;
        return levelScene.killedCreaturesByShell;
    }

    public byte[][] getCompleteObservation() {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).mergedObservation(this.ZLevelScene, this.ZLevelEnemies);
        return null;
    }

    public byte[][] getEnemiesObservation() {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).enemiesObservation(this.ZLevelEnemies);
        return null;
    }

    public byte[][] getLevelSceneObservation() {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).levelSceneObservation(this.ZLevelScene);
        return null;
    }

    public boolean isMarioOnGround() {
        return levelScene.mario.isOnGround();
    }

    public boolean mayMarioJump() {
        return levelScene.mario.mayJump();
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        if (agent instanceof KeyAdapter) {
            if (prevHumanKeyBoardAgent != null)
                this.removeKeyListener(prevHumanKeyBoardAgent);
            this.prevHumanKeyBoardAgent = (KeyAdapter) agent;
            this.addKeyListener(prevHumanKeyBoardAgent);
        }
    }

    public void setMarioInvulnerable(boolean invulnerable)
    {
        levelScene.mario.isMarioInvulnerable = invulnerable;
    }

    public void setPaused(boolean paused) {
        levelScene.paused = paused;
    }

    public void setZLevelEnemies(int ZLevelEnemies) {
        this.ZLevelEnemies = ZLevelEnemies;
    }

    public void setZLevelScene(int ZLevelScene) {
        this.ZLevelScene = ZLevelScene;
    }

    public float[] getMarioFloatPos()
    {
        return new float[]{
                    levelScene.mario.x, levelScene.mario.y};
    }

    public float[] getEnemiesFloatPos()
    {
        if (levelScene instanceof LevelScene)
            return ((LevelScene) levelScene).enemiesFloatPos();
        return null;
    }

    public int getMarioMode()
    {
        return levelScene.mario.getMode();
    }

    public boolean isMarioCarrying()
    {
        return levelScene.mario.carried != null;
    }
}