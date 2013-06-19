package com.sIlence.androidracertwo;

import com.sIlence.androidracertwo.game.*;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "fuck you";
    
    public static int INCREASE_KILLS = 1;
    public static int INCREASE_DEATHS = -1;
    public static int INCREASE_NULL = 0;
    
    private boolean pausing = false;
    private boolean starting = true;
    private boolean gameOver = false;
    private boolean won = false;
    
    protected GameLoop loop;
    private Paint brush;
    
    protected int score;
    
    protected long endTime;
    
    private int	boxWidth;
    private int	boxHeight;
    private int	boxsX;
    private int	boxsY;
    private int	top;
    private int	rotation;
    
    private float x;
    private float y;
    private float xDiff;
    private float yDiff;
    
    private Rect bounds;
    private String textString;
    
    private Rect menu;
    private int	fromRight;
    
    private int kills;
    private int deaths;
    private int time;

    private Game game;
        
    public GameView(Context context, Game g) {
	super(context);
	getHolder().addCallback(this);
	setFocusable(true);
	
	game = g;
	loop = new GameLoop(this);
    }
    
    protected void newGame() {
	game.init(this);
	score = 0;
	bounds = new Rect();
	textString = "";
	
	setKills(0);
	setDeaths(0);
	setTime(0);
	endTime = System.currentTimeMillis();

	starting = false;
	gameOver = false;
	won = false;
    }
    
    public void update() {
    	if (starting || gameOver) return;
	
	incTime(loop.framePeriod());

	game.updateLengths();
	
	local().update();
	other().update();
	wall1().update();
	wall2().update();
	
	if (pausing) {
	    pauseGame();
	}
    }
    
    public void render(Canvas c) {
	background(c);
	drawRacers(c);
	hud(c);
	messages(c);
    }
    
    public void drawRacers(Canvas c) {
	local().render(c);
	other().render(c);
	wall1().render(c);
	wall2().render(c);
    }
    
    public void hud(Canvas c) {
	brush.setColor(0xffffffff);
	c.drawText("Time: " + (getTime() / 1000), 10, brush.getFontSpacing(), brush);
	
	textString = getKills() + " :";
	c.drawText(getKills() + " : " + getDeaths(), getWidth() - fromRight - halfWidth(textString), brush.getFontSpacing(), brush);
    }
    
    public void messages(Canvas c) {
	if (gameOver) {
	    if (won) {
	    } else {
		drawMessageBox(c, new String[] {"Game Over", "You Lost"});
	    }
	} else if (starting) {
	    drawMessageBox(c, new String[] {"You Are Blue", "Swipe To Turn", "Make Yellow Crash", "Tap To Play"});
	} else if (loop.isPaused()) {
	    drawMessageBox(c, new String[] {"Touch Screen", "To Resume"});
	}
    }
    
    public void drawMessageBox(Canvas c, String[] lines) {
	brush.setStyle(Paint.Style.FILL);
	brush.setColor(0x50000000);
	c.drawRect(menu, brush);
	
	brush.setColor(0xffffffff);
	brush.setStyle(Paint.Style.STROKE);
	c.drawRect(menu, brush);
	
	float lineY;
	if (lines.length % 2 == 0) {
	    lineY = getHeight() / 2 + brush.getFontSpacing() - ((lines.length / 2) * brush.getFontSpacing());
	} else {
	    lineY = getHeight() / 2 + brush.getFontSpacing() / 2 - ((lines.length / 2) * brush.getFontSpacing());
	}
	
	for (int i = 0; i < lines.length; i++) {
	    c.drawText(lines[i], getWidth() / 2 - halfWidth(lines[i]), lineY, brush);
	    lineY += brush.getFontSpacing();
	}
    }
    
    public void checkScore() {
	/*	if (getKills() >= 10 && getKills() - 2 >= getDeaths()) {
	    if (deaths > 0) {
		score = (int) (10000 / (getTime() / 1000) * ((float) getKills() / getDeaths()));
	    } else {
		score = (int) (10000 / (getTime() / 1000) * 15);
	    }
	    
	    won = true;
	    gameOver();
	    
	    return true;
	} else if (getDeaths() >= 10 && getDeaths() - 2 >= getKills()) {
	    won = false;
	    gameOver();
	    
	    return true;
	}
	
	return false; */
	
	int s = game.checkScore();
	if (s != -1) score = s;
    }
    
    public void gameOver(boolean w) {
	won = w;
	
	gameOver = true;
	endTime = System.currentTimeMillis();
	
	local().pause();
	other().pause();
	wall1().pause();
	wall2().pause();
    }
    
    public void changeScore(int scoreType) {
	if (scoreType == INCREASE_DEATHS) {
	    incDeaths();
	} else if (scoreType == INCREASE_KILLS) {
	    incKills();
	}
    }
    
    public int halfWidth(String text) {
	brush.getTextBounds(text, 0, text.length(), bounds);
	return bounds.width() / 2;
    }
    
    public void background(Canvas c) {
	brush.setColor(0xFF000308);
        brush.setStyle(Paint.Style.FILL);
	c.drawRect(0, 0, getWidth(), getHeight(), brush);
	
	brush.setColor(0x206FC0DF);
	brush.setStyle(Paint.Style.STROKE);
	
	for (int x = 0; x < getWidth(); x += 70) {
	    c.drawLine(x, top, x, getHeight(), brush);
	}
	for (int y = top; y < getHeight(); y += 70) {
	    c.drawLine(0, y, getWidth(), y, brush);
	}
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent e) {
	if (e.getAction() == MotionEvent.ACTION_DOWN) {
	    if ((loop.isPaused() || starting) && !gameOver) {
		resumeGame();
		starting = false;
	    } else if (gameOver && endTime + 300 < System.currentTimeMillis()) {
		stopGame();
		newGame();
		start();
	    }
	    x = e.getX();
	    y = e.getY();
	} else if (e.getAction() == MotionEvent.ACTION_MOVE) {
	    xDiff = e.getX() - x;
	    yDiff = e.getY() - y;
	    x = e.getX();
	    y = e.getY();
	    
	    int g = gratistDiff(xDiff, yDiff);
	    if (g == 0) {
		if (xDiff > 1 && local().changeDirection(1)) {
		}
		if (xDiff < -1 && local().changeDirection(3)) {
		}
	    } else if (g == 1) {
		if (yDiff < -1 && local().changeDirection(0)) {
		}
		if (yDiff > 1 && local().changeDirection(2)) {
		}
	    }
	}
	return true;
    }
    
    protected int gratistDiff(float x, float y) {
        if (x < 0) x = -x;
        if (y < 0) y = -y;
	
        if (x > y) return 0;
	if (y > x) return 1;
	return -1;
    }
    
    public void pause() {
        pausing = true;
    }
    
    public void pauseGame() {
        loop.pauseGame();
	pausing = false;
	
	local().pause();
	other().pause();
	wall1().pause();
	wall2().pause();
    }
    
    public synchronized void resumeGame() {
        if (gameOver) return;
        loop.resumeGame();
	
	local().resume();
	other().resume();
	wall1().resume();
	wall2().resume();
    }
    
    public synchronized void stopGame() {
        if (loop == null) return;
        loop.stopGame();
    }
    
    public synchronized void start() {
        if (loop != null) {
	    loop.stopGame();
	}
	pausing = false;
	notify();
	
	loop = new GameLoop(this);
	loop.start();
	
	pauseGame();
    }
    
    public boolean isPaused() {
        if (loop == null) return true;
	if (gameOver) return true;
        return loop.isPaused();
    }
    
    public int boxsX() {
        return boxsX;
    }
    
    public int boxsY() {
        return boxsY;
    }
    
    protected void setBoxsX(int n) {
	boxsX = n;
    }
    
    
    protected void setBoxsY(int n) {
	boxsY = n;
    }
    
    public int boxWidth() {
        return boxWidth;
    }
    
    public int boxHeight() {
        return boxHeight;
    }
    
    protected void setBoxWidth(int n) {
	boxWidth = n;
    }
    
    protected void setBoxHeight(int n) {
	boxHeight = n;
    }
    
    public int top() {
        return top;
    }
    
    public int rotation() {
        return rotation;
    }
    
    public int gratestLengthInSegments() {
        if (boxsX > boxsY) return boxsX;
        return boxsY;
    }

    public int getKills() {
	return kills;
    }

    public void incKills() {
	kills++;
    }

    public void setKills(int k) {
	kills = k;
    }
    public int getDeaths() {
	return deaths;
    }

    public void incDeaths() {
	deaths++;
    }

    public void setDeaths(int d) {
	deaths = d;
    }

    public int getTime() {
	return time;
    }

    public void incTime(int a) {
	time += a;
    }

    public void setTime(int t) {
	time = t;
    }

    public int framePeriod() {
	return loop.framePeriod();
    }

    public LightRacer local() {
	return game.local();
    }

    public AIRacer other() {
	return game.other();
    }

    public WallRacer wall1() {
	return game.wall1();
    }

    public WallRacer wall2() {
	return game.wall2();
    }
    
    public void surfaceCreated(SurfaceHolder arg0) {
	
	Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	rotation = display.getRotation();
	
	brush = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	// Screen dependent stuff
	
	int size = getWidth();
	if (getHeight() < getWidth()) size = getHeight();
	
	if (size < 350) { // small
	    
	    menu = new Rect(getWidth() / 2 - 100, getHeight() / 2 - 40, getWidth() / 2 + 100, getHeight() / 2 + 40);
	    brush.setTextSize(12);
	    
	    boxWidth = 3;
	    boxHeight = 3;
	    // has to be in boxs or it fucks everything up
	    top = boxHeight * 5;
	    fromRight = 40;
	} else { // normal
	    
	    menu = new Rect(getWidth() / 2 - 220, getHeight() / 2 - 80, getWidth() / 2 + 220, getHeight() / 2 + 80);
	    brush.setTextSize(26);
	    
	    boxWidth = 5;
	    boxHeight = 5;
	    // has to be in boxs or it fucks everything up
	    top = boxHeight * 8;
	    fromRight = 80;
	}
	
	boxsX = getWidth() / boxWidth;
        boxsY = (getHeight() - top) / boxHeight;
	
        if (starting) newGame();
        start();
    }
    
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}
    
    public void surfaceDestroyed(SurfaceHolder arg0) {
	local().stop();
	other().stop();
	wall1().stop();
	wall2().stop();
	
	stopGame();
    }
}