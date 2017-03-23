package com.rinsarz.braintrainer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startBtn;
    Button restartBtn;
    CountDownTimer timer;
    TextView timerView;
    TextView taskview;
    TextView [] answers;
    TextView scoreView;
    TextView statusView;

    TextView [] errs;
    ImageView [] helpViews;

    int maxTimesec = 30;
    int answer;
    int correctAns;
    int errors;
    int totalAns;
    ArrayList <Integer> colors;

    boolean isTimerActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }
    public void init(){
        startBtn  = (Button)findViewById(R.id.startBtn);
        startBtn.setVisibility(View.VISIBLE);

        restartBtn  = (Button)findViewById(R.id.restartBtn);
        restartBtn.setVisibility(View.INVISIBLE);

        timerView = (TextView)findViewById(R.id.timerView);
        timerView.setText("30");

        taskview = (TextView)findViewById(R.id.taskView);
        taskview.setText("");

        scoreView = (TextView)findViewById(R.id.scoreView);
        scoreView.setText("0/30");

        statusView = (TextView)findViewById(R.id.statusView);
        statusView.setText("");

        helpViews = new ImageView[2];
        helpViews[0] = (ImageView)findViewById(R.id.helpView1);
        helpViews[1] = (ImageView)findViewById(R.id.helpView2);

        for (int i = 0; i < 2; i++){
            helpViews[i].setVisibility(View.VISIBLE);
            helpViews[i].setAlpha(0.5f);
        }

        answers = new TextView[4];
        answers[0] = (TextView)findViewById(R.id.ans1);
        answers[1] = (TextView)findViewById(R.id.ans2);
        answers[2] = (TextView)findViewById(R.id.ans3);
        answers[3] = (TextView)findViewById(R.id.ans4);

        errs = new TextView[3];
        errs[0] = (TextView)findViewById(R.id.err1);
        errs[1] = (TextView)findViewById(R.id.err2);
        errs[2] = (TextView)findViewById(R.id.err3);

        fullErrsView();


        colors = new ArrayList();
        colors.add(Color.parseColor("#ff669900"));
        colors.add(Color.parseColor("#ff0099cc"));
        colors.add(Color.parseColor("#ffff8800"));
        colors.add(Color.parseColor("#ffaa66cc"));






        isTimerActive = false;
    }

    public void restart(View view){
        timer.cancel();
        init();
    }

    public void start(View view){
        init();
        startBtn.setVisibility(View.INVISIBLE);
        restartBtn.setVisibility(View.VISIBLE);

        for (int i = 0; i < 2; i++){
            helpViews[i].setVisibility(View.INVISIBLE);
        }


        correctAns = 0;
        totalAns = 0;
        errors = 0;
        isTimerActive = true;

        timer = new CountDownTimer((maxTimesec + 1)*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(Integer.toString((int)millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                stopGame();
            }
        }.start();

        makeQuestion();
    }

    public void fullErrsView(){
        for (int i = 0; i < 3; i++){
            errs[i].setBackgroundColor(Color.parseColor("#ff669900"));
        }
    }

    public void stopGame(){
        isTimerActive = false;
        startBtn.setVisibility(View.VISIBLE);
        restartBtn.setVisibility(View.INVISIBLE);
        timerView.setText("00");
        taskview.setText(" ");

        if(correctAns > 29){
            statusView.setText("Excellent!");
            statusView.setTextColor(Color.parseColor("#ff669900"));
        } else if(correctAns > 25){
            Toast.makeText(MainActivity.this, "You are trained!", Toast.LENGTH_SHORT).show();
            statusView.setText("Good!");
            statusView.setTextColor(Color.parseColor("#ff669900"));
        }else if(correctAns > 15){
            statusView.setText("Average!");
            statusView.setTextColor(Color.parseColor("#ff669900"));
        } else if(errors == 3 || correctAns <= 15){
            Toast.makeText(MainActivity.this, "You have failed!", Toast.LENGTH_SHORT).show();
            statusView.setText("Bad!");
            statusView.setTextColor(Color.RED);
        }
    }

    public void changeStatus(){

    }

    public void check(View view){
        if(!isTimerActive){
            return;
        }
        int ans = Integer.parseInt(((TextView)view).getText().toString());
        if (ans == answer){
            correctAns+=1;
        }else{
            errs[errors].setBackgroundColor(Color.RED);
            errors++;
            if (errors > 2){
                timer.cancel();
                stopGame();
            }
        }
        totalAns+=1;

        updateScoreView();
        makeQuestion();
    }

    public void updateScoreView(){
        scoreView.setText(correctAns + "/" + totalAns);
    }

    public void makeQuestion (){
        Random rnd=  new Random();
        int first = rnd.nextInt(20)+2;
        int second = rnd.nextInt(20) + 2;

        answer = first + second;

        List answersList = new ArrayList();
        answersList.add(answer);
        int next = 1;

        while(answersList.size() < 4){
            next = rnd.nextInt(answer)+1;
            if (!answersList.contains(next)) {
                answersList.add(next);
            }
        }


        Collections.shuffle(answersList);
        Collections.shuffle(colors);

        for (int i = 0; i < 4; i++){
            answers[i].setText(answersList.get(i).toString());
            answers[i].setBackgroundColor(colors.get(i));
        }


        taskview.setText(first + " + " + second);



    }
}
