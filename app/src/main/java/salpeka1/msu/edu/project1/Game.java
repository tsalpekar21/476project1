package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Alex on 2/10/2015.
 */
public class Game {
    // code for gameplay here

    static final float GAME_FIELD_SCALE = 0.95f;
    private float gameField;
    private Paint paintBorder;  // paint object to outline playing field

    private int wid;
    private int hit;

    public ArrayList<Bird> gameBirds;  // array of birds w/ locations to push into as they are placed

    public Game(Context context) {
        this.gameBirds = new ArrayList<Bird>();

        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(0xff0000ff);  // hex value for blue paint
        paintBorder.setStrokeWidth(5.f);

        gameBirds.add(new Bird(context, R.drawable.ostrich, this));

        gameField = 0;

    }

    public void draw(Canvas canvas){
        wid = canvas.getWidth();
        hit = canvas.getHeight();
        int minimumDimension = wid < hit ? wid : hit;  // compares wid to hit, chooses smallest value

        gameField = (int)(minimumDimension*GAME_FIELD_SCALE); // compute size of gameplay field as square scaled to 95% of available screen

        float marginX = (wid-gameField)/2;
        float marginY = (hit-gameField)/2;  // find space left in view, split in half to use as margins on field of play

        canvas.drawRect(marginX, marginY, marginX + gameField, marginY + gameField, paintBorder);

        for (Bird b : gameBirds) {
            b.draw(canvas);
        }
    }

    public int getWidth() {
        return wid;
    }

    public int getHeight() {
        return hit;
    }
}
