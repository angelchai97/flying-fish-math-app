package com.inti.flyingmathfishapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gamelevel extends AppCompatActivity implements View.OnClickListener{

    private Button easybtn;
    private Button normalbtn;
    private Button difficultbtn;
    public static final String EXTRA_NUMBER = "com.inti.flyingmathfishapp.EXTRA_NUMBER";
   // public static final String EASY_LEVEL = "1";
    // public String getExtranum() {
    //return EXTRA_NUMBER;
//}
    GameLevelNum mGameLevelNum = new GameLevelNum();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelevel);

        easybtn = (Button) findViewById(R.id.easybtn);
        normalbtn = (Button) findViewById(R.id.normalbtn);
        difficultbtn = (Button) findViewById(R.id.difficultbtn);


        easybtn.setOnClickListener(this);
        normalbtn.setOnClickListener(this);
        difficultbtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easybtn:
                mGameLevelNum.setMgamelevel(1);
                Intent intent = new Intent(this , MainActivity.class);
                startActivity(intent);
                break;
            case R.id.normalbtn:
                mGameLevelNum.setMgamelevel(2);
                Intent intent1 = new Intent(this , MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.difficultbtn:
                mGameLevelNum.setMgamelevel(3);
                Intent intent2 = new Intent(this , MainActivity.class);
                startActivity(intent2);
                break;


    }
}
}

   /**public void openEasyActivity(){
        Intent intent = new Intent(this,flyingMathFishView.class);
        int eztext = 1;
        intent.putExtra(EXTRA_NUMBER, eztext);
        startActivity(intent);
    }
}
**/