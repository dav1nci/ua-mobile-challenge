package org.reaction.objects;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Figure extends Activity
{
    protected Bitmap image;
    protected int x;
    protected int y;
    protected int screenWidth, screenHeight;
    protected int figureHeight, figureWidth;

    public Figure(Bitmap image, int screenWidth, int screenHight, int figureHeight, int figureWidth)
    {
        this.image = image;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHight;
        this.figureHeight = figureHeight;
        this.figureWidth = figureWidth;
    }

    abstract public void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
}
