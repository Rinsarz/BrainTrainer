package com.rinsarz.braintrainer;

/**
 * Created by gravityPC on 4/27/2017.
 */

public class GameSettings {
    int MIN_SCORE =    15;
    int GOOD_SCORE =   25;
    int EXCELLENT_SCORE = 30;
    final int MAX_ERROR_NUMBER = 3;
    int FIRST_NUMBER_MAX = 20;
    int SECOND_NUMBER_MAX = 20;
    final int MAX_ANSWERS_NUMBER = 4;
    final int MAX_TIMER = 30;

    enum GAME_OVER_STATUS {BAD, AVERAGE, GOOD, EXCELLENT}


}
