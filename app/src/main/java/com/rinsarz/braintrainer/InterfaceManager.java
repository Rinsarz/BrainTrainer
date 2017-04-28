package com.rinsarz.braintrainer;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gravityPC on 4/27/2017.
 */

public class InterfaceManager {
    InterfaceManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        colors.add(Color.parseColor("#ff669900"));
        colors.add(Color.parseColor("#ff0099cc"));
        colors.add(Color.parseColor("#ffff8800"));
        colors.add(Color.parseColor("#ffaa66cc"));
    }

    Button startBtn;
    Button restartBtn;
    TextView timerView;
    TextView taskview;
    TextView [] answers;
    TextView scoreView;
    TextView statusView;
    MainActivity mainActivity;

    TextView [] errs;
    ImageView[] helpViews;

    ArrayList<Integer> colors = new ArrayList();




    void initializeInterface(){

        startBtn  = (Button)mainActivity.findViewById(R.id.startBtn);
        startBtn.setVisibility(View.VISIBLE);

        restartBtn  = (Button)mainActivity.findViewById(R.id.restartBtn);
        restartBtn.setVisibility(View.INVISIBLE);

        timerView = (TextView)mainActivity.findViewById(R.id.timerView);
        timerView.setText("30");

        taskview = (TextView)mainActivity.findViewById(R.id.taskView);
        taskview.setText("");

        scoreView = (TextView)mainActivity.findViewById(R.id.scoreView);
        scoreView.setText("0/30");

        statusView = (TextView)mainActivity.findViewById(R.id.statusView);
        statusView.setText("");

        helpViews = new ImageView[2];
        helpViews[0] = (ImageView)mainActivity.findViewById(R.id.helpView1);
        helpViews[1] = (ImageView)mainActivity.findViewById(R.id.helpView2);

        for (int i = 0; i < 2; i++){
            helpViews[i].setVisibility(View.VISIBLE);
            helpViews[i].setAlpha(0.5f);
        }

        answers = new TextView[GameSettings.MAX_ANSWERS_NUMBER];
        answers[0] = (TextView)mainActivity.findViewById(R.id.ans1);
        answers[1] = (TextView)mainActivity.findViewById(R.id.ans2);
        answers[2] = (TextView)mainActivity.findViewById(R.id.ans3);
        answers[3] = (TextView)mainActivity.findViewById(R.id.ans4);

        errs = new TextView[GameSettings.MAX_ERROR_NUMBER];
        errs[0] = (TextView)mainActivity.findViewById(R.id.err1);
        errs[1] = (TextView)mainActivity.findViewById(R.id.err2);
        errs[2] = (TextView)mainActivity.findViewById(R.id.err3);

        for (int i = 0; i < 3; i++){
            errs[i].setBackgroundColor(Color.parseColor("#ff669900"));
        }
    }

    void stopGame(GameSettings.GAME_OVER_STATUS status){
        startBtn.setVisibility(View.VISIBLE);
        restartBtn.setVisibility(View.INVISIBLE);
        timerView.setText("00");
        taskview.setText(" ");

        switch (status){
            case EXCELLENT:
                statusView.setText("Excellent!");
                statusView.setTextColor(Color.parseColor("#ff669900"));
                break;
            case GOOD:
                Toast.makeText(mainActivity, "You are trained!", Toast.LENGTH_SHORT).show();
                statusView.setText("Good!");
                statusView.setTextColor(Color.parseColor("#ff669900"));
                break;
            case AVERAGE:
                statusView.setText("Average!");
                statusView.setTextColor(Color.parseColor("#ff669900"));
                break;
            case BAD:
                Toast.makeText(mainActivity, "You have failed!", Toast.LENGTH_SHORT).show();
                statusView.setText("Bad!");
                statusView.setTextColor(Color.RED);
                break;
        }
    }

    void askQuestion(List<Integer> answersList, int firstNumber, int secondNumber){

        Collections.shuffle(answersList);
        Collections.shuffle(colors);


        for (int i = 0; i < GameSettings.MAX_ANSWERS_NUMBER; i++){
            answers[i].setText(answersList.get(i).toString());
            answers[i].setBackgroundColor(colors.get(i));
        }
        taskview.setText(firstNumber + " + " + secondNumber);
    }

    int parseAnswer(View view){
        return Integer.parseInt(((TextView)view).getText().toString());
    }

    void updateScoreView(int correctAns, int totalAns){
        scoreView.setText(correctAns + "/" + totalAns);
    }
}
