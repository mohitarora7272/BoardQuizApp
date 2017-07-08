package com.board.quiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Statics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);
        AnimationUtil animationUtil = new AnimationUtil(this);
        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        String timer = DataManager.loadSavedPreferences("timer", Statics.this);
        String fifty = DataManager.loadSavedPreferences("fifty", Statics.this);
        String skip = DataManager.loadSavedPreferences("skip", Statics.this);

        String right = getIntent().getStringExtra("rightans");
        String wrong = getIntent().getStringExtra("wrongans");
        String tot = getIntent().getStringExtra("totattempt");

        TextView title = (TextView) findViewById(R.id.tvstatics);
        TextView titlefifty = (TextView) findViewById(R.id.titlefifty);
        TextView titleright = (TextView) findViewById(R.id.titleright);
        TextView titleskip = (TextView) findViewById(R.id.titleskip);
        TextView titletimer = (TextView) findViewById(R.id.titletimer);
        TextView titletot = (TextView) findViewById(R.id.titletot);
        TextView titlewrong = (TextView) findViewById(R.id.titlewrong);
        TextView tvskip = (TextView) findViewById(R.id.tvskip);
        TextView tvfifty = (TextView) findViewById(R.id.tvfifty);
        TextView tvtimer = (TextView) findViewById(R.id.tvtimer);
        TextView tvright = (TextView) findViewById(R.id.tvright);
        TextView tvwrong = (TextView) findViewById(R.id.tvwrong);
        TextView tvtot = (TextView) findViewById(R.id.tvtotal);
        Button btnhome = (Button) findViewById(R.id.btnhome);
        animationUtil.slideInDown(btnhome);

        title.setTypeface(bold);
        titlewrong.setTypeface(normal);
        titletot.setTypeface(normal);
        titletimer.setTypeface(normal);
        titleskip.setTypeface(normal);
        titlefifty.setTypeface(normal);
        titleright.setTypeface(normal);
        tvskip.setTypeface(bold);
        tvfifty.setTypeface(bold);
        tvtimer.setTypeface(bold);
        tvright.setTypeface(bold);
        tvwrong.setTypeface(bold);
        tvtot.setTypeface(bold);
        btnhome.setTypeface(bold);
        tvskip.setText(skip);
        tvtimer.setText(timer);
        tvfifty.setText(fifty);
        tvright.setText(right);
        tvwrong.setText(wrong);
        tvtot.setText(tot);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statics.this, MainActivity.class);
                finish();
                startActivity(it);
            }
        });
    }
}