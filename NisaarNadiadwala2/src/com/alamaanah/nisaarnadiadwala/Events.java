package com.alamaanah.nisaarnadiadwala;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Events extends ActionBarActivity {

	private ListView listView1;
	Context context;
	View empty;
	final Activity activity = this;
	TextView header;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// setContentView(R.layout.events);
		context = this;
		init();

	}

	private void init() {
		// TODO Auto-generated method stub

		// new
		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(
						new ColorDrawable(Color.parseColor("#1a237e")))
				.headerLayout(R.layout.events_header)
				.contentLayout(R.layout.events)
				.headerOverlayLayout(R.layout.events_header_overlay);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		// end

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#22A7F0")));
		ab.setIcon(android.R.color.transparent);
		
		header=(TextView)findViewById(R.id.tvEventsHeader);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoregular.ttf");
		header.setTypeface(typeface);

		listView1 = (ListView) findViewById(android.R.id.list);
		// listView1.setBackground(new
		// ColorDrawable(Color.parseColor("#e5e5e5")));;
		empty = findViewById(R.id.tvEmptyView1);
		// listView1.setEmptyView(empty);

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent == false) {
			Toast.makeText(context, (String) "Please connect to the Internet",
					Toast.LENGTH_SHORT).show();

			FadingActionBarHelper helper2 = new FadingActionBarHelper()
					.actionBarBackground(
							new ColorDrawable(Color.parseColor("#1a237e")))
					.headerLayout(R.layout.events_header)
					.contentLayout(R.layout.empty_view)
					.headerOverlayLayout(R.layout.events_header_overlay);
			setContentView(helper2.createView(context));
			TextView emp = (TextView) findViewById(R.id.tvEmptyView);
			emp.setText("No events");
			
		} else
			activity.setProgressBarIndeterminateVisibility(true);
		getEvents();
	}

	private void getEvents() {
		// TODO Auto-generated method stub

		AQuery aq = new AQuery(this);
		String url = "http://216.12.194.26/~alamaana/nisaar/api/getEvents.php";

		aq.ajax(url, String.class, new AjaxCallback<String>() {

			@Override
			public void callback(String url, String apiResponse,
					AjaxStatus status) {

				Log.d("events", apiResponse);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				SingleEvent[] event_data = gson.fromJson(apiResponse,
						SingleEvent[].class);
				EventsAdapter adapter = new EventsAdapter(context,
						R.layout.events_single, event_data);

				if (event_data == null || event_data.length == 0) {
					FadingActionBarHelper helper = new FadingActionBarHelper()
							.actionBarBackground(
									new ColorDrawable(Color
											.parseColor("#1a237e")))
							.headerLayout(R.layout.events_header)
							.contentLayout(R.layout.empty_view)
							.headerOverlayLayout(R.layout.events_header_overlay);
					setContentView(helper.createView(context));
					TextView emp = (TextView) findViewById(R.id.tvEmptyView);
					emp.setText("No upcoming events");
					activity.setProgressBarIndeterminateVisibility(false);
					return;

				}
				listView1.setAdapter(adapter);
				activity.setProgressBarIndeterminateVisibility(false);

			}
		});

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, Home.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
	}
}