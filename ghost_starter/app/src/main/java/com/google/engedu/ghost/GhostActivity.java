package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStart(null);
            }
        });
        final Button challengeButton = (Button) findViewById(R.id.challengeButton);
        challengeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                challenge(null);
            }
        });
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            TextView text = (TextView) findViewById(R.id.ghostText);
            char c = (char) event.getUnicodeChar();
            String word = text.getText().toString() + c;
            text.setText(word);
            if (dictionary.isWord(word)) {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                label.setText("You got a word!");
            }
            userTurn = false;
            TextView label = (TextView) findViewById(R.id.gameStatus);
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
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

    /**
     * Handler for the "Challenge" button
     */
    public boolean challenge(View view) {
        TextView text = (TextView) findViewById(R.id.ghostText);
        String word = text.getText().toString();
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (word.length() >= 4 && dictionary.isWord(word)) {
            label.setText("You win!");
        } else {
            String dictWord = dictionary.getGoodWordStartingWith(word);
            if (dictWord == null) {
                label.setText("You win!");
            } else {
                text.setText(dictWord);
                label.setText("Computer wins!");
            }
        }
        return true;
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView text = (TextView) findViewById(R.id.ghostText);
        //Log.d("COMPUTER TURN", text.getText().toString());
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String word = text.getText().toString();
        if (word.length() >= 4 && dictionary.isWord(word)) {
            label.setText("Computer wins!");
        } else {
            String dictWord = dictionary.getGoodWordStartingWith(word);
            if (dictWord == null) {
                //Log.d("COMPUTER TURN", "NULL");
                challenge(null);
            } else {
                //Log.d("COMPUTER TURN", dictWord);
                text.setText(dictWord.substring(0, word.length()+1));
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }
}
