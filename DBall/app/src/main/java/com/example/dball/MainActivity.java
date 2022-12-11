package com.example.dball;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView rightButton,leftButton;
    private LinearLayout carBox;
    private ImageView[] hearts;
    private int index;
    private int lives;
    private int carPositionNum;
    private TableLayout rocks;
    private Timer clock;
    private int delay = 1000;
    private Random rnd ;
    private final int HEARTS_NUM = 3;
    private final int COLS = 3;
    private final int RATE = 3;
    private final int ROWS = 7;
    private final int MAX_LINES_NUM = 3;
    private final int MIN_LINES_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

        Find();

        onClick();

        GenerateRocks();
    }

    private void GenerateRocks() {
        clock = new Timer();
        clock.schedule(new TimerTask() {
            @Override
            public void run() {
                InTimer();
            }

        }, 0, delay);
    }

    private void InTimer() {
        this.runOnUiThread(new Runnable() {
            public void run() {

                if(index % RATE == 0 )
                    showRock(0, getRandomRockPos());

                updateRocks();
                checkHit();
                index++;
            }
        });
    }

    private void updateRocks() {
        //update rocks location
        for(int i = index%RATE; i < rocks.getChildCount(); i+=RATE){
            TableRow row = (TableRow) rocks.getChildAt(i);
            for(int j = 0 ; j < row.getChildCount(); j++){
                ImageView img = (ImageView) row.getChildAt(j);
                //image visible then invisible and visible the image below it
                if(img.getVisibility() == View.VISIBLE){
                    img.setVisibility(View.INVISIBLE);
                    if(i + 1 < rocks.getChildCount())
                        showRock(i+1, j);
                }
            }
        }
    }

    private void checkHit() {
        //check if car crashed
        TableRow row = (TableRow) rocks.getChildAt(ROWS-2);

        for(int i = 0; i < row.getChildCount(); i++){
            ImageView img = (ImageView) row.getChildAt(i);
            //car is crashed
            if(img.getVisibility() == View.VISIBLE && carPositionNum == i+1){
                lives += 1;
                // if still have hears decrement by one
                if(lives < HEARTS_NUM) {
                    //playSound(R.raw.crash);// play crash sound
                    hearts[HEARTS_NUM - lives].setVisibility(View.INVISIBLE);
                }
                else{
                    // no hears left then game over
                    hearts[0].setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Game Over", Toast.LENGTH_SHORT).show();
                    clock.cancel();
                    // playSound(R.raw.game_over);//play game over sound
                }
            }
        }

    }

    private void showRock(int i, int j) {
        // visible rock image in i,j index
        TableRow row = (TableRow) rocks.getChildAt(i);
        ImageView img  = (ImageView) row.getChildAt(j);
        img.setVisibility(View.VISIBLE);
    }

    public int getRandomRockPos(){
        //get random number of cols
        return rnd.nextInt(COLS);
    }

    private void Init() {
        carPositionNum = 2; // 1 - line 1, 2 - line 2, 3 - line 3
        index = 0;
        rnd = new Random();
        hearts = new ImageView[HEARTS_NUM];
        lives = 0;

    }

    private void onClick() {
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(carPositionNum < MAX_LINES_NUM) {
                    JumpRight();
                }
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(carPositionNum > MIN_LINES_NUM) {
                    JumpLeft();
                }
            }
        });

    }

    private void JumpLeft() {
        carPositionNum--;
        updateCarPosition();
    }

    private void JumpRight() {
        carPositionNum++;
        updateCarPosition();
    }

    private void updateCarPosition() {
        for(int i = 0; i < carBox.getChildCount(); i++){
            ImageView car = (ImageView) carBox.getChildAt(i);
            car.setVisibility(View.INVISIBLE);
        }

        ((ImageView) carBox.getChildAt(carPositionNum - 1)).setVisibility(View.VISIBLE);
    }

    private void Find() {
        rightButton = (ImageView)findViewById(R.id.Button_right);// Initialize right button
        leftButton = (ImageView)findViewById(R.id.Button_left);// Initialize left button
        carBox = (LinearLayout)findViewById(R.id.car);//Initialize car
        rocks = findViewById(R.id.Table_rocks);
        hearts[0] = findViewById(R.id.heart1);
        hearts[1] = findViewById(R.id.heart2);
        hearts[2] = findViewById(R.id.heart3);

    }
}