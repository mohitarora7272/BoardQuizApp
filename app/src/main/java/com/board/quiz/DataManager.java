package com.board.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class DataManager {

    public static String timer = "16";
    public static int mistake = 3;
    public static int noofquestions = 10;
    public static int ratecounter = 5;
    public static int adRateCounter = 4;
    public static String appurl = "https://play.google.com/store/apps/details?id=com.board.quiz";  // change your package name
    public static String email = "iamdeveloper7272@gmail.com"; // change your email here ...
    public static String admobid = "ca-app-pub-6192865524332826/5593641192";
    public static String share = "You can download Board Quiz app from : " + appurl;
    public static ArrayList<QuizPojo> result = null;

    private static SharedPreferences mSharedPreferences;

    public static String loadSavedPreferences(String etValue, Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        return mSharedPreferences.getString(etValue, "0");
    }

    //Save String Value
    public static void savePreferences(String key, String value, Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void setFiftyValue(int value, Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt("fiftyVal", value);
        edit.apply();
    }

    public static int getFiftyValue(Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        return mSharedPreferences.getInt("fiftyVal", 5);
    }

    public static void setSkipValue(int value, Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt("skipVal", value);
        edit.apply();
    }

    public static int getSkipValue(Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        return mSharedPreferences.getInt("skipVal", 5);
    }

    public static void setTimerValue(int value, Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt("timerVal", value);
        edit.apply();
    }

    public static int getTimerValue(Context ctx) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        return mSharedPreferences.getInt("timerVal", 5);
    }
}