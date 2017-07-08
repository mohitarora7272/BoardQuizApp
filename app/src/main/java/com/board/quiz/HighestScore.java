package com.board.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class HighestScore extends AppCompatActivity {

    private ListView lvscore;
    private DbHighestScore db;
    private TextView highscore;
    private LinearLayout header;
    private ArrayList<String> idlist = new ArrayList<>();
    private ArrayList<String> namelist = new ArrayList<>();
    private ArrayList<String> scorelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_score);

        highscore = (TextView) findViewById(R.id.textViewHS);
        header = (LinearLayout) findViewById(R.id.header);

        Button btnback = (Button) findViewById(R.id.btnback);
        lvscore = (ListView) findViewById(R.id.lvscore);
        db = new DbHighestScore(this);

        getListItem();

        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HighestScore.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    public void getListItem() {
        List<ScoreData> score = db.getAllContacts();
        if (score != null && score.size() == 0) {
            highscore.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);
            lvscore.setVisibility(View.GONE);
            return;
        }

        for (ScoreData sc : score) {

            String id = String.valueOf(sc.getId());
            String name = sc.getName();
            String topscore = sc.getScore();

            idlist.add(id);
            namelist.add(name);
            scorelist.add(topscore);
        }

        ImageAdapter img = new ImageAdapter(this);
        lvscore.setAdapter(img);
    }

    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ImageAdapter(Context c) {
            mInflater = LayoutInflater.from(c);

        }

        @Override
        public int getCount() {
            return idlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.score_row, null);

                holder = new ViewHolder();
                holder.txtid = (TextView) convertView.findViewById(R.id.txtid);
                holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtscore = (TextView) convertView.findViewById(R.id.txtscore);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtid.setText("" + (position + 1));
            holder.txtname.setText(namelist.get(position));
            holder.txtscore.setText(scorelist.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView txtid, txtname, txtscore;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HighestScore.this, MainActivity.class);
        finish();
        startActivity(i);
    }
}