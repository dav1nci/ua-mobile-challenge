package org.reaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import org.reaction.objects.Hero;
import org.reaction.objects.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Manager implements Runnable {
    private boolean runFlag = true;
    private boolean suspendFlag = false;
    private boolean isSpeedRise = false;
    private Bitmap heroImage;
    private Bitmap enemyImage;
    private Hero hero;
    private List<Enemy> enemies = new ArrayList<>(4);
    private int screenWidth;
    private int screenHeight;
    private long score;
    private long currentTime; //it is possible that the variable is not needed, but I thought that each time getting here this way (System.currentTimeMillis()) will be more resource-intensive
    private int difficult;
    private long periodOfSpeedRising;
    private long periodStartPoint;//starting point start of the period
    private String pauseResume = "Pause";
    private final SurfaceHolder surfaceHolder;
    private Context context;
    private Paint paintStyle;
    private Canvas canvas;
    Thread t;

    public Manager(SurfaceHolder surfaceHolder, Context context, int difficult)
    {
        this.surfaceHolder = surfaceHolder;
        this.difficult = difficult;
        this.context = context;

        //count period of speed rise up with this formula. For example with game difficult 1 - period 27 seconds, difficult 5 - 3 seconds
        this.periodOfSpeedRising = (30 - (difficult + (difficult-1)) * 3) * 1000;
        initOthers();// init other objects and variables
        t = new Thread(this, "Drawing");
        t.start();
    }



    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() throws ArithmeticException
    {
        score = System.currentTimeMillis();
        while (runFlag) {
            canvas = null;
            try
            {
                /*synchronized (this)
                {
                    while (suspendFlag)//actually don't work, I don't know why
                        wait();*/
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder)
                    {
                        while (suspendFlag) {// do this bullshit because of previous commented strings of code don't work =(
                        }
                        canvas.drawColor(Color.BLACK);
                        currentTime = System.currentTimeMillis();
                        updatePositions();//update coordinates of objects
                        isSpeedRise = false; // after rising speed up, return value to false position
                        //runFlag = !(isCrash());//check if player crushed into wall or in other enemy objects
                        if (!runFlag) {
                            finishGame();//if he do, finish this game
                            //break;
                        }
                        //-----draw all updated objects include string Pause/Resume --------
                        drawObjects();
                        Thread.sleep(20);
                    }
                //}
            }
            catch (Exception e)
            {
                Log.d("Interrupted Exception", "HERE!!");
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

    private void updatePositions()
    {
        if (riseSpeed(currentTime - periodStartPoint)) { //check if it need to speed rise up
            isSpeedRise = true;
            periodStartPoint = currentTime;
            Log.d("qwe", "Speed Rise UP");
        }
        hero.update();
        for (Enemy enemy : enemies)
            enemy.update(isSpeedRise);
    }

    private void drawObjects()
    {
        hero.draw(canvas);
        for (Enemy enemy : enemies)
            enemy.draw(canvas);
        canvas.drawText(String.valueOf(currentTime - score), 0, 30, paintStyle);
        canvas.drawText(pauseResume, screenWidth - 100, 30, paintStyle);
    }

    private boolean isCrash()
    {
        //--------check if player crushed into the one of side of monitor-----
        if ( hero.getX() < 1
                || hero.getX() > screenWidth - heroImage.getWidth()
                || hero.getY() < 1
                || hero.getY() > screenHeight - heroImage.getHeight()) {
            return true;
        }
        //----------check if player crushed into the one of enemy objects-----
        for (Enemy enemy : enemies)
        {
            if (Math.abs(hero.getX() - enemy.getX()) < enemyImage.getWidth() - 20)// 20 because the images are not squares, they are circles or ovals, and a collision is considered for squares
            {
                if (Math.abs(hero.getY() -  enemy.getY()) < enemyImage.getHeight() - 30)
                    return true;
            }
        }
        return false;
    }

    private void finishGame()
    {
        currentTime = System.currentTimeMillis();
        Intent intent = new Intent(context, FinishGame.class);
        intent.putExtra("score", String.valueOf(currentTime - score));//send to finish game activity variable score
        intent.putExtra("difficult", String.valueOf(difficult));
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void initOthers()
    {
        //-----------This trick I used to get the object canvas and initialize variables
        // screenHeight and screenWidth ------------------------------------------------
        canvas = surfaceHolder.lockCanvas();
        screenHeight = canvas.getHeight();
        screenWidth = canvas.getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
        //----------------------------------------------------------------------------
        heroImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        int figureHeight = enemyImage.getHeight();
        int figureWidth = enemyImage.getWidth();
        hero = new Hero(heroImage, screenWidth, screenHeight, context, heroImage.getHeight(), heroImage.getWidth());

        //I try to initialize them in the loop, but they have different parameters
        enemies.add(new Enemy(enemyImage, screenWidth, screenHeight, 0, 0, figureHeight, figureWidth, difficult));
        enemies.add(new Enemy(enemyImage, screenWidth, screenHeight, screenWidth - figureWidth, 0, figureHeight, figureWidth, difficult));
        enemies.add(new Enemy(enemyImage, screenWidth, screenHeight, 0, screenHeight - figureHeight, figureHeight, figureWidth, difficult));
        enemies.add(new Enemy(enemyImage, screenWidth, screenHeight, screenWidth - figureWidth, screenHeight - figureHeight, figureHeight, figureWidth, difficult));

        //just initialize style and other settings to Pause/Resume string
        paintStyle = new Paint();
        paintStyle.setAntiAlias(true);
        paintStyle.setColor(Color.WHITE);
        paintStyle.setTextSize(35.0f);
        paintStyle.setStrokeWidth(2.0f);
        paintStyle.setStyle(Paint.Style.STROKE);
        paintStyle.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

        periodStartPoint = System.currentTimeMillis();
    }


    private boolean riseSpeed(long timer)//check if the speed need to be rising
    {
        return  (timer >= periodOfSpeedRising);
    }

    // getters and setters
    public void setPauseResume(String pauseResume) {
        this.pauseResume = pauseResume;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public String getPauseResume() {
        return pauseResume;
    }


    //I tried to implement multithreading like in Shildt, but it throws exceptions
    synchronized void mySuspend()
    {
        suspendFlag = true;
    }

    synchronized void myResume()
    {
        suspendFlag = false;
        notify();
    }
}
