package com.board.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.board.quiz.R.id.btn_backMode;

public class ModeToActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2);
        init();
    }

    private void init() {
        AnimationUtil animationUtil = new AnimationUtil(this);
        TextView tvStudyMode = (TextView) findViewById(R.id.txtStudyMode);
        animationUtil.slideInDown(tvStudyMode);
        tvStudyMode.setOnClickListener(this);
        TextView tvTestMode = (TextView) findViewById(R.id.txtTestMode);
        animationUtil.slideInUp(tvTestMode);
        tvTestMode.setOnClickListener(this);

        Button home = (Button) findViewById(btn_backMode);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int nResId = v.getId();
        switch (nResId) {
            case R.id.txtStudyMode:
                onClickStudyMode();
                break;
            case R.id.txtTestMode:
                onClickTestMode();
                break;
            case R.id.btn_backMode:
                OnClickBackMode();
                break;
        }
    }

    private void OnClickBackMode() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void onClickStudyMode() {
        SharedPreferences preferences = getSharedPreferences("flag", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String buttonId = "StudyMode";
        editor.putString("buttonId", buttonId);
        editor.apply();
        goCategory();
    }

    private void onClickTestMode() {
        SharedPreferences preferences = getSharedPreferences("flag", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String buttonId = "TestMode";
        editor.putString("buttonId", buttonId);
        editor.apply();
        goCategory();
    }

    private void goCategory() {
        Intent intent = new Intent(this, Category.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}