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
    //the coordinates of the object can be changed in both the positive
    // and negative direction of the axes to update () method I need to
    // know what direction to change the coordinate for the sake of simplicity
    // I did sign a separate variable
    private int signX;
    private int signY;

    public Enemy(Bitmap image, int screenWidth, int screenHeight, int x, int y, int height, int width, int difficult) {
        super(image, screenWidth, screenHeight, height, width);
        this.x = x;
        this.y = y;
        this.difficult = difficult;
        deltaSpeed = (screenHeight*screenWidth) / (image.getHeight() * image.getWidth() * 15);
        speed = -5;//experimentally derived number which places into the formula for the optimal initial velocity =)
        changeDirection();
    }

    public void update(boolean speedRise)
    {
        if (speedRise)//if need to rise up speed
            speed += deltaSpeed;
        updateSign();
        //do this loop while vector direction of object will not lead them over the borders of screen
        while (stillWrongDirection())
        {
            changeDirection();
            updateSign();
        }

        x += vector[0] + speed * signX;
        y += vector[1] + speed * signY;
    }

    private boolean stillWrongDirection()
    {
        return (x + vector[0] + speed * signX) < 0
                || (x + vector[0] + speed * signX) > screenWidth - figureWidth
                || (y + vector[1]+ speed * signY) < 0
                || (y + vector[1]+ speed * signY) > screenHeight - figureHeight;
    }

    private void updateSign()
    {
        signX = 1;
        if (vector[0] < 0)
            signX = -1;
        else if (vector[0] == 0)
            signX = 0;
        signY = 1;
        if (vector[1] < 0)
            signY = -1;
        else if (vector[1] == 0)
            signY = 0;
    }

    private void changeDirection()
    {
        // The task said that after the collision with the wall,
        // the direction should be changed randomly,
        // not at the same angle,
        // so that the questions to the task, not to me, they was told I was done =)
        vector[0] = rnd.nextInt(10);
        vector[1] = (int)(Math.sqrt(Math.pow((double)10, 2) - Math.pow((double)vector[0], 2)));//standard Pythagorean theorem
        if (rnd.nextBoolean())
            vector[0] = (-1)*vector[0];
        if (rnd.nextBoolean())
            vector[1] = (-1)*vector[1];
    }
}
