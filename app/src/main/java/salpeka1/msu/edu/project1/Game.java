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

    // init: from opening activity. initiate game class, hand off to bird selection
    // birdselect: hand off to bird selection
    // roundA/B: players place bird, then check if end condition. if not, up to birdselect
    // end: game over. hand off to end screen
    public enum GameState {init, birdselect, roundA, roundB, end}

    private GameState playState;
    GameView view;

    Player P1, P2;
    private class Player {

        private Bird currBird;
        private String name;
        private int ID;

        public Bird getCurrBird() {
            return currBird;
        }

        public String getName() {
            return name;
        }

        public int getID() {
            return ID;
        }

        public void setCurrBird(Bird currBird) {
            this.currBird = currBird;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        }

    public ArrayList<Bird> gameBirds;  // array of birds w/ locations to push into as they are placed

    public Game(Context context, GameView view_context) {
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(0xffcbe5f8);  // hex value for sky blue paint
        paintBorder.setStrokeWidth(5.f);

        gameBirds = new ArrayList<Bird>();
        gameBirds.add(new Bird(context, R.drawable.ostrich, this));

        gameField = 0;
        playState = GameState.init;

        view = view_context;

    }

    public void SetPlayers(String name1, String name2){

        P1 = new Player();
        P1.setName(name1);
        P1.setID(1);

        P2 = new Player();
        P2.setName(name2);
        P2.setID(2);

    }

    public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();
        int minimumDimension = wid < hit ? wid : hit;  // compares wid to hit, chooses smallest value

        gameField = (int)(minimumDimension*GAME_FIELD_SCALE); // compute size of gameplay field as square scaled to 95% of available screen

        float marginX = (wid-gameField)/2;
        float marginY = (hit-gameField)/2;  // find space left in view, split in half to use as margins on field of play

        canvas.drawRect(marginX, marginY, marginX + gameField, marginY + gameField, paintBorder);

        for (Bird b : gameBirds) {
            b.draw(canvas);
        }
    }

    public void setPlayState(GameState playState) {
        this.playState = playState;
    }

    public GameState getPlayState() {
        return playState;
    }
}
