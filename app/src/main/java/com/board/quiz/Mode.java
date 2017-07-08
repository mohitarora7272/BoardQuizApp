package com.board.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Mode extends AppCompatActivity {
    /*
        ListView listView;
        ArrayAdapter<String> arrayAdapter;
        String category;
        static InputStream is = null;
        static JSONArray jObj = null;
        static String json = "";
        int categoryid;
        private ArrayList<String> offlinecategorylist = new ArrayList<String>();
        DataBaseHelper db;
        JSONArray json1;
        SharedPreferences prefs;
        private AdView adView;
        Typeface normal, bold;
          /* Your ad unit id. Replace with your actual ad unit id. */
    //private static final String AD_UNIT_ID = DataManager.admobid;
    //boolean cbonline;
    TextView txtStudyMode;
    TextView txtTestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        /*db = new DataBaseHelper(getApplicationContext());

		  normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
		  bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	   
	    AdRequest adRequest = new AdRequest.Builder().build();
	    txtheader = (TextView)findViewById(R.id.txtheader);
	    txtheader.setTypeface(normal);
	    adView.loadAd(adRequest);
	    LinearLayout ll = (LinearLayout)findViewById(R.id.ad);
	    ll.addView(adView);*/


        txtStudyMode = (TextView) findViewById(R.id.txtStudyMode);
        txtTestMode = (TextView) findViewById(R.id.txtTestMode);
        Button home = (Button) findViewById(R.id.btnbackMode);

        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Mode.this, MainActivity.class);
                finish();
                startActivity(i);

            }
        });


        //listView = (ListView) findViewById(R.id.lvclassic_cat);


        //displaydata();
		
/*
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String selected = arg0.getAdapter().getItem(position)
						.toString();
				Intent i = new Intent(Mode.this, TimerQuestions.class);
				i.putExtra("categoryname", selected);
				finish();
				startActivity(i);
			}

		});*/
    }

	
	
/*	public void displaydata()
	{
		
		List<QuizPojo> getcategory = db.getcategory();
		  for (QuizPojo cn : getcategory) {
			  	
			  	category = cn.getCategory_name();
	
			  	offlinecategorylist.add(category);
			  	
	
	        }	

		arrayAdapter = new ArrayAdapter<String>(this, R.layout.category_row,
				R.id.customrow, offlinecategorylist)
		{
			@Override
		    public View getView(int position, View convertView, ViewGroup parent) {

		           convertView= super.getView(position, convertView, parent);

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
	
	
*/

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Mode.this, MainActivity.class);
        finish();
        startActivity(i);
    }
}