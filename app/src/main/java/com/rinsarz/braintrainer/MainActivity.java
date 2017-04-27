package com.rinsarz.braintrainer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GameSettings gameSettings = new GameSettings();
    InterfaceManager interfaceManager = new InterfaceManager(this);


    int answer;
    int correctAns;
    int errors;
    int totalAns;
    CountDownTimer timer;


    boolean isTimerActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.settings:
                Log.i("Menu item selected", "Settings");
                return true;
            case R.id.help:
                Log.i("Menu item selected", "Help");
                return true;
            default:
                return false;
        }
    }

    public void init(){
        interfaceManager.initializeInterface();
        isTimerActive = false;
    }

    public void restart(View view){
        timer.cancel();
        init();
    }

    public void start(View view){
        init();
        interfaceManager.startBtn.setVisibility(View.INVISIBLE);
        interfaceManager.restartBtn.setVisibility(View.VISIBLE);

        for (int i = 0; i < 2; i++){
            interfaceManager.helpViews[i].setVisibility(View.INVISIBLE);
        }


        correctAns = 0;
        totalAns = 0;
        errors = 0;
        isTimerActive = true;

        timer = new CountDownTimer((gameSettings.MAX_TIMER + 1)*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                interfaceManager.timerView.setText(Integer.toString((int)millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                stopGame();
            }
        }.start();

        makeQuestion();
    }

    public void stopGame(){
        isTimerActive = false;
        GameSettings.GAME_OVER_STATUS status = GameSettings.GAME_OVER_STATUS.BAD;

        if(correctAns >= gameSettings.EXCELLENT_SCORE){
            status = GameSettings.GAME_OVER_STATUS.EXCELLENT;
        } else if(correctAns > gameSettings.GOOD_SCORE){
            status = GameSettings.GAME_OVER_STATUS.GOOD;
        }else if(correctAns > gameSettings.MIN_SCORE){
            status = GameSettings.GAME_OVER_STATUS.AVERAGE;
        } else if(errors == gameSettings.MAX_ERROR_NUMBER || correctAns <= gameSettings.MIN_SCORE){
            status = GameSettings.GAME_OVER_STATUS.BAD;
        }
        interfaceManager.stopGame(status);
    }

    public void check(View view){
        if(!isTimerActive){
            return;
        }
        int ans = interfaceManager.parseAnswer(view);
        if (ans == answer){
            correctAns+=1;
        }else{
            interfaceManager.errs[errors].setBackgroundColor(Color.RED);
            errors++;
            if (errors >= gameSettings.MAX_ERROR_NUMBER){
                timer.cancel();
                stopGame();
            }
        }
        totalAns += 1;

        interfaceManager.updateScoreView(correctAns, totalAns);
        makeQuestion();
    }

    public void makeQuestion (){
        Random rnd=  new Random();
        int first   = rnd.nextInt(gameSettings.FIRST_NUMBER_MAX) + 1;
        int second  = rnd.nextInt(gameSettings.SECOND_NUMBER_MAX) + 1;

        answer = first + second;

        List <Integer> answersList = new ArrayList<>();
        answersList.add(answer);
        int next;
        while(answersList.size() < gameSettings.MAX_ANSWERS_NUMBER){
            next = rnd.nextInt(answer)+1;
            if (!answersList.contains(next)) {
                answersList.add(next);
            }
        }

        interfaceManager.askQuestion(answersList, first, second);

    }
}
