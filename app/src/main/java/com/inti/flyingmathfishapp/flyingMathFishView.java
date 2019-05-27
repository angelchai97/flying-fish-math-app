package com.inti.flyingmathfishapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.VibrationEffect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import org.w3c.dom.Text;


public class flyingMathFishView extends View
{
    private Bitmap fish[] = new Bitmap[2];
    private Bitmap digit [] = new Bitmap[10];
    private Bitmap bomb [] = new Bitmap[1];
    private Bitmap shrimp[] = new Bitmap[1];
    private int fishX = 10;
    private int fishY;
    private int fishSpeed;

    private int canvasWidth, canvasHeight;

    private int yellowX, yellowY, yellowSpeed = 16;
    private Paint yellowPaint = new Paint();
    //private Paint WordPaint = new Paint();//not available
    //private String text;

    private int greenX, greenY, greenSpeed = 20;
    private Paint greenPaint = new Paint();

    private int redX, redY, redSpeed = 25;
    private Paint redPaint = new Paint();

    private int score, lifeCounterOFFish;

    private TextView question;


    private boolean touch = false;

    private  Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Paint questionPaint = new Paint();
    private Bitmap life[] = new Bitmap[2];



    @SuppressLint("SetTextI18n")
    public flyingMathFishView(Context context)
    {
        super(context);
        fish[0] = BitmapFactory.decodeResource(getResources(),R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(),R.drawable.fish2);

        digit[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_0);
        bomb[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.bomb);
        shrimp[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.shrimp);

        //question.setText("15+15 = ?");




        backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(true);//false


        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);
        //temporary
        //WordPaint.setTextSize(30);
        //WordPaint.setColor(Color.BLACK);
        //WordPaint.setAntiAlias(true);
        //WordPaint.setTextAlign(Paint.Align.CENTER);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);


        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        questionPaint.setColor(Color.BLACK);
        questionPaint.setTextSize(70);
        questionPaint.setTypeface(Typeface.DEFAULT_BOLD);
        questionPaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);

        fishY = 550;
        score = 0;
        lifeCounterOFFish = 3;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);

        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 1;//supposed to be * 3
        fishY = fishY + fishSpeed;
        if (fishY <minFishY)
        {
            fishY = minFishY;
        }

        if (fishY > maxFishY)
        {
            fishY = maxFishY;
        }
        fishSpeed = fishSpeed + 2;

        if (touch)
        {
            canvas.drawBitmap(fish[1],fishX,fishY, null);
            touch = false;
        }
        else
            {
            canvas.drawBitmap(fish[0],fishX,fishY,null);
        }

        canvas.drawText("1 - 1 = ?",400,195,questionPaint);


        yellowX = yellowX - yellowSpeed;


        if (hitBallChecker(yellowX, yellowY))
        {
            score = score + 10;
            yellowX =  - 100;
        }

        if(yellowX < 0)
        {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random()* maxFishY + minFishY)+ minFishY ; //supposed to be maxFishY-minFishY

        }
        //Rect bounds = new Rect();//none
        //WordPaint.getTextBounds(text,0,text.length(),bounds);//none

        //canvas.drawCircle(yellowX, yellowY, 25, yellowPaint);
        canvas.drawBitmap(digit[0],yellowX,yellowY, null);
       // canvas.drawText(text,yellowX,yellowY,WordPaint);//none
       // yellowPaint.setTextSize(24);


        greenX = greenX - greenSpeed;


        if (hitBallChecker(greenX, greenY))
        {
            if(lifeCounterOFFish<3){
                lifeCounterOFFish++;
            }
            greenX =  - 100;
        }

        if(greenX < 0)
        {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random()* maxFishY + minFishY)+ minFishY ; //supposed to be maxFishY-minFishY

        }

        //canvas.drawCircle(greenX, greenY, 30, greenPaint);
        canvas.drawBitmap(shrimp[0],greenX,greenY, null);

       redX = redX - redSpeed;


        if (hitBallChecker(redX, redY))
        {

           redX =  - 100;
           lifeCounterOFFish--;

            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }

           if(lifeCounterOFFish == 0)
           {
               Toast.makeText(getContext(),"Game Over", Toast.LENGTH_SHORT).show();

               Intent gameOverIntents = new Intent(getContext(),GameOverActivity.class);
               gameOverIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
               getContext().startActivity(gameOverIntents);
           }
        }

        if(redX < 0)
        {
           redX = canvasWidth + 21;
           redY = (int) Math.floor(Math.random()* maxFishY + minFishY)+ minFishY ; //supposed to be maxFishY-minFishY

        }

        //canvas.drawCircle(redX, redY, 30, redPaint);
        canvas.drawBitmap(bomb[0],redX,redY, null);

        canvas.drawText(" Score:"+score,20,60,scorePaint);




        for (int i = 0; i <3;i++)
        {
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i<lifeCounterOFFish)
            {
                canvas.drawBitmap(life[0],x,y,null);
            }
            else{
                canvas.drawBitmap(life[1],x,y,null);
            }
        }






    }

    public boolean hitBallChecker(int x,int y) {
        if (fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < fishY + fish[0].getHeight())
        {
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            touch = true;
            fishSpeed = -28;

        }return true;
    }
}
