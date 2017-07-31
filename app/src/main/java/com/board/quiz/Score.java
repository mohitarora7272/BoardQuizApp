package com.board.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.HashMap;

@SuppressWarnings("ALL")
public class Score extends AppCompatActivity {

    private String rightans = null;
    private String wrongans = null;
    private String totattempt = null;
    private String totalquestions = null;
    private String score;
    private String name;
    private SettingPreference setuser;
    private DbHighestScore db;
    private Typeface bold;
    final private static int DIALOG_LOGIN = 1;
    private AdMobsUtils adMobsUtils;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        adMobsUtils = new AdMobsUtils(this);
        AnimationUtil animationUtil = new AnimationUtil(this);

        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        TextView txtright = (TextView) findViewById(R.id.txtright);
        animationUtil.slideInDown(txtright);
        setuser = new SettingPreference(this);

        SettingPreference pref = new SettingPreference(this);
        db = new DbHighestScore(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        TextView txtheader = (TextView) findViewById(R.id.textView);
        TextView txtscoreheader = (TextView) findViewById(R.id.txtscoreheader);

        txtheader.setTypeface(bold);
        txtscoreheader.setTypeface(bold);
        txtright.setTypeface(normal);

        rightans = getIntent().getSerializableExtra("rightans").toString();
        wrongans = getIntent().getSerializableExtra("wrongans").toString();
        totattempt = getIntent().getSerializableExtra("totattempt").toString();
        totalquestions = getIntent().getSerializableExtra("totalques").toString();

        int numberques = Integer.parseInt(totalquestions);

        int result = (Integer.parseInt(rightans));

        HashMap<String, String> user = pref.getUserDetails();
        name = user.get(SettingPreference.KEY_USERNAME);

        score = String.valueOf(result);

        Button btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        animationUtil.slideInLeft(btnMainMenu);
        Button btnHighscore = (Button) findViewById(R.id.btnHighestscore);
        animationUtil.slideInRight(btnHighscore);
        Button btnShare = (Button) findViewById(R.id.btnShare);
        animationUtil.slideInLeft(btnShare);
        Button btnstatics = (Button) findViewById(R.id.btnstatics);
        animationUtil.slideInRight(btnstatics);

        txtright.setText("" + result);

        btnMainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMainMenu = new Intent(Score.this, MainActivity.class);
                finish();
                startActivity(iMainMenu);
            }
        });

        btnHighscore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog(DIALOG_LOGIN);
            }
        });

        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, DataManager.share);
                // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, share);
                startActivity(Intent.createChooser(sharingIntent, "Share using..."));
            }
        });

        btnstatics.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Score.this, Statics.class);
                it.putExtra("rightans", rightans);
                it.putExtra("wrongans", wrongans);
                it.putExtra("totattempt", totattempt);
                it.putExtra("totalques", totalquestions);
                finish();
                startActivity(it);
            }
        });
        adMobsUtils.showBannerAd(mAdView);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Score.this, MainActivity.class);
        finish();
        startActivity(i);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog dialogDetails = null;

        switch (id) {
            case DIALOG_LOGIN:
                LayoutInflater inflater = LayoutInflater.from(this);

                View dialogView = inflater.inflate(R.layout.custom_dialog_entername, null);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setView(dialogView);
                dialogDetails = dialogBuilder.create();
                dialogDetails.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
                break;
        }

        return dialogDetails;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        final HashMap<String, String> user = setuser.getUserDetails();
        switch (id) {
            case DIALOG_LOGIN:
                final AlertDialog myDialog = (AlertDialog) dialog;

                Button login = (Button) myDialog.findViewById(R.id.btnOk);
                Button createAccount = (Button) myDialog.findViewById(R.id.btncancel);
                final EditText input = (EditText) myDialog.findViewById(R.id.etname);
                input.setTypeface(bold);
                input.setText(user.get(SettingPreference.KEY_USERNAME));
                input.requestFocus();
                TextView text = (TextView) myDialog.findViewById(R.id.txtname);

                text.setTypeface(bold);

                login.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {

                        name = user.get(SettingPreference.KEY_USERNAME);
                        String name = input.getText().toString();
                        setuser.enterName(name);
                        db.addContact(new ScoreData(name, score));
                        myDialog.cancel();
                        passNextActivity();
                    }
                });

                createAccount.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        myDialog.cancel();
                        passNextActivity();
                    }
                });
                break;
        }
    }

    private void passNextActivity() {
        Intent i = new Intent(Score.this, HighestScore.class);
        finish();
        startActivity(i);
    }
}