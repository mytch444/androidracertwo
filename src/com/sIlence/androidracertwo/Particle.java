package com.sIlence.androidracertwo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Surface;
import java.util.Random;
import java.lang.Math;
import android.util.Log;

public class Particle {

    private float mx, my;
    private float xv, yv;
    private int age;
    private int	stop;
    private int	fade;
    private int	color;
    private Random rand;
    private boolean alive;
    private Paint brush;

    public Particle(int c, int x, int y, int O, int Oerror, float speed, float sError, int sto) {
        rand = new Random();
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);

        color = c;
        mx = x;
        my = y;

        stop = rand.nextInt(sto) + 2;
        age = 0;
        fade = Color.alpha(color) / 8;
        alive = true;

        float rspeed = speed + ((rand.nextFloat() * sError) - (sError / 2));

        Oerror = (int) (-60f * rspeed + 180f) + Oerror;
        int d = O + ((int) (rand.nextFloat() * Oerror)) - (Oerror / 2);

        if (d > 360) d -= 360;
        if (d < 0) d += 360;

        O = d;
        d = d % 90; 
        float r = (float) d / 180 * (float) Math.PI;
        float a = (float) Math.cos(r) * rspeed;
        float o = (float) Math.sin(r) * rspeed;
        if (O < 90) {
            xv = o;
            yv = -a;
        }
        if (O > 90 && O < 180) {
            xv = a;
            yv = o;
        }
        if (O > 180 && O < 270) {
            xv = -o;
            yv = a;
        }
        if (O > 270) {
            xv = -a;
            yv = -o;
        }
    }

    public Particle(int c, int x, int y, int O, float speed, int sto) {
        this(c, x, y, O, 40, speed, 0.5f, sto);
    }

    public void update() {
        if (alive) {
            if (age > stop) {
                xv *= 0.8f;
                yv *= 0.8f;
            }
        
            mx += xv;
            my += yv;
           
            age++;

            if ((xv < 0.001f && xv > -0.001f) && (yv < 0.001f && yv > -0.001f)) {
                int a = Color.alpha(color);
                a -= fade;
                color = Color.argb(a, Color.red(color), Color.green(color), Color.blue(color));
                if (a <= 10) alive = false;
            }
        }
    }

    public void render (Canvas c) {
        brush.setColor(color);
        c.drawPoint(mx, my, brush);
    }

    public boolean isAlive() {
        return alive;
    }
}
