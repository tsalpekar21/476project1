package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/*
* This activity needs to be recalled after every round.
* There cannot be multiple versions of this activity pushed onto the stack at one time
 */

public class GameActivity extends ActionBarActivity {

    private GameView gameView;  // reference to the custom view that handles the game class
    private String player1;     // varialbe to store player1 name
    private String player2;     // variable to store player2 name
    private int P1Bird, P2Bird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView)findViewById(R.id.viewGame);
     //   onGameStateChange(gameView);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        onGameStateChange(gameView);  // when this activity takes the foreground, check the state of the game
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClickQuit(View view)
    {
        Intent intent = new Intent(this, FinishedActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  // adding this flag prevents creating repeat activities if one exists on stack
        startActivity(intent);
    }

    public void onClickSet(View view)
    {
        // this button click will, instead of pulling up BirdSelectActivity, lock a bird's location in on the game field and call a check for endgame conditions
        Intent intent = new Intent(this, BirdSelectActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    // switch statement to delegate control of the game to either a different activity or to the game view's onTouch event controllers
    // with each switch, update the state of the game as appropriate.
    // calls through gameview to get game class' state of play, represented as enums {init, birdselect, roundA, roundB, end}
    public void onGameStateChange(View view){
        switch(gameView.getGameState()){
            case init:

                player1 = getIntent().getExtras().getString("player1");
                player2 = getIntent().getExtras().getString("player2");
                gameView.getGameObject().SetPlayers(player1, player2);  // construct player objects and set their names

                Intent intent = new Intent(this, BirdSelectActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                gameView.setGameState(Game.GameState.roundA);  // first player goes after this init
                startActivity(intent);

                break;

            case roundA:

                P1Bird = getIntent().getExtras().getInt("p1bird");
                P2Bird = getIntent().getExtras().getInt("p2bird");


            default:

                break; // onReturn calls this to find current state. in init and birdselect, start needed activities. on play, hand off to view's touch handler. on finish, start ending activity
        }
    }

}
