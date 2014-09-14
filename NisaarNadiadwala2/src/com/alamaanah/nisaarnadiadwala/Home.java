package com.alamaanah.nisaarnadiadwala;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
//import de.keyboardsurfer.android.widget.crouton.Crouton;

public class Home extends ActionBarActivity implements OnClickListener {

	Button about, notes, events, books;
	ImageView name;
	Fx anim;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	// please enter your sender id
	String SENDER_ID = "900762315781";

	static final String TAG = "GCMDemo";
	GoogleCloudMessaging gcm;

	Context context;
	Context context2;
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		context2=this;
		//ActionBar ab = getSupportActionBar();
		//ab.hide();
		
		name=(ImageView)findViewById(R.id.ivName);
		anim=new Fx();
		anim.slide_in_left(context2, name);
		init();

		context = getApplicationContext();

		TinyDB db = new TinyDB(context);
		ArrayList<Integer> b = new ArrayList<Integer>();
		if (db.getListInt("booksBought", context) == null)
			db.putListInt("booksBought", b, context);
		
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		 
		Boolean isInternetPresent = cd.isConnectingToInternet();
		
		if (isInternetPresent==false)
			Toast.makeText(context, (String)"No Internet connection found", Toast.LENGTH_SHORT).show();

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			if (regid.isEmpty()) {
				new RegisterBackground().execute();
			}

		}

	}

	private void init() {
		
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		// TODO Auto-generated method stub
		about = (Button) findViewById(R.id.bAbout);
		notes = (Button) findViewById(R.id.bNotes);
		events = (Button) findViewById(R.id.bEvents);
		books = (Button) findViewById(R.id.bBooks);
		about.setTypeface(typeface);
		books.setTypeface(typeface);
		notes.setTypeface(typeface);
		events.setTypeface(typeface);
		
		anim.slide_in_right(context2, about);
		anim.slide_in_right(context2, books);
		anim.slide_in_right(context2, notes);
		anim.slide_in_right(context2, events);
		
		
		
		
		
		about.setOnClickListener(this);
		books.setOnClickListener(this);
		notes.setOnClickListener(this);
		events.setOnClickListener(this);
		

	}

	class RegisterBackground extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				regid = gcm.register(SENDER_ID);
				msg = "Dvice registered, registration ID=" + regid;
				Log.d("111", msg);
				asyncJson();

				// Persist the regID - no need to register again.
				storeRegistrationId(context, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {

		}

		public void asyncJson() {

			// perform a Google search in just a few lines of code
			AQuery aq = new AQuery(getApplicationContext());
			String url = "http://nisaar.al-amaanah.com/server/getdevice.php?regid="
					+ regid;
			aq.ajax(url, JSONObject.class, this, "jsonCallback");

		}

		public void jsonCallback(String url, JSONObject json, AjaxStatus status) {

			if (json != null) {
				// successful ajax call
			} else {
				// ajax error
			}

		}

		private void storeRegistrationId(Context context, String regId) {
			final SharedPreferences prefs = getGCMPreferences(context);
			int appVersion = getAppVersion(context);
			Log.i(TAG, "Saving regId on app version " + appVersion);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(PROPERTY_REG_ID, regId);
			editor.putInt(PROPERTY_APP_VERSION, appVersion);
			editor.commit();
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {

		return getSharedPreferences(Home.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bAbout:
			Intent about = new Intent("com.alamaanah.nisaarnadiadwala.ABOUT");
			startActivity(about);
			overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.bBooks:
			Intent books = new Intent("com.alamaanah.nisaarnadiadwala.BOOKS");
			// Intent books= new Intent(this,GridActivity.class);
			startActivity(books);
			overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.bNotes:
			Intent notes = new Intent("com.alamaanah.nisaarnadiadwala.NOTES");
			startActivity(notes);
			overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.bEvents:
			Intent events = new Intent("com.alamaanah.nisaarnadiadwala.EVENTS");
			startActivity(events);
			overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			break;
			
		}
	}

}
