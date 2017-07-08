package com.board.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor prefsEditor;

    private final String TAG_NAME = "RateCounter";

    private Typeface bold;

    private int rateCounter;

    final private static int DIALOG_LOGIN = 1;

    private AdMobsUtils adMobsUtils;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adMobsUtils = new AdMobsUtils(this);
        AnimationUtil animationUtil = new AnimationUtil(this);

        @SuppressWarnings("unused")
        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        SharedPreferences myPrefs = this.getSharedPreferences("RateCounterPrefs", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        rateCounter = myPrefs.getInt(TAG_NAME, 0);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        TextView txtHeader = (TextView) findViewById(R.id.txtheader);

        TextView txtPlay = (TextView) findViewById(R.id.txtplay1);
        animationUtil.slideInDown(txtPlay);

        TextView txtShopping = (TextView) findViewById(R.id.btnshopping);
        animationUtil.slideInUp(txtShopping);

        TextView txtFeedback = (TextView) findViewById(R.id.txtfeedback1);
        animationUtil.slideInLeft(txtFeedback);

        TextView txtHighScore = (TextView) findViewById(R.id.txthighscore);
        animationUtil.slideInRight(txtHighScore);

        Button btnSetting = (Button) findViewById(R.id.btnsetting);

        txtPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rateCounter++;
                updateRateCounter();
            }
        });

        txtHighScore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighestScore.class);
                startActivity(i);
            }
        });

        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(i);
            }
        });

        txtFeedback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DIALOG_LOGIN);
            }
        });

        txtShopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ShopArea.class);
                startActivity(i);
            }
        });

        //adMobsUtils.showBannerAd(mAdView);
    }

    @Override
    public void onBackPressed() {
        showDialogForExit();
    }

    // Show Dialog For Exit
    private void showDialogForExit() {
        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_exit_to_app_white_24dp)
                .setMessage(R.string.exit_message)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    public void updateRateCounter() {
        prefsEditor.putInt(TAG_NAME, rateCounter);
        prefsEditor.apply();

        if (rateCounter == DataManager.ratecounter) {
            rateAlert();

        } else {

            if (rateCounter == DataManager.adRateCounter) {
                //adMobsUtils.showInterstitial();
            }

            Intent i = new Intent(this, Category.class);
            startActivity(i);
        }
    }

    public void rateAlert() {
        new LovelyStandardDialog(this)
                .setCancelable(false)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_rate_review_white_24dp)
                .setMessage(R.string.rate_app)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setNeutralButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.rate_now, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = DataManager.appurl;
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        prefsEditor.putInt(TAG_NAME, 200);
                        prefsEditor.apply();
                        Intent i = new Intent(MainActivity.this, Category.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.never, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefsEditor.putInt(TAG_NAME, 200);
                        prefsEditor.apply();
                        Intent i = new Intent(MainActivity.this, Category.class);
                        startActivity(i);
                    }
                })
                .setNeutralButton(R.string.later, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefsEditor.putInt(TAG_NAME, 0);
                        prefsEditor.apply();
                        Intent i = new Intent(MainActivity.this, Category.class);
                        startActivity(i);
                    }
                })
                .show();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog dialogDetails = null;

        switch (id) {
            case DIALOG_LOGIN:
                LayoutInflater inflater = LayoutInflater.from(this);

                View dialogView = inflater.inflate(R.layout.custom_dialog_rateapp, null);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setView(dialogView);

                dialogDetails = dialogBuilder.create();
                dialogDetails.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                break;
        }

        return dialogDetails;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
            case DIALOG_LOGIN:
                final AlertDialog myDialog = (AlertDialog) dialog;
                Button rateBtn = (Button) myDialog.findViewById(R.id.btnOk);
                Button suggestionBtn = (Button) myDialog.findViewById(R.id.btncancel);
                rateBtn.setTypeface(bold);
                suggestionBtn.setTypeface(bold);

                TextView text = (TextView) myDialog.findViewById(R.id.txtheader);
                text.setTypeface(bold);

                rateBtn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {

                        String str = DataManager.appurl;
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(str)));
                        myDialog.cancel();
                    }
                });

                suggestionBtn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("Text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{DataManager.email});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.feedBackMsg));
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        myDialog.cancel();
                    }
                });
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adMobsUtils.getAdView() != null) {
            adMobsUtils.getAdView().resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adMobsUtils.getAdView() != null) {
            adMobsUtils.getAdView().pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adMobsUtils.getAdView() != null) {
            adMobsUtils.getAdView().destroy();
        }
    }
}