package salpeka1.msu.edu.project1;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for Game View.
 */
public class GameView extends View {

    private Game game;

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        game = new Game(getContext(), this);
    }


    @Override
    // pass drawing functionality to the game class itself
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        game.draw(canvas);

    }

    // check and make updates to the game object's current state, as prompted by GameActivity.onGameStateChange()
    public Game.GameState getGameState(){
        return game.getGameState();
    }
    public void setGameState(Game.GameState playstate){
        game.setGameState(playstate);
    }

    public Game getGameObject(){return game;}

    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }
    public void restoreInstanceState(Bundle bundle){
        game.restoreInstanceState(bundle);
    }

    //generate a new bird for the game to handle. this new bird is set at the end of the array and also referenced as the current bird to move around
    public void CreateBird(int birdID){
        Bird bird = new Bird(this.getContext(), birdID, game);
        game.AddBird(bird);

    }

    @Override
    // pass touch events to the game itself
    public boolean onTouchEvent(MotionEvent event){
        return game.onTouchEvent(this, event);
    }
}