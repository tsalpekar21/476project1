package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class FinishedActivity extends ActionBarActivity {

    private String winner, rounds;
    private TextView endingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        displayText();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finished, menu);
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
        // this button click will, instead of pulling up BirdSelectActivity, lock a bird's location in on the game field and call a check for endgame conditions
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void displayText()
    {
        winner = getIntent().getExtras().getString("winner");
        rounds = getIntent().getExtras().getString("rounds");
        endingText = (TextView)findViewById(R.id.endingText);
        endingText.setText("After a gruelling battle lasting " + rounds + " rounds, " + winner + " has achieved Victory!");
    }

    @Override
    public void onBackPressed() {} // Do nothing if the back button is pressed on this activity
}
