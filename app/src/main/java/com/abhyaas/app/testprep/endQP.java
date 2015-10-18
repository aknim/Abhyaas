package com.abhyaas.app.testprep;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class endQP extends ActionBarActivity {

    Intent game;
    Button newGame;
    TextView debug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        initialise();
    }

    private void initialise() {
        newGame = (Button) findViewById(R.id.newGame);
        debug = (TextView) findViewById(R.id.debug);
        (newGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                debug.setText("Great you clicked");

                game= new Intent(endQP.this,Game.class) ;
                startActivity(game);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_end, menu);
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
}
