package com.board.quiz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity implements Constant {

    private SettingPreference pref;
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 3 Seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        pref = new SettingPreference(this);
        RuntimePermission runtimePermission = new RuntimePermission(this);
        Typeface pacifico = Typeface.createFromAsset(getAssets(), "pacifico.ttf");
        AnimationUtil animationUtil = new AnimationUtil(this);
        TextView textView = (TextView) findViewById(R.id.imageView1);
        textView.setTypeface(pacifico);
        animationUtil.slideInLeft(textView);
        TextView textView2 = (TextView) findViewById(R.id.imageView2);
        textView2.setTypeface(pacifico);
        animationUtil.slideInRight(textView2);

        final DataBaseHelper dbHelper = new DataBaseHelper(this);

        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!runtimePermission.checkPermissionForWriteExternalStorage()) {
            runtimePermission.requestPermissionForExternalStorage();
            return;
        }

        Handler handler = new Handler();

        // run a thread after 3 seconds to start the home screen
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                passToNextActivity();
            }
        }, SPLASH_DURATION);
    }

    private void passToNextActivity() {
        if (!mIsBackButtonPressed) {

            pref.editor.putBoolean("sound", true);
            pref.editor.putBoolean("vibrate", true);

            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            finish();
            startActivity(i);
        }
    }

    // Request Call Back Method To check permission is granted by user or not for MarshMallow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    passToNextActivity();
                } else {
                    Toast.makeText(this, "Please enable permission in Settings", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();
    }
}