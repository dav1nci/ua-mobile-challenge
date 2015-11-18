package org.reaction.objects;

import android.graphics.Bitmap;

import java.util.Random;

public class Enemy extends Figure
{
    private int[] vector = new int[2];
    final Random rnd = new Random();
    private int difficult;
    private int speed;
    private int deltaSpeed;

    public Enemy(Bitmap image, int screenWidth, int screenHeight, int x, int y, int height, int width, int difficult) {
        super(image, screenWidth, screenHeight, height, width);
        this.x = x;
        this.y = y;
        this.difficult = difficult;
        //Log.d("sad", " " + screenHeight + " " + screenWidth + " " + image.getHeight() + " "+ image.getWidth());
        deltaSpeed = (screenHeight*screenWidth) / (image.getHeight() * image.getWidth() * 15);
        speed = -5;
        changeDirection();
    }

    public void update(boolean speedRise)
    {
        if (speedRise) {
            speed += deltaSpeed;
            //Log.d("asd", String.valueOf(deltaSpeed));
            //Log.d("sa", "SPEED RISE!");
        }
        int sign = 1;
        if (vector[0] < 0)
            sign = -1;
        else if (vector[0] == 0)
            sign = 0;
        int sign1 = 1;
        if (vector[1] < 0)
            sign1 = -1;
        else if (vector[1] == 0)
            sign1 = 0;
        while ((x + vector[0] + speed * sign) < 0 || (x + vector[0] + speed * sign) > screenWidth - figureWidth  || (y + vector[1]+ speed * sign1) < 0 || (y + vector[1]+ speed * sign1) > screenHeight - figureHeight)
        {
            changeDirection();
            sign = 1;
            if (vector[0] < 0)
                sign = -1;
            else if (vector[0] == 0)
                sign = 0;
            sign1 = 1;
            if (vector[1] < 0)
                sign1 = -1;
            else if (vector[1] == 0)
                sign1 = 0;
        }
        //Log.d("vector[0], vector [1] ", " " + vector[0] + " " + vector[1]);

        x += vector[0] + speed * sign;
        //Log.d("X = ", String.valueOf(x));
        y += vector[1] + speed * sign1;
    }

    private void changeDirection()
    {
        vector[0] = rnd.nextInt(5);
        vector[1] = (int)(Math.sqrt(Math.pow((double)5, 2) - Math.pow((double)vector[0], 2)));
        if (rnd.nextBoolean())
            vector[0] = (-1)*vector[0];
        if (rnd.nextBoolean())
            vector[1] = (-1)*vector[1];
    }

    @Override
    public void update() {

    }
}
