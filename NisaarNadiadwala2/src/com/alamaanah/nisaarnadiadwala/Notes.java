package com.alamaanah.nisaarnadiadwala;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;


public class Notes extends ActionBarActivity implements OnItemClickListener{

	private ListView listView1;
	Context context;
	SingleNote[] note_data;
	final Activity activity = this;
	TextView header;
	ArrayList<SingleNote>notes_data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// setContentView(R.layout.notes);
		context = this;
		TinyDB db = new TinyDB(context);
		boolean tutorial;
		if (db.getBoolean("tutorial")==false)
			db.putBoolean("tutorial", false);
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		// new
		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(
						new ColorDrawable(Color.parseColor("#1a237e")))
				.headerLayout(R.layout.notes_header)
				.contentLayout(R.layout.notes)
				.headerOverlayLayout(R.layout.notes_header_overlay);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		// end

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#22A7F0")));
		ab.setIcon(android.R.color.transparent);
		
		/*header=(TextView)findViewById(R.id.tvNotesHeader);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoregular.ttf");
		header.setTypeface(typeface);*/

		listView1 = (ListView) findViewById(android.R.id.list);
		listView1.setOnItemClickListener(this);

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent == false) {
			Toast.makeText( context,(String) "Please connect to the Internet", Toast.LENGTH_SHORT).show();

			FadingActionBarHelper helper2 = new FadingActionBarHelper()
					.actionBarBackground(
							new ColorDrawable(Color.parseColor("#1a237e")))
					.headerLayout(R.layout.notes_header)
					.contentLayout(R.layout.empty_view)
					.headerOverlayLayout(R.layout.notes_header_overlay);
			setContentView(helper2.createView(context));
			TextView emp = (TextView) findViewById(R.id.tvEmptyView);
			emp.setText("No notes");

		} else
			activity.setProgressBarIndeterminateVisibility(true);
		getNotes();

	}

	private void getNotes() {

		AQuery aq = new AQuery(this);
		String url = "http://216.12.194.26/~alamaana/nisaar/api/getNotes2.php";

		aq.ajax(url, String.class, new AjaxCallback<String>() {

			@Override
			public void callback(String url, String apiResponse,
					AjaxStatus status) {

				Log.d("notes", apiResponse);
				Gson gson = new Gson();
				note_data = gson.fromJson(apiResponse, SingleNote[].class);

				NotesAdapter adapter = new NotesAdapter(context,
						R.layout.expandable, note_data);

				if (note_data == null || note_data.length == 0) {
					FadingActionBarHelper helper = new FadingActionBarHelper()
							.actionBarBackground(
									new ColorDrawable(Color
											.parseColor("#1a237e")))
							.headerLayout(R.layout.notes_header)
							.contentLayout(R.layout.empty_view)
							.headerOverlayLayout(R.layout.notes_header_overlay);
					setContentView(helper.createView(context));
					TextView emp = (TextView) findViewById(R.id.tvEmptyView);
					emp.setText("No notes");
					activity.setProgressBarIndeterminateVisibility(false);
					return;

				}

				listView1.setAdapter(adapter);
				activity.setProgressBarIndeterminateVisibility(false);
				notes_data= new ArrayList<SingleNote>(Arrays.asList(note_data));

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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "The position is"+position, Toast.LENGTH_LONG).show();
		Intent read = new Intent(context, NotesViewPager.class);
		read.putParcelableArrayListExtra("notes", notes_data);
		read.putExtra("position",position-1);
		activity.startActivity(read);
		overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
}