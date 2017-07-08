package com.board.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Category extends AppCompatActivity {

    private ListView listView;

    private ArrayList<String> offlinecategorylist = new ArrayList<>();

    private DataBaseHelper db;

    private Typeface bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = new DataBaseHelper(getApplicationContext());

        Typeface normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
        bold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        TextView txtheader = (TextView) findViewById(R.id.txtheader);
        txtheader.setTypeface(normal);

        Button home = (Button) findViewById(R.id.btnback);

        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Category.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });

        listView = (ListView) findViewById(R.id.lvclassic_cat);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selected = arg0.getAdapter().getItem(position).toString();
                Intent i = new Intent(Category.this, TimerQuestions.class);
                i.putExtra("categoryname", selected);
                finish();
                startActivity(i);
            }
        });

        setModeInPref();
        displayData();
    }

    private void setModeInPref() {
        SharedPreferences preferences = getSharedPreferences("flag", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String buttonId = "TestMode";
        editor.putString("buttonId", buttonId);
        editor.apply();
    }

    public void displayData() {
        List<QuizPojo> getCategory = db.getCategory();
        for (QuizPojo cn : getCategory) {
            String category = cn.getCategory_name();
            offlinecategorylist.add(category);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.category_row,
                R.id.customrow, offlinecategorylist) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                convertView = super.getView(position, convertView, parent);
                TextView text = (TextView) convertView.findViewById(R.id.customrow);
                text.setTypeface(bold);
                return convertView;
            }
        };

        listView.setAdapter(arrayAdapter);
        registerForContextMenu(listView);
        listView.setLongClickable(true);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Category.this, MainActivity.class);
        finish();
        startActivity(i);
    }
}