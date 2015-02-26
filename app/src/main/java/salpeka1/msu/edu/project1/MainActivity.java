package salpeka1.msu.edu.project1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickRules(View view){
        TextView txt = (TextView)findViewById(R.id.textRules);
        txt.setText(R.string.rules);
    }

    public void onClickStart(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);

        // Passing the player names into the Intent
        EditText player1 = (EditText) findViewById(R.id.player1);
        EditText player2 = (EditText) findViewById(R.id.player2);
        String player1_str = player1.getText().toString();
        String player2_str = player2.getText().toString();
        player1_str = player1_str.isEmpty() ? "Player 1" : player1_str;
        player2_str = player2_str.isEmpty() ? "Player 2" : player2_str;
        intent.putExtra("player1", player1_str);
        intent.putExtra("player2", player2_str);

        startActivity(intent);
    }

}
