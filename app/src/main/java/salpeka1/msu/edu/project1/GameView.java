package salpeka1.msu.edu.project1;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
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

        Play();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        game.draw(canvas);

    }

    public Game.GameState getGameState(){
        return game.getPlayState();
    }
    public void setGameState(Game.GameState playstate){
        game.setPlayState(playstate);
    }
    public void Play(){
        switch(game.getPlayState()){
            case init:


        }
    }


}