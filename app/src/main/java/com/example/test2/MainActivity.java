package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author OCdt Nethercott 29049
 * @version Dec 2, 2019
 * test change1.2
 */

public class MainActivity extends AppCompatActivity {

    /** int score: tracks the number of mole hits by user */
    static int score;
    /** int speed: used to increase speed of mole movement */
    static int speed = 0;
    /** ArrayList sesList: array list of objects to control the mole movement */
    static ArrayList <ScheduledExecutorService> sesList = new ArrayList();
    /** int sesCount: tracks number of SESs created, IOT kill unused ones */
    static int sesCount = 0;
    /** int widthKiller: used to shrink mole button width */
    static int widthKiller=0;
    /** int widthKiller: used to shrink mole button height */
    static int heightKiller =0;
    /** int time: starting time of the game in seconds */
    static int time = 30;



    /** Method: onCreate
     *
     * Default method for android applications
     *
     * @param savedInstanceState
     *
     * @return void
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = findViewById(R.id.btnStart);
        final Button btnExit = findViewById(R.id.btnExit);
        final Button btnRestart = findViewById(R.id.btnRestart);
        final Button btnMole = findViewById(R.id.btnMole);
        final TextView lblScore = findViewById(R.id.lblScore);
        final ImageView imgBack = findViewById(R.id.imgBack);
        final TextView lblTime = findViewById(R.id.lblTime);

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnStart.setVisibility(View.INVISIBLE);
                begin(btnMole,lblScore, imgBack, lblTime);
                createClock(lblTime, btnMole);
                //btnRestart.setVisibility(View.VISIBLE);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });


        btnRestart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

    }

    /** Method: begin
     *
     * Executes the game, called on start button pressed
     *
     * @param btnMole
     * @param lblScore
     * @param imgBack
     * @param lblTime
     *
     * @return void
     */
    public static void begin(Button btnMole, TextView lblScore, ImageView imgBack, TextView lblTime) {

        final int fieldWidth = imgBack.getMeasuredWidth();
        final int fieldHeight = imgBack.getMeasuredHeight();

        final int moleWidth = btnMole.getWidth();
        final int moleHeight = btnMole.getHeight();

        final Random rdmNum = new Random();

        final Button btnMole1 = btnMole;

        final TextView lblScore1 = lblScore;

        final TextView lblTime1 = lblTime;

        createTimer(fieldWidth, fieldHeight, moleWidth, moleHeight, btnMole, rdmNum);


        btnMole.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                score++;
                lblScore1.setText("Score: " + String.valueOf(score));

                move(fieldWidth, fieldHeight, rdmNum, btnMole1, moleWidth, moleHeight);

                if (speed < 500) {
                    speed = speed + 50;
                }  else if (speed >= 500) {
                    speed = speed + 10;
                    widthKiller += 5;
                    heightKiller += 5;
                    btnMole1.setLayoutParams(new ConstraintLayout.LayoutParams(moleWidth - widthKiller, moleHeight - heightKiller));
                }

                createTimer(fieldWidth, fieldHeight, moleWidth, moleHeight, btnMole1, rdmNum);

            }
        });


    }


    /** Method: move
     *
     * Moves the mole button randomly within the screen.
     *
     * @param fieldWidth
     * @param fieldHeight
     * @param rdmNum
     * @param btnMole
     * @param moleWidth
     * @param moleHeight
     *
     * @return void
     */
    public static void move(int fieldWidth, int fieldHeight, Random rdmNum, Button btnMole, int moleWidth, int moleHeight) {

        int newX;
        int newY;

        final Button btnMole1 = btnMole;

        newX = rdmNum.nextInt(fieldWidth - moleWidth);
        newY = rdmNum.nextInt(fieldHeight - moleHeight);

        btnMole1.setX(newX);
        btnMole1.setY(newY);

    }

    /** Method: createTimer
     *
     * Creates and kills ScheduledServiceExecutor objects as needed IOT speed up
     * the game as the user gets a higher score
     *
     * @param fieldWidth
     * @param fieldHeight
     * @param rdmNum
     * @param btnMole
     * @param moleWidth
     * @param moleHeight
     *
     * @return void
     */
    public static void createTimer (int fieldWidth, int fieldHeight, int moleWidth, int moleHeight, Button btnMole, Random rdmNum) {

        final int fieldWidth1 = fieldWidth;
        final int fieldHeight1 = fieldHeight;
        final int moleWidth1 = moleWidth;
        final int  moleHeight1 = moleHeight;
        final Button btnMole1 = btnMole;
        final Random rdmNum1 = rdmNum;

        if (sesCount > 0) {
            ScheduledExecutorService sesKill = sesList.get(sesCount-1);
            sesKill.shutdownNow();
        }

        sesList.add(Executors.newScheduledThreadPool(1));

        ScheduledExecutorService sesX = sesList.get(sesCount);

        final Runnable mover = new Runnable() {

            public void run() {

                int newX;
                int newY;

                newX = rdmNum1.nextInt(fieldWidth1 - moleWidth1);
                newY = rdmNum1.nextInt(fieldHeight1 - moleHeight1);

                btnMole1.setX(newX);
                btnMole1.setY(newY);
            }
        };

        sesX.scheduleAtFixedRate(mover, 0, 1000 - speed, TimeUnit.MILLISECONDS);

        sesCount++;
    }

    /** Method: createClock
     *
     * Starts the countdown clock for the game, using Timer/Timertask
     *
     * @param lblTime
     * @param btnMole
     *
     * @return void
     */
    public void createClock (TextView lblTime, Button btnMole){ //creates the countdown clock process
        final TextView lblTime1 = lblTime;
        final Button btnMole1 = btnMole;

        final TimerTask timeDown = new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String hold = "Time Left: " + time;
                            lblTime1.setText(hold);
                        }
                    });
                } else {
                    this.cancel();
                    end(btnMole1);
                }
            }
        };

        new Timer().scheduleAtFixedRate(timeDown, 0, 1000);

    }


    /** Method: end
     *
     * Effectively ends the game by making the mole button invisible
     *
     * @param btnMole
     *
     * @return void
     */

    public static void end (Button btnMole) {
        btnMole.setVisibility(View.INVISIBLE);
    }

}
