package com.board.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ResultActivity extends AppCompatActivity {

    private int rightans;
    private int wrongans;
    private int totalquestions;
    private int totattempt;
    private AdMobsUtils adMobsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        adMobsUtils = new AdMobsUtils(this);
        AnimationUtil animationUtil = new AnimationUtil(this);

        //getSharedPreferenceValues();
        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        TextView title = (TextView) findViewById(R.id.txtresult);
        ListView lvresult = (ListView) findViewById(R.id.lvresult);
        Button btncontinue = (Button) findViewById(R.id.btncontine);
        animationUtil.slideInDown(btncontinue);
        rightans = getIntent().getIntExtra("rightans", 0);
        wrongans = getIntent().getIntExtra("wrongans", 0);
        totattempt = getIntent().getIntExtra("totattempt", 0);
        totalquestions = getIntent().getIntExtra("totalques", 0);
        title.setTypeface(bold);
        btncontinue.setTypeface(bold);
        ArrayList<QuizPojo> arl = DataManager.result;
        if (arl.size() > 0) {
            Adapter adapter = new Adapter(ResultActivity.this, arl);

            if (wrongans == 100) {
                adapter.buttonId = "StudyMode";
            } else {
                adapter.buttonId = "TestMode";
            }

            lvresult.setAdapter(adapter);
        }
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iScore = new Intent(ResultActivity.this, Score.class);
                iScore.putExtra("rightans", rightans);
                iScore.putExtra("totalques", totalquestions);
                iScore.putExtra("wrongans", wrongans);
                iScore.putExtra("totattempt", totattempt);
                finish();
                startActivity(iScore);
            }
        });
        adMobsUtils.showBannerAd(mAdView);
    }

    private void getSharedPreferenceValues() {
        SharedPreferences preferences = getSharedPreferences("flag", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String buttonID = preferences.getString("buttonId", null);
        editor.clear();
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataManager.result = null;
    }
}