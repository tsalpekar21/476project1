package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/*
* This activity needs to be recalled after every round.
* There cannot be multiple versions of this activity pushed onto the stack at one time
 */

public class BirdSelectActivity extends ActionBarActivity {

    private String player1, player2;
    private int P1Bird, P2Bird;
    private TextView t;
    private boolean returnSentinel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_select);

    }

    @Override
    protected void onResume(){
        super.onResume();

        t = (TextView)findViewById(R.id.playerToSelect);
        player1 = getIntent().getExtras().getString("player1");
        player2 = getIntent().getExtras().getString("player2");

        t.setText(player1+", Choose a Bird");
        //   onGameStateChange(gameView);

        returnSentinel = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bird_select, menu);
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

    public void onClickGame(View view)
    {
        // unused.
        //TODO: remove the button from this view. function returns to GameActivity on P2's choice of bird
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float relX = (event.getX());
        float relY = (event.getY());

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                // TODO: Check if a bird was actually touched, and if so which one. Save the bird's ID value to pass back to Game class
                // TODO: If this was P1's bird choice, allow P2 to choose next. Otherwise, allow return to GameActivity
                Selected(R.drawable.robin);  // always returns robin here

                return true;

        }

        return false;
    }

    public void Selected(int birdID){
        if(!returnSentinel){ // on p1's choice
            P1Bird = birdID;
            t.setText(player2+", Now You Choose a Bird");
            returnSentinel = true; // ready to return on next call here
        }
        else{
            P2Bird = birdID;
            Intent intent = new Intent(this, GameActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("p1bird",P1Bird);
            intent.putExtra("p2bird",P2Bird);
            startActivity(intent);
        }
    }
}
