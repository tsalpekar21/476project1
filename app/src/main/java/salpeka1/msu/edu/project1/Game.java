package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
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

    private GameState gameState;
    GameView view;

    public ArrayList<Bird> gameBirds;  // array of birds w/ locations to push into as they are placed

    Bird currBird;
    float lastX, lastY;
    float marginX, marginY, scaleFactor;
    static final float GAME_FIELD_SCALE = 0.9f;

    private final static String PLAYERNAMES = "PLAYERNAMES";
    private final static String PLAYERIDS = "PLAYERIDS";
    private final static String BIRDCOORDINATES = "BIRDCOORDINATES";
    private final static String BIRDIMAGES = "BIRDIMAGES";
    private final static String STATE = "GAMESTATE";

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
        private String name;
        private int ID;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setID(int ID) {
            this.ID = ID;
        }
        }

    public Game(Context context, GameView view_context) {
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(0xffcbe5f8);  // hex value for sky blue paint. consider replacing with actual sky image?
        paintBorder.setStrokeWidth(5.f);

        gameBirds = new ArrayList<Bird>();

        gameField = 0;
        gameState = GameState.init;

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
        } else {
            return P2;
        }
    }

    public String getNameById(int id){
        if(id == 1){
            return P1.getName();
        } else {
            return P2.getName();
        }
    }

    // save game details in bundle
    public void saveInstanceState(Bundle bundle) {
        float[] BirdCoordinates = new float[gameBirds.size()*2];
        int[] BirdImages = new int[gameBirds.size()];
        String[] PlayerNames = new String[2];
        int GameState;


        for(int i = 0; i < gameBirds.size(); i++){
            Bird bird = gameBirds.get(i);
            BirdCoordinates[i*2] = bird.getX();
            BirdCoordinates[i*2+1] = bird.getY();
        }
        bundle.putFloatArray(BIRDCOORDINATES, BirdCoordinates);

        for(int j = 0; j < gameBirds.size(); j++){
            BirdImages[j] = gameBirds.get(j).getImageID();
        }
        bundle.putIntArray(BIRDIMAGES, BirdImages);

        PlayerNames[0] = P1.getName();
        PlayerNames[1] = P2.getName();
        bundle.putStringArray(PLAYERNAMES, PlayerNames);

        switch(gameState){
            case init:
                GameState = 0;
                break;
            case birdselect:
                GameState = 1;
                break;
            case roundA:
                GameState = 2;
                break;
            case roundB:
                GameState = 3;
                break;
            case end:
                GameState = 4;
                break;
            default:
                GameState = 0;
        }
        bundle.putInt(STATE, GameState);

    }

    public void restoreInstanceState(Bundle bundle){
        float[] BirdCoordinates = bundle.getFloatArray(BIRDCOORDINATES);
        int[] BirdImages = bundle.getIntArray(BIRDIMAGES);
        String[] PlayerNames = bundle.getStringArray(PLAYERNAMES);
        int GameState = bundle.getInt(STATE);

        for( int i = 0; i < BirdImages.length; i++){
            Bird bird = new Bird(view.getContext(), BirdImages[i], this);
            bird.setX(BirdCoordinates[i*2]);
            bird.setY(BirdCoordinates[i*2+1]);
            gameBirds.add(bird);
        }

        SetPlayers(PlayerNames[0], PlayerNames[1]);

        switch(GameState){
            case 0:
                gameState = Game.GameState.init;
                break;
            case 1:
                gameState = Game.GameState.birdselect;
                break;
            case 2:
                gameState = Game.GameState.roundA;
                currBird = gameBirds.get(gameBirds.size()-1);
                break;
            case 3:
                gameState = Game.GameState.roundB;
                currBird = gameBirds.get(gameBirds.size()-1);
                break;
            case 4:
                gameState = Game.GameState.end;
                break;
            default:
                gameState = Game.GameState.init;
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

        scaleFactor = (float) gameField / (float) canvas.getWidth();

        canvas.save();

        canvas.translate(marginX, marginY);

        canvas.scale(scaleFactor, scaleFactor);

        canvas.drawRect(marginX, marginY, marginX + gameField, marginY + gameField, paintBorder);

        for (Bird b : gameBirds) {
            b.draw(canvas);
        }

        canvas.restore();
    }

    // called from GameActivity, through GameView, to here. Updates current state of the game
    public void setGameState(GameState playState) {
        this.gameState = playState;
    }

    public GameState getGameState() {
        return gameState;
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
                float x = currBird.getX();
                float y = currBird.getY();

                float wid = currBird.getWidth();
                float hit = currBird.getHeight();

                currBird.move(event.getX() - lastX, event.getY() - lastY);

                if (x > view.getWidth() - marginX - wid) currBird.setX(view.getWidth() - marginX - wid);
                if (y > view.getHeight() - marginY - hit) currBird.setY(view.getHeight() - marginY - hit);
                if (x < marginX) currBird.setX(marginX);
                if (y < marginY) currBird.setY(marginY);

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
