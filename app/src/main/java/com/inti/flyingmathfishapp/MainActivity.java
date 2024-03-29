package com.inti.flyingmathfishapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private flyingMathFishView gameView;
    private Handler handler = new Handler();
    private final static long Interval = 30;


    //cf pass number 2 line
   // Intent intent = getIntent();
    //int gamelevel = intent.getIntExtra(Gamelevel.EXTRA_NUMBER,0);


   // public int getGamelevel() {
    //    return gamelevel;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new flyingMathFishView(this);
        setContentView(gameView);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();


                    }
                });

            }
        },0,Interval);
    }
}
