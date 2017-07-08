package com.board.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

@SuppressWarnings("ALL")
public class ShopArea extends AppCompatActivity {

    // product code
    private static final String PRODUCT_FIFTY = "fiftyfifty";
    private static final String PRODUCT_SKIP = "skip";
    private static final String PRODUCT_TIMER = "timer";

    private String productselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopp_area);

        AnimationUtil animationUtil = new AnimationUtil(this);

        SettingPreference pref = new SettingPreference(this);

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());

        // Initialise buy buttons
        Button btnfiftyfifty = (Button) findViewById(R.id.btnfiftyfifty);
        animationUtil.slideInDown(btnfiftyfifty);

        Button btnSkip = (Button) findViewById(R.id.btnskip);
        animationUtil.slideInLeft(btnSkip);

        Button btnTimer = (Button) findViewById(R.id.btntimer);
        animationUtil.slideInUp(btnTimer);

        btnfiftyfifty.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                productselected = PRODUCT_FIFTY;
                comingSoonDialog();
            }
        });

        btnSkip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                productselected = PRODUCT_SKIP;
                comingSoonDialog();
            }
        });

        btnTimer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                productselected = PRODUCT_TIMER;
                comingSoonDialog();
            }
        });

        Button home = (Button) findViewById(R.id.btnback);

        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void comingSoonDialog() {
        new LovelyStandardDialog(this)
                .setCancelable(true)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_shopping_cart_white_24dp)
                .setTopTitle(R.string.comingsoon)
                .show();
    }
}