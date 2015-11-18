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
import org.reaction.objects.Chip;
import org.reaction.objects.Enemy;

public class Manager implements Runnable {
    private boolean runFlag = true;
    private final SurfaceHolder surfaceHolder;
    private Bitmap hero;
    private Bitmap enemy;
    private Chip chip;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private int screenWidth;
    private int screenHeight;
    private Context context;
    private long score;
    private Paint paint;
    private long current_time;
    private Canvas canvas;
    private int difficult;
    private long period_of_speed_rising;
    private long timePass;
    private boolean rise = false;
    private String pauseResme;
    private boolean suspendFlag = false;
    Thread t;
    private long timeInGame = 0;//have no time for this, but i finish it in future


    public Manager(SurfaceHolder surfaceHolder, Context context, int difficult)
    {
        this.surfaceHolder = surfaceHolder;
        this.difficult = difficult;
        canvas = surfaceHolder.lockCanvas();
        screenHeight = canvas.getHeight();
        screenWidth = canvas.getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
        hero = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        int figureHeight = enemy.getHeight();
        int figureWidht = enemy.getWidth();
        chip = new Chip(hero, screenWidth, screenHeight, context, hero.getHeight(), hero.getWidth());
        enemy1 = new Enemy(enemy, screenWidth, screenHeight, 0, 0, figureHeight, figureWidht, difficult);
        enemy2 = new Enemy(enemy, screenWidth, screenHeight, screenWidth - figureWidht, 0, figureHeight, figureWidht, difficult);
        enemy3 = new Enemy(enemy, screenWidth, screenHeight, 0, screenHeight - figureHeight, figureHeight, figureWidht, difficult);
        enemy4 = new Enemy(enemy, screenWidth, screenHeight, screenWidth - figureWidht, screenHeight - figureHeight, figureHeight, figureWidht, difficult);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(35.0f);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
        this.difficult = difficult;
        this.period_of_speed_rising = (30 - (difficult + (difficult-1)) * 3) * 1000;
        timePass = score;
        pauseResme = "Pause";
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
                    while (suspendFlag)//actually don't work, i don't know why
                        wait();*/
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder)
                    {
                        while (suspendFlag) {
                        }
                        canvas.drawColor(Color.BLACK);
                        current_time = System.currentTimeMillis();
                        if (riseSpeed(current_time - timePass)) { //check if i need to rise up speed
                            rise = true;
                            timePass = current_time;
                        }
                        //----update coordinats of objects-----
                        chip.update();
                        enemy1.update(rise);
                        enemy2.update(rise);
                        enemy3.update(rise);
                        enemy4.update(rise);
                        //-------------------------------------
                        rise = false;
                        runFlag = !(isCrash());//check if player crushed into wall or in other ojects
                        if (!runFlag) {
                            current_time = System.currentTimeMillis();
                            Intent intent = new Intent(context, FinishGame.class);
                            intent.putExtra("score", String.valueOf(current_time - score));
                            intent.putExtra("difficult", String.valueOf(difficult));
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            break;
                        }
                        //-----draw all updated objects--------
                        chip.draw(canvas);
                        enemy1.draw(canvas);
                        enemy2.draw(canvas);
                        enemy3.draw(canvas);
                        enemy4.draw(canvas);
                        //--------------------------------------
                        //-----draw score and Pause/Resume------
                        canvas.drawText(String.valueOf(current_time - score), 0, 30, paint);
                        canvas.drawText(pauseResme, screenWidth - 100, 30, paint);
                        //--------------------------------------
                        Thread.sleep(20);
                    }
                //}
            }
            catch (Exception e)
            {
                Log.d("Interrupdet Exheption", "HERE!!");
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

    private boolean isCrash()
    {
        //--------check if player crush into one of side of monitor-----
        if ( chip.getX() < 1 || chip.getX() > screenWidth - hero.getWidth() || chip.getY() < 1 || chip.getY() > screenHeight - hero.getHeight()) {
            return true;
        }
        //-------------------------------------------------------------
        //----------check if player crushed in one of enemy objects-----
        if (Math.abs(chip.getX() - enemy1.getX()) < enemy.getWidth() - 20)
        {
            if (Math.abs(chip.getY() -  enemy1.getY()) < enemy.getHeight() - 30)
                return true;
        }
        if (Math.abs(chip.getX() - enemy2.getX()) < enemy.getWidth() - 20)
        {
            if (Math.abs(chip.getY() -  enemy2.getY()) < enemy.getHeight() - 30)
                return true;
        }
        if (Math.abs(chip.getX() - enemy3.getX()) < enemy.getWidth() - 20)
        {
            if (Math.abs(chip.getY()  - enemy3.getY()) < enemy.getHeight() - 30)
                return true;
        }
        if (Math.abs(chip.getX() - enemy4.getX()) < enemy.getWidth() - 20)
        {
            if (Math.abs(chip.getY()  - enemy4.getY()) < enemy.getHeight() - 30)
                return true;
        }
        //-----------------------------------------------------------------
        return false;
    }

    private boolean riseSpeed(long timer)
    {
        return  (timer >= period_of_speed_rising);
    }

    public void setPauseResme(String pauseResme) {
        this.pauseResme = pauseResme;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public String getPauseResme() {
        return pauseResme;
    }

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
