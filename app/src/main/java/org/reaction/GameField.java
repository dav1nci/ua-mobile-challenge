package org.reaction;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameField extends SurfaceView implements SurfaceHolder.Callback
{
    protected Manager manager;

    private int difficult;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        if (x > manager.getScreenWidth() - 100 && y < 100)//area to touch on screen
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                if (manager.getPauseResume().equals("Pause"))
                {
                    manager.setPauseResume("Resume");
                    manager.mySuspend();
                    Log.d("q", "Pause");
                    return true;
                }
                manager.setPauseResume("Pause");
                manager.myResume();
                return true;
            }
        }
        return false;
    }

    public GameField(Context context, int difficult) {
        super(context);
        getHolder().addCallback(this);
        this.difficult = difficult;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        manager = new Manager(getHolder(), getContext(), difficult);
        manager.setRunning(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        manager.setRunning(false);
        while (retry) {
            try {
                manager.t.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
