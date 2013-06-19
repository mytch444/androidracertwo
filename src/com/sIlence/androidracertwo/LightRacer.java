package com.sIlence.androidracertwo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Surface;
import java.util.Arrays;

public class LightRacer extends Part {
    public static final int	STANDARD_LENGTH = 70;
    
    int[] linex;
    int[] liney;
    int length;
    
    Explosion[] explosions;
    LineFall[] lineFall;
    
    int	scoreToChange;
    
    int ri, a, front;
    
    boolean foundSpawn;

    boolean light;
    
    
    public LightRacer(GameView v, int c, int stc) {
        super(v);

        linex = new int[STANDARD_LENGTH];
        liney = new int[STANDARD_LENGTH];
	length = STANDARD_LENGTH;
        direction = rand.nextInt(4);
        color = c;
        startColor = color;
        brush.setStrokeWidth(0f);
	light = true;
	
        explosions = new Explosion[3];
        Arrays.fill(explosions, new Explosion(view, color, 0, 0, 0, 0, 0, 0, 0, 1));
	
        lineFall = new LineFall[3];
        Arrays.fill(lineFall, new LineFall(view, startColor, linex, liney, 1));
	
        startColor = color;
	
        scoreToChange = stc;
	
	foundSpawn = true;
    }
    
    @Override
    public void update() {
	
        updateExplosions();
	
        if (dieing > 0) {
            dieing++;
	    
	    if (!foundSpawn) {
		findSpawn();
	    }
	    
            if (dieing == 30) {
		
		view.checkScore();
		    
		if (!foundSpawn) {
		    spawn(length);
		} else {
		    spawnSpec(linex[0], liney[0], safestDirection(), length);
		}
		    
		dieing = 0;
		foundSpawn = true;
            }
	    
            return;
        }
	
        move();
        offScreen();
	updateLength();	
        updateLine();
        checkCollisions();
    }

    public void updateLength() {
        if (length != linex.length) {
	    int[] tempx = linex.clone();
	    int[] tempy = liney.clone();
		
	    linex = new int[length];
	    liney = new int[length];
		
	    int l = (linex.length > tempx.length) ? tempx.length : linex.length;
		
	    System.arraycopy(tempx, 0, linex, 0, l);
	    System.arraycopy(tempy, 0, liney, 0, l);
        }
    }
    
    public void die() {
        if (explosions == null || lineFall == null) return;
	
	for (int i = 0; i < explosions.length; i++) {
            if (!explosions[i].isAlive()) {
                explosions[i] = new Explosion(view, startColor, (int) linex[0], (int) liney[0], direction, 100, 30, 0, 3, 0);
                break;
            }
        }
	
        for (int i = 0; i < lineFall.length; i++) {
            if (!lineFall[i].isAlive()) {
                lineFall[i] = new LineFall(view, startColor, linex, liney, 0);
                break;
            }
        }
	
	view.changeScore(scoreToChange);
        dieing = 1;
	
	foundSpawn = false;
    }
    
    public void updateExplosions() {
        for (int i = 0; i < explosions.length; i++) {
            explosions[i].update();
        }
        for (int i = 0; i < lineFall.length; i++) {
            lineFall[i].update();
        }
    }
    
    public void updateLine() {
        for (int i = linex.length - 1; i > 0; i--) {
            if (linex[0] == linex[i] && liney[0] == liney[i]) {
                die();
                return;
            }
	    
            linex[i] = linex[i - 1];
            liney[i] = liney[i - 1];
        }
    }
    
    public void checkCollisions() {
        if (opps == null) return;
	
        x = linex[0];
        y = liney[0];
        for (int i = 0; i < opps.length; i++) {
            if (opps[i] != this) {
                if (opps[i].collides(this) && opps[i].isAlive()) {
                    die();
                    break;
                }
            }
        }
    }
    
    public void move() {
        switch (direction) {
	case 0:
	    liney[0] -= view.boxHeight();
	    break;
	case 1:
	    linex[0] += view.boxWidth();
	    break;
	case 2:
	    liney[0] += view.boxHeight();
	    break;
	case 3:
	    linex[0] -= view.boxWidth();
	    break;
        }
    }
    
    @Override
    public void render(Canvas c) {
        for (ri = 0; ri < explosions.length; ri++) {
            explosions[ri].render(c);
        }
        for (ri = 0; ri < lineFall.length; ri++) {
            lineFall[ri].render(c);
        }
	
        if (dieing == 0) {
            renderLines(c);
        }
    }
    
    public void renderLines(Canvas c) {
        front = Color.argb(0, 255, 255, 255);
	
        brush.setColor(color);
        for (ri = linex.length - 1; ri > 0; ri--) {
            if ((((linex[ri] > linex[ri - 1]) ? (linex[ri] - linex[ri - 1]) : (linex[ri - 1] - linex[ri])) <= view.boxWidth()) &&
		(((liney[ri] > liney[ri - 1]) ? (liney[ri] - liney[ri - 1]) : (liney[ri - 1] - liney[ri])) <= view.boxHeight())) {
		c.drawLine(linex[ri], liney[ri], linex[ri - 1], liney[ri - 1], brush);
	    }
        }
	if (!light) return;
	
        for (ri = 10; ri > 0; ri--) {
            if ((((linex[ri] > linex[ri - 1]) ? (linex[ri] - linex[ri - 1]) : (linex[ri - 1] - linex[ri])) <= view.boxWidth()) &&
		(((liney[ri] > liney[ri - 1]) ? (liney[ri] - liney[ri - 1]) : (liney[ri - 1] - liney[ri])) <= view.boxHeight())) {
	        a = Color.alpha(front);
                a += 25;
                front = Color.argb(a, 255, 255, 255);
                brush.setColor(front);
                c.drawLine(linex[ri], liney[ri], linex[ri - 1], liney[ri - 1], brush);
            }
        }
    }
    
    public boolean changeDirection(int wd) { // Change to spicific direction
	if ((wd == oppDirection(direction)) || (wd == direction) || (lastTurn + 5 * view.framePeriod() > view.getTime()) || (dieing != 0)) {
	    return false;
        } else { // Can turn this way8
            direction = wd;
            lastTurn = view.getTime();
	    
	    
            return true;
        }
    }
    
    protected void newLine() {
        for (int i = 0; i < lineFall.length; i++) {
            if (!lineFall[i].isAlive()) {
                lineFall[i] = new LineFall(view, startColor, linex, liney, 0);
                break;
            }
        }
	
        linex = new int[length];
        liney = new int[length];
    }
    
    public void spawn(int length) {
	
	linex[0] = (rand.nextInt(view.boxsX() - 20) + 10) * view.boxWidth();
	liney[0] = (rand.nextInt(view.boxsY() - 25) + 15) * view.boxHeight();
	
	spawnSpec(linex[0], liney[0], safestDirection(), length);
    }
    
    public void findSpawn() {
	linex[0] = (rand.nextInt(view.boxsX() - 20) + 10) * view.boxWidth();
	liney[0] = (rand.nextInt(view.boxsY() - 25) + 15) * view.boxHeight();
	
        if (!safeToTurn(0, 100)) {
	    return;
	} else if (!safeToTurn(1, 100)) {
	    return;
	} else if (!safeToTurn(2, 100)) {
	    return;
	} else if (!safeToTurn(3, 100)) {
	    return;
	}
	
	foundSpawn = true;
    }
    
    public void spawnSpec(int x, int y, int di, int length) {
        linex = new int[length];
        liney = new int[length];
	
        lastTurn = 0;
        color = startColor;
	
        linex[0] = x;
        liney[0] = y;
        direction = di;
    }
    
    public int safestDirection() {
        int newDi = -1;
        int bestClearance = 0;
	
        for (int checkingDi = 0; checkingDi < 4; checkingDi++) {
	    
            x = linex[0];
            y = liney[0];
	    
            clearancetesting:
            for (int clearance = 1; clearance < view.gratestLengthInSegments(); clearance++) {
                switch (checkingDi) {
		case 0:
		    x = linex[0];
		    y = liney[0] - (clearance * view.boxHeight());
		    break;
		case 1:
		    x = linex[0] + (clearance * view.boxWidth());
		    y = liney[0];
		    break;
		case 2:
		    x = linex[0];
		    y = liney[0] + (clearance * view.boxHeight());
		    break;
		case 3:
		    x = linex[0] - (clearance * view.boxWidth());
		    y = liney[0];
		    break;
                }
		
		if (clearance > bestClearance) {
		    newDi = checkingDi;
		    bestClearance = clearance;
		}
		
		
                for (int i = 0; i < opps.length; i++) {
                    if (opps[i].collides(this)) {
			
                        if (clearance > bestClearance) {
                            newDi = checkingDi;
                            bestClearance = clearance;
                        }
			
                        break clearancetesting;
                    }
                }
            }
        }
	
        return newDi;
    }
    
    public boolean safeToTurn(int wd, int distance) {
	int distanceChecked = 0;
	
	x = linex[0];
        y = liney[0];
	
        while (distanceChecked < distance) {
            switch (wd) {
	    case 0:
		distanceChecked += view.boxHeight();
		
		y -= view.boxHeight();
		break;
	    case 1:
		distanceChecked += view.boxWidth();
		
		x += view.boxWidth();
		break;
	    case 2:
		distanceChecked += view.boxHeight();
		
		y += view.boxHeight();
		break;
	    case 3:
		distanceChecked += view.boxWidth();
		
		x -= view.boxWidth();
		break;
            }
	    
            for (int i = 0; i < opps.length; i++) {
                if (opps[i].collides(this)) {
                    return false;
                }
            }
        }
	
        return true;
    }
    
    public void offScreen() {
        if (liney[0] < view.top() + view.boxHeight()) {

	    linex[0] = view.boxsX() / 2 * view.boxWidth() + (view.boxsX() / 2 * view.boxWidth() - x);
            liney[0] = (view.boxsY() - 2) * view.boxHeight() + view.top();

        } else if (liney[0] > (view.boxsY() - 1) * view.boxHeight() + view.top()) {

            linex[0] = view.boxsX() / 2 * view.boxWidth() + (view.boxsX() / 2 * view.boxWidth() - x);
            liney[0] = view.top() + view.boxHeight() * 2;

        } else if (linex[0] < view.boxWidth()) {

            linex[0] = (view.boxsX() - 2) * view.boxWidth();
            liney[0] = view.boxsY() / 2  * view.boxHeight() + (view.boxsY() / 2 * view.boxHeight() - y) + view.top();

        } else if (linex[0] > (view.boxsX() - 1) * view.boxWidth()) {

            linex[0] = view.boxWidth();
            liney[0] = view.boxsY() / 2 * view.boxHeight() + (view.boxsY() / 2 * view.boxHeight() - y) + view.top();

        }
    }
    
    @Override
    public boolean collides(Part other) {
        int lx = other.getX();
        int ly = other.getY();
        for (int i = 0; i < linex.length; i++) {
            if (lx == linex[i] && ly == liney[i]) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void pause() {
	
    }
    
    @Override
    public void resume() {
	
    }
    
    @Override
    public void stop() {
	
    }

    public void setLength(int l) {
	length = l;
    }

    public int getLength() {
	return length;
    }
}