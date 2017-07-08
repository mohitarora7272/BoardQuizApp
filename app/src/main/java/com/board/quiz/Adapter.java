package com.board.quiz;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Adapter extends BaseAdapter {

    private ArrayList<QuizPojo> listData;
    private LayoutInflater layoutInflater;
    Context context;
    Typeface normal, bold;

    public String buttonId;

    public Adapter(Context aContext, ArrayList<QuizPojo> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        context = aContext;
        //getSharedPreferenceValues();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_row, null);
            holder = new ViewHolder();

            holder.tvque = (TextView) convertView.findViewById(R.id.tvque);
            holder.tvans = (TextView) convertView.findViewById(R.id.tvans);
            holder.tvcorrect = (TextView) convertView.findViewById(R.id.tvcorrectans);
            holder.tvskip = (TextView) convertView.findViewById(R.id.tvskip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (buttonId.equals("StudyMode")) {
            holder.tvans.setVisibility(View.INVISIBLE);
        } else {
            holder.tvans.setVisibility(View.VISIBLE);
        }


        normal = Typeface.createFromAsset(context.getAssets(), "normal.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "bold.ttf");

        int pos = position + 1;
        holder.tvque.setText(pos + ") " + listData.get(position).get_question());
        holder.tvque.setTextColor(context.getResources().getColor(R.color.black));
        String selans = listData.get(position).getSelanswer();
        String ans = listData.get(position).get_answer();
        holder.tvskip.setTypeface(bold);
        holder.tvque.setTypeface(normal);
        holder.tvans.setTypeface(bold);
        holder.tvcorrect.setTypeface(bold);
        if (selans.length() == 0 || selans.equals("")) {
            holder.tvskip.setText("Not Attempt");
            holder.tvans.setVisibility(View.GONE);
            holder.tvcorrect.setVisibility(View.GONE);
            holder.tvskip.setTextColor(context.getResources().getColor(R.color.md_red_500));
        } else if (selans.equals(ans)) {
            holder.tvskip.setVisibility(View.GONE);
            holder.tvans.setText("Answer : " + ans);
            holder.tvans.setTextColor(context.getResources().getColor(R.color.md_green_500));
            holder.tvcorrect.setVisibility(View.GONE);
        } else {
            holder.tvskip.setVisibility(View.GONE);
            holder.tvans.setText("Selected Answer : " + selans);
            holder.tvcorrect.setText("Correct Answer: " + ans);
            holder.tvans.setTextColor(context.getResources().getColor(R.color.md_red_500));
            holder.tvcorrect.setTextColor(context.getResources().getColor(R.color.md_green_500));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvque, tvans, tvcorrect, tvskip;
    }
}