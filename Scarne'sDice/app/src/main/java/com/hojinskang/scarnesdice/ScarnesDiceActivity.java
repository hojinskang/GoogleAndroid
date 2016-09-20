package com.hojinskang.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ScarnesDiceActivity extends AppCompatActivity {

    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int computerOverallScore = 0;
    private int computerTurnScore = 0;

    private Button rollButton;
    private Button holdButton;
    private Button resetButton;

    private TextView userScore;
    private TextView computerScore;
    private TextView turner;
    private TextView turnScore;
    private TextView message;

    private ImageView diceImage;
    private ImageView diceImage2;

    private int[] diceList = new int[]{R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    private Random random = new Random();

    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarnes_dice);

        rollButton = (Button) findViewById(R.id.roll_button);
        holdButton = (Button) findViewById(R.id.hold_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        userScore = (TextView) findViewById(R.id.user_score);
        computerScore = (TextView) findViewById(R.id.computer_score);
        turner = (TextView) findViewById(R.id.turner);
        turnScore = (TextView) findViewById(R.id.turn_score);
        message = (TextView) findViewById(R.id.message);

        diceImage = (ImageView) findViewById(R.id.dice);
        diceImage2 = (ImageView) findViewById(R.id.dice2);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdButton.setEnabled(true);

                Integer rand = random.nextInt(diceList.length - 1) + 0;
                Integer rand2 = random.nextInt(diceList.length - 1) + 0;
                diceImage.setImageResource(diceList[rand]);
                diceImage2.setImageResource(diceList[rand2]);

                int diceValue = rand + 1;
                int diceValue2 = rand2 + 1;
                if (diceValue == 1 && diceValue2 == 1) {
                    userTurnScore = 0;
                    userOverallScore = 0;
                    turnScore.setText(String.valueOf(userTurnScore));
                    userScore.setText("0");
                    message.setText("You rolled a " + String.valueOf(diceValue) + " and " + String.valueOf(diceValue2));
                    computerTurn();
                } else if (diceValue == 1 || diceValue2 == 1) {
                    userTurnScore = 0;
                    turnScore.setText(String.valueOf(userTurnScore));
                    message.setText("You rolled a " + String.valueOf(diceValue) + " and " + String.valueOf(diceValue2));
                    computerTurn();
                } else {
                    if (diceValue == diceValue2) {
                        holdButton.setEnabled(false);
                    }
                    userTurnScore += diceValue;
                    turnScore.setText(String.valueOf(userTurnScore));
                    if (userOverallScore >= 100)
                        message.setText("You win");
                    else
                        message.setText("You rolled a " + String.valueOf(diceValue) + " and " + String.valueOf(diceValue2));
                }
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    userOverallScore += userTurnScore;
                    turnScore.setText("0");
                    userScore.setText(String.valueOf(userOverallScore));

                    userTurnScore = 0;

                    turner.setText("Computer's");
                message.setText("Computer's turn");

                computerTurn();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOverallScore = 0;
                userTurnScore = 0;
                computerOverallScore = 0;
                computerTurnScore = 0;

                userScore.setText("0");
                computerScore.setText("0");
                turner.setText("Your");
                turnScore.setText("0");
                message.setText("Your turn");
            }
        });
    }

    public void computerTurn() {
        holdButton.setEnabled(false);
        resetButton.setEnabled(false);

        turner.setText("Computer's");

        final long startTime = 0;
        timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                Integer rand = random.nextInt(diceList.length - 1) + 0;
                Integer rand2 = random.nextInt(diceList.length - 1) + 0;
                diceImage.setImageResource(diceList[rand]);
                diceImage2.setImageResource(diceList[rand2]);

                int diceValue = rand + 1;
                int diceValue2 = rand2 + 1;
                // roll
                if (diceValue == 1 && diceValue2 == 1) {
                    computerOverallScore = 0;
                    turnScore.setText("0");
                    computerScore.setText("0");
                    turner.setText("Your");

                    holdButton.setEnabled(true);
                    resetButton.setEnabled(true);
                } else if (diceValue == 1) {
                    computerTurnScore = 0;
                    turnScore.setText("0");
                    turner.setText("Your");

                    holdButton.setEnabled(true);
                    resetButton.setEnabled(true);
                } else {
                    computerTurnScore += diceValue;
                    turnScore.setText(String.valueOf(computerTurnScore));
                    if (computerOverallScore >= 100) {
                        message.setText("Computer wins");
                    } else {
                        message.setText("Computer rolled a " + String.valueOf(diceValue) + " and " + String.valueOf(diceValue2));

                        if (computerTurnScore >= 20) {
                            // hold
                            computerOverallScore += computerTurnScore;
                            turnScore.setText("0");
                            computerScore.setText(String.valueOf(computerOverallScore));
                            message.setText("Your turn");

                            computerTurnScore = 0;

                            turner.setText("Your");

                            holdButton.setEnabled(true);
                            resetButton.setEnabled(true);
                        } else {
                            timerHandler.postDelayed(this, 2000);
                        }
                    }
                }
            }
        };
        timerRunnable.run();
    }
}
