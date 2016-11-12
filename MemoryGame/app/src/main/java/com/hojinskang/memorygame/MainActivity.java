package com.hojinskang.memorygame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TableLayout grid;
    private ImageView[][] blockGrid;
    private TextView message;
    private int[][] answer;
    private boolean win;

    private Handler handler = new Handler();
    private Runnable runner = new Runnable() {
        @Override
        public void run() {
            resetGame();
        }
    };

    private Button startButton, checkButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (TableLayout)findViewById(R.id.Grid);

        startButton = (Button) findViewById(R.id.start_button);
        checkButton = (Button) findViewById(R.id.check_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        message = (TextView) findViewById(R.id.message);

        blockGrid = new ImageView[][]{
                {(ImageView) findViewById(R.id.block1x1), (ImageView) findViewById(R.id.block1x2), (ImageView) findViewById(R.id.block1x3), (ImageView) findViewById(R.id.block1x4)},
                {(ImageView) findViewById(R.id.block2x1), (ImageView) findViewById(R.id.block2x2), (ImageView) findViewById(R.id.block2x3), (ImageView) findViewById(R.id.block2x4)},
                {(ImageView) findViewById(R.id.block3x1), (ImageView) findViewById(R.id.block3x2), (ImageView) findViewById(R.id.block3x3), (ImageView) findViewById(R.id.block3x4)},
                {(ImageView) findViewById(R.id.block4x1), (ImageView) findViewById(R.id.block4x2), (ImageView) findViewById(R.id.block4x3), (ImageView) findViewById(R.id.block4x4)}
        };

        answer = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                blockGrid[i][j].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ColorDrawable drawable = (ColorDrawable) v.getBackground();
                        if (drawable.getColor() == Color.BLACK) {
                            v.setBackgroundColor(Color.RED);
                        }
                    }
                });
            }
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
                handler.postDelayed(runner, 3000);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGrid();
            }
        });
    }

    protected void resetGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                blockGrid[i][j].setBackgroundColor(Color.BLACK);
            }
        }
        message.setText("Press START to begin");
    }

    protected void startGame() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (random.nextBoolean()) {
                    blockGrid[i][j].setBackgroundColor(Color.RED);
                    answer[i][j] = 1;
                } else {
                    blockGrid[i][j].setBackgroundColor(Color.BLACK);
                    answer[i][j] = 0;
                }
            }
        }
        message.setText("Memorize which blocks are RED");
    }

    protected void checkGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ColorDrawable drawable = (ColorDrawable) blockGrid[i][j].getBackground();
                if ((drawable.getColor() == Color.RED && answer[i][j] == 1)
                        || (drawable.getColor() == Color.BLACK && answer[i][j] == 0)) {
                    win = true;
                } else {
                    win = false;
                }
            }
        }

        if (win) {
            message.setText("You win!");
        } else {
            message.setText("You lose!");
        }
    }

}
