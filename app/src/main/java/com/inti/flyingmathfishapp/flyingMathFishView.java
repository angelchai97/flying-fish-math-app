package com.inti.flyingmathfishapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.VibrationEffect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class flyingMathFishView extends View
{
    // All canvas resource
    private Bitmap fish[] = new Bitmap[2];
    private Bitmap digit [] = new Bitmap[10];
    private Bitmap bomb [] = new Bitmap[1];
    private Bitmap shrimp[] = new Bitmap[1];
    private  Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Paint questionPaint = new Paint();
    private Bitmap life[] = new Bitmap[2];
    private Paint AddScorePaint = new Paint();
    private int lifeX, lifeY, lifeSpeed = 16;
    private Paint lifePaint = new Paint();
    private Paint digitPaint = new Paint();

    // Value for fish controller movement
    private int fishX = 10;
    private int fishY;
    private int fishSpeed;


    //value needed for Design Question before finalize
    private String question;
    int number1;
    int number2;
    private int ans;
    private String operator;
    private String[] quiz;
    private int[] digitNumber;

    // From Select Game Level layout , when user click Level button assign value and send it to gameLevel to decide the game level
    // gameLevel == 1 is easy mode
    // gameLevel == 2 is Normal Mode
    // gameLevel == 3 is Hard Mode
    private  int gameLevel;

    // Finalized Question statement for Display
    private String statement;

    // Classes for generate random number with probability
    DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator(); // for digit ans
    ProbabilityLife probabilityLife = new ProbabilityLife(); // for life

    private int canvasWidth, canvasHeight;

    // Generate 4 random digits as user answer
    private ArrayList<Integer> ramdonNum = new ArrayList<>();
    private int random,random1,random2,random3,random4;
    private int digit1X, digit1Y, digit1Speed = 16;
    private int digit2X, digit2Y, digit2Speed= 16;
    private int digit3X, digit3Y, digit3Speed= 16;
    private int digit4X, digit4Y, digit4Speed= 16;

    // Random Location for each digit answer
    private ArrayList<Integer> locationY = new ArrayList<>(); // List that
    private ArrayList<Integer> locationX = new ArrayList<>();


    private int score, lifeCounterOFFish;


    private boolean touch = false;





    @SuppressLint("SetTextI18n")
    public flyingMathFishView(Context context)
    {
        super(context);

        // Assign all image
        fish[0] = BitmapFactory.decodeResource(getResources(),R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(),R.drawable.fish2);
        digit[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_0);
        digit[1] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_1);
        digit[2] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_2);
        digit[3] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_3);
        digit[4] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_4);
        digit[5] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_5);
        digit[6] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_6);
        digit[7] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_7);
        digit[8] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_8);
        digit[9] = BitmapFactory.decodeResource(getResources(),R.mipmap.digit_9);
        bomb[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.bomb);
        shrimp[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.shrimp);
        backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);

        // Assign all Styling
        digitPaint.setAntiAlias(true);//false

        AddScorePaint.setColor(Color.BLACK);
        AddScorePaint.setTextSize(300);
        AddScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        AddScorePaint.setAntiAlias(true);

        lifePaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        questionPaint.setColor(Color.BLACK);
        questionPaint.setTextSize(70);
        questionPaint.setTypeface(Typeface.DEFAULT_BOLD);
        questionPaint.setAntiAlias(true);

        // initial Setting for fish location, score and number of life
        fishY = 550;
        score = 0;
        lifeCounterOFFish = 3;

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();


        canvas.drawBitmap(backgroundImage,0,0,null);

        // Fish Controller
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

        // Display Question Part
        // Initial Question
        if(statement == null){
            RefreshQuestion();
        }
        canvas.drawText(statement,400,195,questionPaint); // Draw the Question

        Context c = getContext();
        // Check if the User has fill up all the "X" for the answer
        // If fill up the "X" mean have select all the digit for the Answer return true
        if (checkX(statement)) {
            // check the Answer and refresh question
            boolean answer = checkAns(statement);
            if (answer== true){
                Toast.makeText(c,"Correct" , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(c,"Answer is "+ quiz[1] , Toast.LENGTH_LONG).show();
            }

            RefreshQuestion();
        }


        // Random generate 4 number to assign 4 location y-axis for each of the digit answer and store it inside a list for later use
        for (int i = 0; i < 6; i++)
        {
            // Make sure random number no repeated
            while(true)
            {
                Integer nextY = (int) Math.floor(Math.random() * maxFishY + minFishY) + minFishY + 400;
                if (!locationY.contains(nextY))
                {
                    // Done for this iteration
                    locationY.add(nextY);
                    break;
                }
            }
        }

        // Random generate 4 number to assign 4 location x-axis for each of the digit answer and store it inside a list for later use
        for (int i = 0; i < 6; i++)
        {
            // Make sure random number no repeated
            while(true)
            {
                Integer nextX = canvasWidth + new Random().nextInt((100 - 21) + 1) + 21;
                if (!locationX.contains(nextX))
                {
                    // Done for this iteration
                    locationX.add(nextX);
                    break;
                }
            }
        }

        // Get Random number for the 4 digit Answer and then store them into list for later use
        for (int x = 0; x < 4;x++) {
            random = drng.getDistributedRandomNumber(); // Generate a random number
            ramdonNum.add(random);
        }

        // 1st Digit Answer Setting
        digit1X = digit1X - digit1Speed;
        //If 1st Digit being touch by Fish
        if (hitDigitChecker(digit1X, digit1Y, random1))
        {
            digit1X =  - 100;// If touch, digit will disappear
        }
        // Set location for 1st Digit Answer
        if(digit1X < 0)
        {
            digit1X = canvasWidth + 21;
            digit1Y = locationY.get(0);
            random1 = ramdonNum.get(0); //Get a number from randomNum list to represent its Digit
        }

        // 2nd Digit Answer Setting
        digit2X = digit2X - digit2Speed;
        //If 2nd Digit being touch by Fish
        if (hitDigitChecker(digit2X, digit2Y,random2))
        {
            digit2X =  - 100;// If touch, digit will disappear
        }
        // Set location for 2nd Digit Answer
        if(digit2X < 0)
        {
            digit2X = canvasWidth + 21;
            digit2Y = locationY.get(1);
            random2 = ramdonNum.get(1);//Get a number from randomNum list to represent its Digit
        }

        // 3rd Digit Answer Setting
        digit3X = digit3X - digit3Speed;
        //If 3rd Digit being touch by Fish
        if (hitDigitChecker(digit3X, digit3Y,random3))
        {
            digit3X =  - 100;// If touch, digit will disappear
        }
        // Set location for 3rd Digit Answer
        if(digit3X < 0)
        {
            digit3X = canvasWidth + 21;
            digit3Y = locationY.get(2);
            random3 = ramdonNum.get(2);//Get a number from randomNum list to represent its Digit
        }

        // 4th Digit Answer Setting
        digit4X = digit4X - digit4Speed;
        //If 4th Digit being touch by Fish
        if (hitDigitChecker(digit4X, digit4Y,random4))
        {
            digit4X =  - 100;// If touch, digit will disappear
        }
        // Set location for 4th Digit Answer
        if(digit4X < 0)
        {
            digit4X = canvasWidth + 21;
            digit4Y = locationY.get(3);
            random4 = ramdonNum.get(3);//Get a number from randomNum list to represent its Digit
        }

        // Setting for Life
        lifeX = lifeX - lifeSpeed;
        //If Life(Shrimp) being touch by Fish
        if (hitLifeChecker(lifeX, lifeY))
        {
            if(lifeCounterOFFish<3){
                lifeCounterOFFish++;// Add life if life number is not 3
            }
            lifeX =  - 100;// If touch, shrimp will disappear
        }
        // Set location for Life
        if(lifeX < 0)
        {
            lifeX = canvasWidth + locationX.get(4);
            lifeY = locationY.get(4) ;

        }

        // Add probability for Life to being display
        probabilityLife.addPropability(1,0.5);// Probability for Life to display
        probabilityLife.addPropability(0,0.6);// Probability for Life to not be display
        int probability = probabilityLife.getDistributedRandomPropability();
        if(probability ==1) {
            canvas.drawBitmap(shrimp[0], lifeX, lifeY, null);
        }

        // Display the Digit Answer
        canvas.drawBitmap(digit[random1], digit1X, digit1Y, null);
        canvas.drawBitmap(digit[random2], digit2X, digit2Y, null);
        canvas.drawBitmap(digit[random3], digit3X, digit3Y, null);
        canvas.drawBitmap(digit[random4], digit4X, digit4Y, null);

        // Display the Score
        canvas.drawText(" Score:"+score,20,60,scorePaint);



        //Display Life
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

        // Clear List for new round
        locationY.clear();
        locationX.clear();
        ramdonNum.clear();
    }


    @SuppressLint("NewApi")
    private void RefreshQuestion() {
        quiz = Question();// Get Question eg. ["10 + 2 =","12"]
        statement = String.valueOf(quiz[0]);// Get the Question statement eg. "10 + 2 ="

        // Get Question Answer eg. "12", then break them into digit by digit eg. ["1", "2"]
        digitNumber = String.valueOf(quiz[1]).chars().map(Character::getNumericValue).toArray();


        // Initial Add probability for each digit 0 to 9 to be display as Digit Answer, to make sure every digit got chance to being display
        for (int n = 0; n < 10; n++) {
            drng.addNumber(n, 0.2d);// Adds the digit with a probability of 0.2 (20%)
        }

        // Overwrite and increase probability for the each digit of the real answer eg. ["1", "2"]
        for (int i = 0; i < digitNumber.length; i++) {
            // Insert number of place for the Question eg."10 + 2 =" , answer is 12 so got 2 answer place then add 2 "X" as answer place
            // Final output will be "10 + 2 = XX"
            statement = new StringBuilder(statement).insert(statement.length()-1, "X").toString();
            drng.addNumber(digitNumber[i], 0.5d);// Adds the Real Answer digit with a probability of 0.5 (50%)
        }
    }

    // Check whether the place of answer is fully fill up
    private boolean checkX(String x) {
        if(!(x.contains("X"))){
            return true;
        }
        return false;
    }

    // Check the Answer
    private boolean checkAns(String s) {
        String result = s.substring(s.lastIndexOf('=') + 1).trim();
        if (result.equals(String.valueOf(ans))){
            // If answer correct, score increase by 10 points
            score = score + 10;
            return true;
        }else{

            // If answer incorrect, Life decrease by 1
            lifeCounterOFFish--;
            Vibrate();
            checkLife(); //Check if the life is 0
            return false;
        }
    }

    //Check if the life is 0, if yes intend to Game Over Layout
    private void checkLife() {
        if(lifeCounterOFFish == 0)
        {
            Toast.makeText(getContext(),"Game Over", Toast.LENGTH_SHORT).show();

            Intent gameOverIntents = new Intent(getContext(),GameOverActivity.class);
            gameOverIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(gameOverIntents);
        }
    }

    // When decreasing Life make the phone vibrate to inform user
    private void Vibrate() {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    // Set Question, store them in list as question and answer eg.["10 + 2 =","12"]
    private String[] Question() {
        operator = QuestionOperator(); // Get the Question operator +,-,x,÷
        // Get the number to build the question, operator parameter is to determine the number of digit based on game level
        int questionNum[] = QuestionNumber(operator);
        if (operator.equals("-") || operator.equals("÷"))  {
            if (questionNum[0] > questionNum[1]){
                if(operator.equals("-")) {
                    ans = questionNum[0] - questionNum[1];
                }else{
                    ans = questionNum[0] / questionNum[1];
                }
                question = questionNum[0]+" " + operator +" "+ questionNum[1] + " = ";
            }else{
                if(operator.equals("-")) {
                    ans = questionNum[1] - questionNum[0];
                }else{
                    ans = questionNum[1] / questionNum[0];
                }
                question = questionNum[1] +" "+ operator + questionNum[0] +" "+ " = ";
            }
        }else {
            if(operator.equals("+")) {
                ans = questionNum[0] + questionNum[1];
            }else{
                ans = questionNum[0] * questionNum[1];
            }
            question = questionNum[0] +" "+ operator +" "+ questionNum[1]+ " = ";
        }
        String ar[] = new String[2];
        ar[0] = question;
        ar[1] = String.valueOf(ans);
        return ar;
    }

    // Generate Random number for Question based on Game Mode
    private int[] QuestionNumber(String operator) {
        Random digit = new Random();
        if(gameLevel ==1){
            number1 = digit.nextInt(9 - 0) + 0;
            number2 = digit.nextInt(9 - 0) + 0;
        }else if (gameLevel == 2){
            if(operator == "+" || operator == "-") {
                number1 = digit.nextInt(99 - 10) + 10;
                number2 = digit.nextInt(99 - 10) + 10;
            }else{
                while(true) {
                    number1 = digit.nextInt(99 - 10) + 10;
                    number2 = digit.nextInt(12 - 1) + 1;
                    if (operator == "×") {
                        if(!((number1*number2)>= 1000)){
                            break;
                        }
                    } else if(operator == "÷") {
                        if(number1 % number2 == 0){
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }

        }else{
            if(operator == "+" || operator == "-") {
                number1 = digit.nextInt(900 - 100) + 100;
                number2 = digit.nextInt(99 - 10) + 10;
            }else{
                while(true) {
                    number1 = digit.nextInt(999 - 100) + 100;
                    number2 = digit.nextInt(12 - 1) + 1;
                    if (operator == "×") {
                        if(!((number1*number2)>= 1000)){
                            break;
                        }
                    } else if(operator == "÷") {
                        if(number1 % number2 == 0){
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        }
        return new int[] {number1, number2};
    }

    // Generate Question operator and store them in a list
    private String QuestionOperator() {
        List<String> operatorMath = new ArrayList<>();
        if(gameLevel == 1) {
            operatorMath.add("+");
            operatorMath.add("-");
        }else {
            operatorMath.add("+");
            operatorMath.add("-");
            operatorMath.add("×");
            operatorMath.add("÷");
        }
        Random rand = new Random();
        return operatorMath.get(rand.nextInt(operatorMath.size()));
    }

    // Checker when fish hit Digit Answer
    public boolean hitDigitChecker(int x,int y, int r) {
        if (fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < fishY + fish[0].getHeight())
        {
            // replace "X" with digit that being hit
            statement = statement.replaceFirst("X", String.valueOf(r));
            return true;
        }
        return false;
    }

    // Checker when fish hit Shrimp
    public boolean hitLifeChecker(int x, int y) {
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
