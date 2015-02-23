package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    public void onSaveInstanceState(Bundle bundle) {

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

    public void onBirdSelect(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ostrich:
                Selected(R.drawable.ostrich);
                break;
            case R.id.parrot:
                Selected(R.drawable.parrot);
                break;
            case R.id.robin:
                Selected(R.drawable.robin);
                break;
            case R.id.swallow:
                Selected(R.drawable.swallow);
                break;
            case R.id.hummingbird:
                Selected(R.drawable.hummingbird);
                break;
            default:
                // If you've reached this statement, there's an error!
                Selected(R.drawable.ic_launcher);
                break;
        }

    }

    public void Selected(int birdID){
        if(!returnSentinel){ // on p1's choice
            P1Bird = birdID;
            t.setText(player2+", Now You Choose a Bird");
            returnSentinel = true; // ready to return on next call here
        } else {
            P2Bird = birdID;
            Intent intent = new Intent(this, GameActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("p1bird",P1Bird);
            intent.putExtra("p2bird",P2Bird);
            startActivity(intent);
        }
    }
}
