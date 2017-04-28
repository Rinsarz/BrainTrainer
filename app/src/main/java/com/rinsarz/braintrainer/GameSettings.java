package com.rinsarz.braintrainer;

import java.util.ArrayList;

/**
 * Created by gravityPC on 4/27/2017.
 */

public class GameSettings {
    public static int MIN_SCORE =    15;
    public static int GOOD_SCORE =   25;
    public static int EXCELLENT_SCORE = 30;
    public static final int MAX_ERROR_NUMBER = 3;
    public static int FIRST_NUMBER_MAX = 20;
    public static int SECOND_NUMBER_MAX = 20;
    public static final int MAX_ANSWERS_NUMBER = 4;
    public static final int MAX_TIMER = 30;
    public static ArrayList<Record> RECORDS;

    public enum GAME_OVER_STATUS {BAD, AVERAGE, GOOD, EXCELLENT}


}
