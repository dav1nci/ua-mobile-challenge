package org.reaction.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Chip extends Figure
{
    private static final double koeficient = 5;

    private float xy_angle;
    private float xz_angle;
    private float zy_angle;

    protected SensorManager mSensorManager;

    public Chip(Bitmap image, int screenWidth, int screenHeight, Context context, int figureHeight, int figureWidth)
    {
        super(image, screenWidth, screenHeight, figureHeight, figureWidth);
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(listener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        x = screenWidth/2;
        y = screenHeight/2;
    }

    private SensorEventListener listener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent e) {
            if (e.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                xy_angle = e.values[0];
                xz_angle = e.values[1];
                zy_angle = e.values[2];
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };


    public void update()
    {
        x -= xy_angle * koeficient;
        y += xz_angle * koeficient;
    }

}
