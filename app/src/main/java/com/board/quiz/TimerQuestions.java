package com.board.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class TimerQuestions extends AppCompatActivity implements View.OnClickListener {

    private String que;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String answer;
    private String buttonId;
    private String category;

    private int currentQuestion = 0;
    private int m_nFreeValidCount = 0;
    private int rightans = 0;
    private int wrongans = 0;
    private int i = 0;
    private int fiftfifty = 0, skip = 0, timelifeline = 0;
    private int totalQueLen;
    private int mistake = DataManager.mistake;
    private int fiftyVal;
    private int skipVal;
    private int timerVal;

    private boolean m_bResultState = false;
    private boolean freeUsingState = true;
    private boolean nextButtonTapped = false;
    private boolean cbvibrate;
    private boolean cbtimer;

    private TextView tv;
    private TextView taOpt1;
    private TextView taOpt2;
    private TextView taOpt3;
    private TextView taOpt4;
    private TextView tanoofque;
    private TextView lifeline;
    private TextView taQue;

    private Button btnfifty;
    private Button btnpass;
    private Button btntimer;

    private TextView[] btn1;
    private TextView[] btn2;
    private TextView[] btn3;
    private TextView[] btn4;

    private List<QuizPojo> getquestions = null;
    private ArrayList<QuizPojo> questionlist = new ArrayList<>();

    private TextToSpeech mText2Speech;

    private DataBaseHelper db;

    private ToggleButton tgbtn;

    private MediaPlayer mp;

    private MyCounter timer = null;

    private long savedtimer;

    private Vibrator vibe;

    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizscreen);

        db = new DataBaseHelper(getApplicationContext());

        m_nFreeValidCount = Integer.parseInt(db.getFreeLife());

        getSharedPreferenceValues();

        DataManager.result = null;
        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        tv = (TextView) findViewById(R.id.tv);
        tv.setText(" ");

        getCurPaymentMethod();

        if (freeUsingState) {
            if (m_nFreeValidCount == 0) {
                setAlertCurrentState(getString(R.string.freeAppTitle), getString(R.string.freeAppMessage));
            }
        }

        if (freeUsingState) {
            if (buttonId.equals("StudyMode")) {
                mistake = Integer.parseInt(db.getFreeLife());
            } else {
                mistake = 3;
            }

        } else {
            if (buttonId.equals("StudyMode")) {
                mistake = 150;
            } else {
                mistake = 3;
            }
        }

        String strtimer = DataManager.timer;
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        savedtimer = Long.parseLong(strtimer);
        String categoryname = getIntent().getStringExtra("categoryname");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TimerQuestions.this);

        tgbtn = (ToggleButton) this.findViewById(R.id.toggleButton2);
        boolean cbsound = prefs.getBoolean("cbsound", true);
        cbvibrate = prefs.getBoolean("cbvibrate", true);
        cbtimer = prefs.getBoolean("cbtimer", true);
        btntimer = (Button) this.findViewById(R.id.btntimer);
        LinearLayout lltimer = (LinearLayout) this.findViewById(R.id.lltimer);
        tgbtn.setText(null);
        tgbtn.setTextOn(null);
        tgbtn.setTextOff(null);
        if (cbsound) {
            tgbtn.setChecked(true);
        }

        if (cbtimer) {
            if (buttonId.equals("TestMode")) {
                timer = new MyCounter(savedtimer * 1000, 1000);
                timer.start();
            }
        } else {
            tv.setVisibility(View.INVISIBLE);
            btntimer.setVisibility(View.GONE);
            lltimer.setVisibility(View.GONE);
        }

        currentQuestion = 0;
        rightans = 0;
        wrongans = 0;
        taQue = (TextView) this.findViewById(R.id.taque);
        taOpt1 = (TextView) this.findViewById(R.id.taOpt5);
        taOpt2 = (TextView) this.findViewById(R.id.taOpt6);
        taOpt3 = (TextView) this.findViewById(R.id.taOpt7);
        taOpt4 = (TextView) this.findViewById(R.id.taOpt8);
        lifeline = (TextView) this.findViewById(R.id.txtlife);

        btnfifty = (Button) this.findViewById(R.id.btnfifty);
        btnpass = (Button) this.findViewById(R.id.btnskip);

        TextView txtcategoryname = (TextView) this.findViewById(R.id.txtcategoryname);
        tanoofque = (TextView) this.findViewById(R.id.tanoofque1);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        taOpt1.setOnClickListener(this);
        taOpt2.setOnClickListener(this);
        taOpt3.setOnClickListener(this);
        taOpt4.setOnClickListener(this);

        tanoofque.setTypeface(normal);
        taOpt1.setTypeface(bold);
        taOpt2.setTypeface(bold);
        taOpt3.setTypeface(bold);
        taOpt4.setTypeface(bold);
        lifeline.setTypeface(bold);
        btnfifty.setTypeface(bold);
        btntimer.setTypeface(bold);
        btnpass.setTypeface(bold);
        taQue.setTypeface(bold);
        txtcategoryname.setTypeface(normal);
        tv.setTypeface(bold);

        if (freeUsingState) {
            totalQueLen = 10;
        } else {
            totalQueLen = db.getCategoryCount(categoryname);
        }

        getquestions = db.getQuestion(categoryname);

        getQuestionsAnswers(currentQuestion);

        txtcategoryname.setText(categoryname.toUpperCase());
        TextView[] arr1 = {taOpt2, taOpt3, taOpt4};
        TextView[] arr2 = {taOpt1, taOpt3, taOpt4};
        TextView[] arr3 = {taOpt1, taOpt2, taOpt4};
        TextView[] arr4 = {taOpt2, taOpt3, taOpt1};
        btn1 = arr1;
        btn2 = arr2;
        btn3 = arr3;
        btn4 = arr4;

        if (buttonId.equals("StudyMode")) {
            btnfifty.setText("Correct Answer");
            btntimer.setText("Explaination");
            btnpass.setText("Next");

            btnfifty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nextButtonTapped = false;

                    if (answer.equals(taOpt1.getText().toString())) {
                        taOpt1.setBackgroundResource(R.drawable.green);
                        taOpt1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.right_icon, 0);
                    }
                    if (answer.equals(taOpt2.getText().toString())) {
                        taOpt2.setBackgroundResource(R.drawable.green);
                        taOpt2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.right_icon, 0);
                    }
                    if (answer.equals(taOpt3.getText().toString())) {
                        taOpt3.setBackgroundResource(R.drawable.green);
                        taOpt3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.right_icon, 0);
                    }
                    if (answer.equals(taOpt4.getText().toString())) {
                        taOpt4.setBackgroundResource(R.drawable.green);
                        taOpt4.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.right_icon, 0);
                    }
                }
            });

            btntimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timelifeline = Integer.parseInt(DataManager.loadSavedPreferences("timer", TimerQuestions.this));
                    DataManager.savePreferences("timer", timelifeline + 1 + "", TimerQuestions.this);

                    nextButtonTapped = false;

                    if (buttonId.equals("TestMode")) {
                        if (cbtimer) {
                            long newtime = 16000;
                            timer.cancel();
                            timer = new MyCounter(newtime, 1000);
                            timer.start();
                            btntimer.setVisibility(View.INVISIBLE);
                        }
                    }
                    AlertDlg();
                }
            });

        }

        if (buttonId.equals("TestMode")) {
            btnfifty.setVisibility(View.VISIBLE);
            btntimer.setVisibility(View.VISIBLE);
            btnpass.setVisibility(View.VISIBLE);

            btnfifty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fiftyVal = DataManager.getFiftyValue(TimerQuestions.this);
                    if (fiftyVal == 0) {
                        setAlertCurrentState(getString(R.string.fiftyAppTitle), getString(R.string.fiftyAppMessage));
                        return;
                    }

                    fiftyVal--;
                    DataManager.setFiftyValue(fiftyVal, TimerQuestions.this);

                    nextButtonTapped = false;

                    fiftfifty = Integer.parseInt(DataManager.loadSavedPreferences("fifty", TimerQuestions.this));
                    DataManager.savePreferences("fifty", fiftfifty + 1 + "", TimerQuestions.this);
                    System.out.println("answer--" + answer);
                    taOpt1.setVisibility(View.INVISIBLE);
                    taOpt2.setVisibility(View.INVISIBLE);
                    taOpt3.setVisibility(View.INVISIBLE);
                    taOpt4.setVisibility(View.INVISIBLE);
                    if (answer.equalsIgnoreCase(taOpt1.getText().toString())) {
                        taOpt1.setVisibility(View.VISIBLE);
                        TextView id = btn1[rand.nextInt(btn1.length)];
                        id.setVisibility(View.VISIBLE);
                    } else if (answer.equalsIgnoreCase(taOpt2.getText().toString())) {
                        taOpt2.setVisibility(View.VISIBLE);
                        TextView id = btn2[rand.nextInt(btn2.length)];
                        id.setVisibility(View.VISIBLE);
                    } else if (answer.equalsIgnoreCase(taOpt3.getText().toString())) {
                        taOpt3.setVisibility(View.VISIBLE);
                        TextView id = btn3[rand.nextInt(btn4.length)];
                        id.setVisibility(View.VISIBLE);
                    } else if (answer.equalsIgnoreCase(taOpt4.getText().toString())) {
                        taOpt4.setVisibility(View.VISIBLE);
                        TextView id = btn4[rand.nextInt(btn4.length)];
                        id.setVisibility(View.VISIBLE);
                    }

                    btnfifty.setVisibility(View.VISIBLE);
                    updateTextUI();
                }
            });

            btntimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    timerVal = DataManager.getTimerValue(TimerQuestions.this);
                    if (timerVal == 0) {
                        setAlertCurrentState(getString(R.string.timerAppTitle), getString(R.string.timerAppMessage));
                        return;
                    }

                    timerVal--;
                    DataManager.setTimerValue(timerVal, TimerQuestions.this);

                    timelifeline = Integer.parseInt(DataManager.loadSavedPreferences("timer", TimerQuestions.this));
                    DataManager.savePreferences("timer", timelifeline + 1 + "", TimerQuestions.this);

                    nextButtonTapped = false;

                    if (cbtimer) {
                        long newtime = 16000;
                        timer.cancel();
                        timer = new MyCounter(newtime, 1000);
                        timer.start();
                        btntimer.setVisibility(View.VISIBLE);
                    }

                    updateTextUI();
                }
            });
        }

        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipVal = DataManager.getSkipValue(TimerQuestions.this);
                if (skipVal == 0) {
                    setAlertCurrentState(getString(R.string.skipAppTitle), getString(R.string.skipAppMessage));
                    return;
                }

                skipVal--;
                DataManager.setSkipValue(skipVal, TimerQuestions.this);

                skip = Integer.parseInt(DataManager.loadSavedPreferences("skip", TimerQuestions.this));
                DataManager.savePreferences("skip", skip + 1 + "", TimerQuestions.this);
                QuizPojo quiz = new QuizPojo();
                quiz.set_question(que);
                quiz.set_answer(answer);
                quiz.setSelanswer("");

                nextButtonTapped = true;

                if (buttonId.equals("TestMode")) {
                    questionlist.add(quiz);
                }

                if (buttonId.equals("StudyMode")) {
                    TextView tmp = taOpt1;
                    if (answer.equalsIgnoreCase(taOpt1.getText().toString())) {
                        tmp = taOpt2;
                    } else if (answer.equalsIgnoreCase(taOpt2.getText().toString())) {
                        tmp = taOpt1;
                    } else if (answer.equalsIgnoreCase(taOpt3.getText().toString())) {
                        tmp = taOpt2;
                    } else if (answer.equalsIgnoreCase(taOpt4.getText().toString())) {
                        tmp = taOpt2;
                    }
                    Log.d("TEXT", "TEXT>>>" + tmp.getText().toString());
                    //String sel = tmp.getText().toString();
                    //selectanswer(0, tmp, sel);
                }

                //mistake--;
                //m_nFreeValidCount--;
                lifeline.setText("Life : " + mistake);

                nextQuestion(0);

                if (buttonId.equals("TestMode")) {
                    btnpass.setVisibility(View.VISIBLE);

                } else {
                    btnpass.setVisibility(View.VISIBLE);
                }
                updateTextUI();
            }
        });

        lifeline.setText("Life : " + mistake);

        updateTextUI();
    }

    private void updateTextUI() {
        if (buttonId.equals("TestMode")) {
            fiftyVal = DataManager.getFiftyValue(this);
            btnfifty.setText(getString(R.string._50_50) + " : " + String.valueOf(fiftyVal));
            skipVal = DataManager.getSkipValue(this);
            btnpass.setText(getString(R.string.skip) + " : " + String.valueOf(skipVal));
            timerVal = DataManager.getTimerValue(this);
            btntimer.setText(getString(R.string.timer2) + " : " + String.valueOf(timerVal));
        }
    }

    public void setAlertCurrentState(String title, String message) {
        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }
        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setCancelable(false)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_block_white_24dp)
                .setTopTitle(title)
                .setMessage(message)
                .setMessageGravity(Gravity.LEFT)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.buyNow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(TimerQuestions.this, ShopArea.class);
                        DataManager.result = questionlist;

                        if (buttonId.equals("StudyMode")) {
                            wrongans = 100;
                        }

                        it.putExtra("rightans", rightans);
                        it.putExtra("wrongans", wrongans);
                        it.putExtra("totattempt", i);
                        it.putExtra("totalques", totalQueLen);
                        it.putExtra("category", category);

                        //it.putExtra("buttonTemp", 1);

                        finish();
                        startActivity(it);
                    }
                })
                .setNegativeButton(R.string.buyLater, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (buttonId.equals("TestMode")) {
                            if (cbtimer) {
                                timer.start();
                            }
                        }
                    }
                })
                .show();

    }

    private void getSharedPreferenceValues() {
        SharedPreferences preferences = getSharedPreferences("flag", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        buttonId = preferences.getString("buttonId", null);
        editor.clear();
        editor.apply();
    }

    public void getCurPaymentMethod() {

        ContentValues tmpContentValues = db.getCurPaymentMethod();

        String paymentkind = tmpContentValues.getAsString("paymentkind");
        String reg_date = tmpContentValues.getAsString("reg_date");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());


        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reg_date.substring(6, 8)));
        thatDay.set(Calendar.MONTH, Integer.parseInt(reg_date.substring(4, 6)));
        thatDay.set(Calendar.YEAR, Integer.parseInt(reg_date.substring(0, 4)));

        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_MONTH, Integer.parseInt(currentDate.substring(6, 8)));
        today.set(Calendar.MONTH, Integer.parseInt(currentDate.substring(4, 6)));
        today.set(Calendar.YEAR, Integer.parseInt(currentDate.substring(0, 4)));

        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        switch (paymentkind) {
            case "oneMonth":
                if (days > 31) {
                    freeUsingState = true;

                    db.setCurPaymentMethod("free", currentDate);
                } else {
                    freeUsingState = false;
                }
                break;
            case "threeMonth":
                if (days > 91) {
                    freeUsingState = true;

                    db.setCurPaymentMethod("free", currentDate);
                } else {
                    freeUsingState = false;
                }
                break;
            case "oneYear":
                if (days > 365) {
                    freeUsingState = true;

                    db.setCurPaymentMethod("free", currentDate);
                } else {
                    freeUsingState = false;
                }
                break;
            case "free":
                //freeUsingState = true;
                freeUsingState = false;

                db.setCurPaymentMethod("free", currentDate);
                break;
        }
    }

    @Override
    public void onClick(View v1) {

        nextButtonTapped = false;

        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }

        TextView tmp = (TextView) v1;
        String sel = tmp.getText().toString();

        selectAnswer(tmp, sel);
    }

    public void selectAnswer(final TextView tmp,
                             final String sel) {

        if (sel.equals(answer)) {
            tmp.setBackgroundResource(R.drawable.green);
            tmp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_icon, 0);
            vibrate();
            if (tgbtn.isChecked())
                rightans++;
            {
                startSound("correct.wav");
            }
            if (buttonId.equals("TestMode")) {
                m_nFreeValidCount--;
                nextQuestion(500);
            }

        } else {

            if (!nextButtonTapped) {
                tmp.setBackgroundResource(R.drawable.red);
                tmp.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.wrong_icon, 0);

                if (answer.equals(taOpt1.getText().toString())) {
                    taOpt1.setBackgroundResource(R.drawable.green);
                    taOpt1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.right_icon, 0);

                }
                if (answer.equals(taOpt2.getText().toString())) {
                    taOpt2.setBackgroundResource(R.drawable.green);
                    taOpt2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.right_icon, 0);

                }
                if (answer.equals(taOpt3.getText().toString())) {
                    taOpt3.setBackgroundResource(R.drawable.green);
                    taOpt3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.right_icon, 0);

                }
                if (answer.equals(taOpt4.getText().toString())) {
                    taOpt4.setBackgroundResource(R.drawable.green);
                    taOpt4.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.right_icon, 0);

                }
                wrongans++;
                if (tgbtn.isChecked()) {

                    startSound("wrong.wav");
                }

                vibrate();

            }

            if (buttonId.equals("TestMode")) {
                mistake--;
                m_nFreeValidCount--;
                lifeline.setText("Life : " + mistake);
            }

            if (buttonId.equals("TestMode")) {
                nextQuestion(500);
            }

        }
        QuizPojo quiz = new QuizPojo();
        quiz.set_question(que);
        quiz.set_answer(answer);
        quiz.setSelanswer(sel);
        questionlist.add(quiz);

        taOpt1.setClickable(false);
        taOpt2.setClickable(false);
        taOpt3.setClickable(false);
        taOpt4.setClickable(false);
    }

    public void nextQuestion(int SPLASHTIME) {
        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                db.close();
                currentQuestion++;

                if (freeUsingState) {
                    db.setCurFreeLife(String.valueOf(m_nFreeValidCount));

                    if (m_nFreeValidCount == 0) {
                        setAlertCurrentState(getString(R.string.freeAppTitle), getString(R.string.freeAppMessage));
                    }
                }

                if (mistake > 0) {
                    if (currentQuestion < totalQueLen) {
                        if (cbtimer) {
                            if (buttonId.equals("TestMode")) {
                                timer = new MyCounter(savedtimer * 1000, 1000);
                                timer.start();
                            }
                        }
                        getQuestionsAnswers(currentQuestion);
                        if (tgbtn.isChecked()) {
                            //audio(next);
                        }

                    } else {
                        if (buttonId.equals("TestMode")) {
                            if (cbtimer) {
                                timer.cancel();
                            }
                        }
                        Intent it = new Intent(TimerQuestions.this, ResultActivity.class);
                        DataManager.result = questionlist;
                        it.putExtra("rightans", rightans);
                        it.putExtra("wrongans", wrongans);
                        it.putExtra("totattempt", i);
                        it.putExtra("totalques", totalQueLen);
                        it.putExtra("category", category);
                        //it.putExtra("buttonTemp", 1);
                        finish();
                        startActivity(it);
                    }
                } else {
                    if (buttonId.equals("TestMode")) {
                        if (cbtimer) {
                            timer.cancel();
                        }
                    }
                    if (!m_bResultState) {
                        m_bResultState = true;

                        if (questionlist.size() > 0) {
                            Intent it = new Intent(TimerQuestions.this, ResultActivity.class);
                            DataManager.result = questionlist;

                            if (buttonId.equals("StudyMode")) {
                                wrongans = 100;
                            }

                            it.putExtra("rightans", rightans);
                            it.putExtra("wrongans", wrongans);
                            it.putExtra("totattempt", i);
                            it.putExtra("totalques", totalQueLen);
                            it.putExtra("category", category);

                            //it.putExtra("buttonTemp", 1);

                            finish();
                            startActivity(it);
                        } else {
                            Intent iScore = new Intent(TimerQuestions.this, Score.class);
                            iScore.putExtra("rightans", rightans);
                            iScore.putExtra("totalques", totalQueLen);
                            iScore.putExtra("wrongans", wrongans);
                            iScore.putExtra("totattempt", i);
                            finish();
                            startActivity(iScore);
                        }
                    }
                }
            }

        }, SPLASHTIME);
    }

    public void startSound(String filename) {

        MediaPlayer player = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd(filename);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void vibrate() {
        if (cbvibrate) {
            vibe.vibrate(700);
        }
    }

    public void getQuestionsAnswers(int currentQuestion) {
        QuizPojo cn = getquestions.get(currentQuestion);
        que = cn.get_question();

        try {
            opt1 = new String(cn.get_option1().getBytes("UTF-8"));
            opt2 = new String(cn.get_option2().getBytes("UTF-8"));
            opt3 = new String(cn.get_option3().getBytes("UTF-8"));
            opt4 = new String(cn.get_option4().getBytes("UTF-8"));
            answer = new String(cn.get_answer().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setCurrentQuestion();
    }

    public void setCurrentQuestion() {
        try {
            i++;
            String noofque = "Question No. " + i + " out of " + totalQueLen;
            tanoofque.setText(noofque);
            taOpt1.setVisibility(View.VISIBLE);
            taOpt2.setVisibility(View.VISIBLE);
            taOpt3.setVisibility(View.VISIBLE);
            taOpt4.setVisibility(View.VISIBLE);

            taOpt1.setClickable(true);
            taOpt2.setClickable(true);
            taOpt3.setClickable(true);
            taOpt4.setClickable(true);

            ArrayList<String> optionlist = new ArrayList<>();

            optionlist.add(opt1);
            optionlist.add(opt2);
            optionlist.add(opt3);
            optionlist.add(opt4);

            Collections.shuffle(optionlist);
            taOpt1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            taOpt2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            taOpt3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            taOpt4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            taOpt1.setTextColor(Color.BLACK);
            taOpt2.setTextColor(Color.BLACK);
            taOpt3.setTextColor(Color.BLACK);
            taOpt4.setTextColor(Color.BLACK);
            taOpt1.setBackgroundResource(R.drawable.normal);
            taOpt2.setBackgroundResource(R.drawable.normal);
            taOpt3.setBackgroundResource(R.drawable.normal);
            taOpt4.setBackgroundResource(R.drawable.normal);
            taQue.setText(que);
            taOpt1.setText(optionlist.get(0));
            taOpt2.setText(optionlist.get(1));
            taOpt3.setText(optionlist.get(2));
            taOpt4.setText(optionlist.get(3));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mistake--;
            m_nFreeValidCount--;
            lifeline.setText("Life : " + mistake);
            nextQuestion(500);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Integer milisec = Double.valueOf(millisUntilFinished).intValue();
            Integer cd_secs = milisec / 1000;
            Integer seconds = (cd_secs % 3600) % 60;
            tv.setText("Timer  : " + String.format("%02d", seconds));
        }
    }

    public void AlertDlg() {
        String strQuestion = taQue.getText().toString();
        String strExplanation = db.getCurCategory(strQuestion);

        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_rate_review_white_24dp)
                .setTitle(R.string.explanation)
                .setTitleGravity(Gravity.LEFT)
                .setMessage(strExplanation)
                .setMessageGravity(Gravity.LEFT)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }

        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_exit_to_app_white_24dp)
                .setMessage(R.string.exit_quiz_message)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totalQueLen;

                        if (freeUsingState) {
                            totalQueLen = 10;
                        } else {
                            totalQueLen = db.getCategoryCount();
                        }
                        if (questionlist.size() > 0) {
                            Intent it = new Intent(TimerQuestions.this, ResultActivity.class);
                            DataManager.result = questionlist;
                            it.putExtra("rightans", rightans);
                            it.putExtra("wrongans", wrongans);
                            it.putExtra("totattempt", i);
                            it.putExtra("totalques", totalQueLen);
                            it.putExtra("category", category);
                            //it.putExtra("buttonTemp", 1);
                            finish();
                            startActivity(it);
                        } else {
                            Intent iScore = new Intent(TimerQuestions.this, Score.class);
                            iScore.putExtra("rightans", rightans);
                            iScore.putExtra("totalques", totalQueLen);
                            iScore.putExtra("wrongans", wrongans);
                            iScore.putExtra("totattempt", i);
                            finish();
                            startActivity(iScore);
                        }
                    }
                })
                .setNegativeButton(R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (buttonId.equals("TestMode")) {
                            if (cbtimer) {
                                timer.start();
                            }
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (buttonId.equals("TestMode")) {
            if (cbtimer) {
                timer.cancel();
            }
        }
    }
}