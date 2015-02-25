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

    private String[] players;
    private int[] playerBirdIds;
    private TextView t;
    private int selectedBirdId;
    private enum State {FirstTurn, SecondTurn, Finish};
    private State selectionState;
    private int currentPlayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_bird_select);

        players = new String[2];
        playerBirdIds = new int[2];

        t = (TextView)findViewById(R.id.playerToSelect);

        players[0] = getIntent().getExtras().getString("player1");
        players[1] = getIntent().getExtras().getString("player2");

        currentPlayer = getIntent().getExtras().getInt("currentPlayer");

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putIntArray("playerBirdIds", playerBirdIds);
        bundle.putStringArray("players", players);
        bundle.putSerializable("selectionState", selectionState);
        bundle.putInt("currentPlayer", currentPlayer);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        playerBirdIds = bundle.getIntArray("playerBirdIds");
        players = bundle.getStringArray("players");
        selectionState = (State) bundle.getSerializable("selectionState");
        currentPlayer = bundle.getInt("currentPlayer");


    }

    @Override
    protected void onResume(){
        super.onResume();
        if (selectionState != null) SelectionStateChange(selectionState);
        else SelectionStateChange(State.FirstTurn);
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
                selectedBirdId = R.drawable.ostrich;
                break;
            case R.id.parrot:
                selectedBirdId = R.drawable.parrot;
                break;
            case R.id.robin:
                selectedBirdId = R.drawable.robin;
                break;
            case R.id.swallow:
                selectedBirdId = R.drawable.swallow;
                break;
            case R.id.hummingbird:
                selectedBirdId = R.drawable.hummingbird;
                break;
            default:
                // If you've reached this statement, there's an error!
                selectedBirdId = R.drawable.ic_launcher;
                break;
        }

        SelectionStateChange(selectionState);

    }

    public void SelectionStateChange(State state) {
        selectionState = state;

        switch (state) {
            case FirstTurn:
                t.setText(players[currentPlayer] + ", choose a Bird");

                if (selectedBirdId != 0) {
                    playerBirdIds[currentPlayer] = selectedBirdId;
                    selectedBirdId = 0;
                    currentPlayer = currentPlayer == 0 ? 1 : 0;
                    SelectionStateChange(State.SecondTurn);
                }

                break;

            case SecondTurn:
                t.setText(players[currentPlayer] + ", choose a Bird");

                if (selectedBirdId != 0) {
                    playerBirdIds[currentPlayer] = selectedBirdId;
                    selectedBirdId = 0;
                    SelectionStateChange(State.Finish);
                }

                break;

            case Finish:
                Intent intent = new Intent(this, GameActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("p1bird",playerBirdIds[0]);
                intent.putExtra("p2bird",playerBirdIds[1]);
                selectionState = State.FirstTurn;
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
