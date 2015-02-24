package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


/*
* This activity needs to be recalled after every round.
* There cannot be multiple versions of this activity pushed onto the stack at one time
 */

public class GameActivity extends ActionBarActivity {

    private GameView gameView;  // reference to the custom view that handles the game class
    private TextView playerView;
    private String player1;     // varialbe to store player1 name
    private String player2;     // variable to store player2 name
    private int P1Bird, P2Bird;
    private int RoundNumber = 1;    // Variable to keep track of Round Numbers.
    private enum Player {Player1, Player2};
    private Player currPlayer;
    private String winner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView)findViewById(R.id.viewGame);
        playerView = (TextView)findViewById(R.id.textPlayerName);

    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        gameView.saveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);

        gameView.restoreInstanceState(bundle);
        onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    // Called anytime this activity takes the foreground (from initial screen and after bird selects)
    // makes a call to onGameStateChange, which decides the next course of action to take for the game, based on the GameView's Game object
    protected void onResume(){
        super.onResume();

        if(gameView.getGameState() == Game.GameState.init) {
            player1 = getIntent().getExtras().getString("player1");
            player2 = getIntent().getExtras().getString("player2");
        }

        if(gameView.getGameState() == Game.GameState.birdselect){
            gameView.setGameState(Game.GameState.roundA);  // first player goes after this init
        }

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


    public void onClickSet(View view)
    {
        if(gameView.getGameState() == Game.GameState.roundB) {
            gameView.setGameState(Game.GameState.birdselect);
        }
        if(gameView.getGameState() == Game.GameState.roundA) {
            gameView.setGameState(Game.GameState.roundB);
        }

        if( gameView.getGameObject().CheckBirds() ){
            if (currPlayer == Player.Player1){
                winner = player2;
                Log.i("The Winner is ", player2);
            }
            else{
                winner = player1;
                Log.i("The Winner is ", player1);
            }
            gameView.setGameState(Game.GameState.end);  // on a collision true, game is over

        }

        onGameStateChange(gameView);
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
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  // prevent multiples of the same activity. reuse from stack
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                gameView.setGameState(Game.GameState.birdselect);  // start gamestate after initializing at birdselect
                startActivity(intent);

                break;

            case birdselect:
                RoundNumber++;
                intent = new Intent(this, BirdSelectActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("player1", player1);  // pass player names to selection activity for textview to show
                intent.putExtra("player2", player2);
                startActivity(intent);

                break;

            case roundA:
                P1Bird = getIntent().getExtras().getInt("p1bird");
                P2Bird = getIntent().getExtras().getInt("p2bird");  // get from intent the chosen birds, saved to this activity

                if (RoundNumber % 2 != 0) {
                    currPlayer = Player.Player1;
                    playerView.setText(gameView.getGameObject().getNameById(1) + ", place your next bird ");
                    gameView.CreateBird(P1Bird);  // generate P1's bird. This call places the bird object on the end of the game's array of birds and sets it as the current bird to manipulate during touch events
                    //Log.i("Round A Player 1", Integer.toString(P1Bird));
                }
                else {
                    currPlayer = Player.Player2;
                    playerView.setText(gameView.getGameObject().getNameById(2) + ", place your next bird ");
                    gameView.CreateBird(P2Bird); // generate P2's bird. see above comment
                    //Log.i("Round A Player 2", Integer.toString(P2Bird));
                }

                break;

            case roundB:
                playerView.setText(player2 + ", place your next bird - ");

                if (RoundNumber % 2 != 0) {
                    currPlayer = Player.Player2;
                    playerView.setText(gameView.getGameObject().getNameById(2) + ", place your next bird - ");
                    gameView.CreateBird(P2Bird);  // generate P1's bird. This call places the bird object on the end of the game's array of birds and sets it as the current bird to manipulate during touch events
                    //Log.i("Round B Player 2", Integer.toString(P2Bird));
                }
                else {
                    currPlayer = Player.Player1;
                    playerView.setText(gameView.getGameObject().getNameById(1) + ", place your next bird - ");
                    gameView.CreateBird(P1Bird); // generate P2's bird. see above comment
                    //Log.i("Round B Player 2", Integer.toString(P1Bird));
                }
                //gameView.CreateBird(P2Bird);  // generate P2's bird. see above comment

                break;

            case end:
                //TODO: endgame details and restarting of game
                intent = new Intent(this, FinishedActivity.class);
                intent.putExtra("winner", winner);
                //intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                gameView.setGameState(Game.GameState.birdselect);
                startActivity(intent);

            default:

                break; // GameActivity.onReturn() calls this to find current state. in init and birdselect, start bird select activity. on rounds, hand off to view's touch handler. on end, start ending activity
        }
    }

    @Override
    public void onBackPressed() {} // Do nothing if the back button is pressed on this activity

}
