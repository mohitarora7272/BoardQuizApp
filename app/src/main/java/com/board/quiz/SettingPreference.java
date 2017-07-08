package com.board.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

@SuppressWarnings("ALL")
public class SettingPreference {
    private SharedPreferences pref;
    public Editor editor;

    private static final String PREF_NAME = "BOARD_QUIZ";
    public static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_USERNAME = "userName";

    public SettingPreference(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void enterName(String name) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, name);
        editor.apply();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }
}