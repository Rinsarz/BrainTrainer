package com.rinsarz.braintrainer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
        loadRecords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setTitle("Close app?")
                .setMessage("Do you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.settings:
                Log.i("Menu item selected", "Settings");
                Intent intent = new Intent(getApplicationContext(), GameSettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                Log.i("Menu item selected", "Help");
                return true;
            case R.id.records:
                Intent intent1 = new Intent(getApplicationContext(), RecordsActivity.class);
                startActivity(intent1);
            default:
                return false;
        }
    }

    public void init(){
        interfaceManager.initializeInterface();
        isTimerActive = false;
    }

    public void loadRecords(){
        SharedPreferences sharedPreferences =
                this.getSharedPreferences("com.rinsarz.braintrainer", Context.MODE_PRIVATE);
        try {
            GameSettings.RECORDS = (ArrayList<Record> )ObjectSerializer.deserialize(sharedPreferences.getString("records", ObjectSerializer.serialize(new ArrayList<Record>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        timer = new CountDownTimer((GameSettings.MAX_TIMER + 1)*1000, 1000) {
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
        saveRecord();

        isTimerActive = false;
        GameSettings.GAME_OVER_STATUS status = GameSettings.GAME_OVER_STATUS.BAD;

        if(correctAns >= GameSettings.EXCELLENT_SCORE){
            status = GameSettings.GAME_OVER_STATUS.EXCELLENT;
        } else if(correctAns > GameSettings.GOOD_SCORE){
            status = GameSettings.GAME_OVER_STATUS.GOOD;
        }else if(correctAns > GameSettings.MIN_SCORE){
            status = GameSettings.GAME_OVER_STATUS.AVERAGE;
        } else if(errors == GameSettings.MAX_ERROR_NUMBER || correctAns <= GameSettings.MIN_SCORE){
            status = GameSettings.GAME_OVER_STATUS.BAD;
        }
        interfaceManager.stopGame(status);

        Log.i("GAME STATUS", "Game stopped");
    }

    public void saveRecord(){
        Date date = new Date();
        GameSettings.RECORDS.add(new Record(date, correctAns, errors));
        //Save records
        SharedPreferences sharedPreferences =
                this.getSharedPreferences("com.rinsarz.braintrainer", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("records", ObjectSerializer.serialize(GameSettings.RECORDS)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void check(View view){
        Log.i("GAME STATUS", "Check started");
        if(!isTimerActive){
            return;
        }
        int ans = interfaceManager.parseAnswer(view);
        if (ans == answer){
            correctAns+=1;
        }else{
            interfaceManager.errs[errors].setBackgroundColor(Color.RED);
            errors++;
            if (errors >= GameSettings.MAX_ERROR_NUMBER){
                timer.cancel();
                stopGame();
            }
        }
        totalAns += 1;

        interfaceManager.updateScoreView(correctAns, totalAns);

        Log.i("GAME STATUS", "Check completed");
        makeQuestion();
    }

    public void makeQuestion (){
        Log.i("GAME STATUS", "Make question started");
        Random rnd=  new Random();
        int first   = rnd.nextInt(GameSettings.FIRST_NUMBER_MAX) + 1;
        int second  = rnd.nextInt(GameSettings.SECOND_NUMBER_MAX) + 1;

        answer = first + second;
        List <Integer> answersList = new ArrayList<>();
        answersList.add(answer);
        int next;
        int random_offset = 0;
        if (answer < 10){
            random_offset = 5;
        }
        while(answersList.size() < GameSettings.MAX_ANSWERS_NUMBER){
            next = rnd.nextInt(answer + random_offset)+1;

            /*
            while(answersList.contains(next) && next < (GameSettings.FIRST_NUMBER_MAX + GameSettings.SECOND_NUMBER_MAX)){
                next+=1;
            }
            answersList.add(next);
            */

            if (!answersList.contains(next)) {
                answersList.add(next);
            }

        }
        Log.i("GAME STATUS", "Question list formed");


        interfaceManager.askQuestion(answersList, first, second);
        Log.i("GAME STATUS", "Make question finished");

    }
}
