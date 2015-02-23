package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Alex on 2/10/2015.
 */
public class Game {
    // code for gameplay here

    // init: from opening activity. initiate game class, hand off to bird selection
    // birdselect: hand off to bird selection
    // roundA/B: players place bird, then check if end condition. if not, up to birdselect
    // end: game over. hand off to end screen
    public enum GameState {init, birdselect, roundA, roundB, end}

    private GameState playState;
    GameView view;

    Bird currBird;
    float lastX, lastY;
    float marginX, marginY;
    static final float GAME_FIELD_SCALE = 0.9f;

    public float getGameField() {
        return gameField;
    }

    private float gameField;
    private Paint paintBorder;  // paint object to outline playing field

    Player P1, P2;

    // custom player class to store player information
    // currBird may not end up being stored here;
    // currently, one Bird object is handled at a time, with the rest saved on the array to draw as needed
    // by tracking which player is up on the GameActivity, we shouldn't need to save the bird here again?
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
        paintBorder.setColor(0xffcbe5f8);  // hex value for sky blue paint. consider replacing with actual sky image?
        paintBorder.setStrokeWidth(5.f);

        gameBirds = new ArrayList<Bird>();

        gameField = 0;
        playState = GameState.init;

        view = view_context;

    }

    // initial player construction, using names passed down from intent sent to GameActivity.
    public void SetPlayers(String name1, String name2){
        P1 = new Player();
        P1.setName(name1);
        P1.setID(1);

        P2 = new Player();
        P2.setName(name2);
        P2.setID(2);
    }

    public Player getPlayerById(int id){
        if(id == 1){
            return P1;
        }
        else{
            return P2;
        }
    }

    public String getNameById(int id){
        if(id == 1){
            return P1.getName();
        }
        else{
            return P2.getName();
        }
    }

    // draw function.
    // all but most recently added bird in array should never be modified
    // most recent bird is referred to with the currBird member variable. Don't adjust other birds by array index
    // possibly track the margins and dimensions as class members to use during onTouch?
    public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();
        int minimumDimension = wid < hit ? wid : hit;  // compares wid to hit, chooses smallest value

        gameField = (int)(minimumDimension*GAME_FIELD_SCALE); // compute size of gameplay field as square scaled to 95% of available screen

        marginX = (wid-gameField)/2;
        marginY = (hit-gameField)/2;  // find space left in view, split in half to use as margins on field of play

        canvas.drawRect(marginX, marginY, marginX + gameField, marginY + gameField, paintBorder);

        for (Bird b : gameBirds) {
            b.draw(canvas);
        }
    }

    // called from GameActivity, through GameView, to here. Updates current state of the game
    public void setGameState(GameState playState) {
        this.playState = playState;
    }
    public GameState getPlayState() {
        return playState;
    }

    public void AddBird(Bird bird){
        gameBirds.add(bird);
        currBird = gameBirds.get(gameBirds.size()-1);
        view.invalidate();
    }

    public Bird getCurrBird(){
        return currBird;
    }

    // Hande Touch Events.
    // the main concern here is just moving the bird, which can be done during Action_Move
    // we don't care about up or down because we'll always be moving the bird, regardless of whether the player has touched it or not
    // TODO: however, we do need to keep the birds from moving outside the bounds of the playing area
    // X and Y get the current touch event coordinates
    // Xp and Yp, declared as members here, are to be used to track the previous X and Y values to decide where objects have moved to (by subtraction)
    public boolean onTouchEvent(View view, MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return true;

            case MotionEvent.ACTION_MOVE:
                // TODO: moving birds within playing area
                float x = currBird.getX();
                float y = currBird.getY();
                float wid = currBird.getWidth();
                float hit = currBird.getHeight();

                currBird.move(event.getX() - lastX, event.getY() - lastY);

//                if (x + wid/2 + marginX > gameField) currBird.setX(gameField + marginX);
//                if (y + hit/2 + marginY > gameField) currBird.setY(gameField + marginY);
//                if (x - wid/2 < marginX) currBird.setX(marginX);
//                if (y - hit/2 < marginY) currBird.setY(marginY);

                Log.i("X", Float.toString(x));
                Log.i("Y", Float.toString(y));

                lastX = event.getX();
                lastY = event.getY();

                view.invalidate();
                return true;
        }

        return false;
    }


    public boolean CheckBirds(){
        for(int i = 0; i <= gameBirds.size()-2; i++){
            if(currBird.collisionTest(gameBirds.get(i))){
                return true;
            }
        }
        return false;
    }
}
