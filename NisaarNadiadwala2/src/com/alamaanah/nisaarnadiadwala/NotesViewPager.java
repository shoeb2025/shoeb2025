package com.alamaanah.nisaarnadiadwala;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;


public class NotesViewPager extends FragmentActivity {
	static final int ITEMS = 10;
	MyAdapter mAdapter;
	ViewPager mPager;
	ArrayList<SingleNote> notes_data;
	int position_note;
	Context context;
	Button connect;
	RelativeLayout tutorial;
	Button share_other, perm;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	// private static final int MENU_OVERFLOW = 1;
	private MenuDrawer mMenuDrawer;
	// SocialAuthAdapter adapter;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.fragment_pager);
		context = this;
		final TinyDB db = new TinyDB(context);

		mMenuDrawer = MenuDrawer.attach(this, Position.BOTTOM);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mMenuDrawer.setContentView(R.layout.fragment_pager);
		mMenuDrawer.setMenuView(R.layout.menu_top);

		connect = (Button) findViewById(R.id.bConnectService);
		connect.setText("Connect to Facebook");
		share_other = (Button) findViewById(R.id.bShareOther);

		tutorial = (RelativeLayout) findViewById(R.id.rlTutorial);
		// share_other.setOnClickListener(this);
		perm = (Button) findViewById(R.id.bShareFB);
		perm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (connect.getText().equals("Connect to Facebook")) {
					Toast.makeText(getApplicationContext(),
							"Not connected to Facebook", Toast.LENGTH_SHORT)
							.show();

				} else {
					getPerm();
				}

			}
		});

		position_note = getIntent().getIntExtra("position", 0);
		notes_data = getIntent().getParcelableArrayListExtra("notes");

		mPager = (ViewPager) findViewById(R.id.pager);

		// creating the parallaxTransformer, you only need to pass the id of the
		// View (or ViewGroup) you want to do the parallax effect

		mPager.setPageTransformer(false, new ParallaxPagerTransformer(
				R.id.ivNotesImage));
		mAdapter = new MyAdapter(getSupportFragmentManager(), notes_data);
		mPager.setAdapter(mAdapter);

		mPager.setCurrentItem(position_note);

		if (db.getBoolean("tutorial") == false) {

			tutorial.setVisibility(View.VISIBLE);
			Button close = (Button) findViewById(R.id.bCloseTutorial);
			close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tutorial.setVisibility(View.GONE);
					db.putBoolean("tutorial", true);
				}
			});

		}
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}

		share_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SingleNote note_post = notes_data.get(mPager.getCurrentItem());
				
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "\n\n"
						+ note_post.content
						+ "\n\n-Nisar Nadiadwala\nvia Official Android app of Nisaar Nadiadwala";
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						note_post.title);
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				context.startActivity(Intent.createChooser(sharingIntent,
						"Share via"));

			}
		});

		updateView();

	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private void updateView() {
		final Session session = Session.getActiveSession();
		if (session.isOpened()) {
			Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
			connect.setText("Logout from Facebook");
			connect.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					onClickLogout();
				}
			});

		} else {
			connect.setText("Connect to Facebook");
			connect.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					onClickLogin();
				}
			});
		}
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setPermissions(
					Arrays.asList("public_profile"))
					.setCallback(statusCallback));
			// getPermission();

		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void getPerm() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			SingleNote note_post = notes_data.get(mPager.getCurrentItem());
			Bundle postParams = new Bundle();
			postParams.putString("message", note_post.title + "\n\n\n"
					+ note_post.content);
			// postParams.putString("name", "Facebook SDK for Android");
			// postParams.putString("caption",
			// "Build great social apps and get more installs.");
			// postParams.putString("description",
			// "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
			// postParams.putString("link",
			// "https://developers.facebook.com/android");
			// postParams.putString("picture",
			// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i("posting", "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(context, error.getErrorMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "Status posted",
								Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			updateView();
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		ArrayList<SingleNote> notes_data;

		public MyAdapter(FragmentManager fragmentManager,
				ArrayList<SingleNote> notes_data) {
			super(fragmentManager);
			this.notes_data = notes_data;
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show image
				return NotesFragment.init(position, notes_data.get(0));
			case 1: // Fragment # 1 - This will show image
				return NotesFragment.init(position, notes_data.get(1));
			case 2:
				return NotesFragment.init(position, notes_data.get(2));
			case 3:
				return NotesFragment.init(position, notes_data.get(3));
			case 4:
				return NotesFragment.init(position, notes_data.get(4));
			case 5:
				return NotesFragment.init(position, notes_data.get(5));
			case 6:
				return NotesFragment.init(position, notes_data.get(6));
			case 7:
				return NotesFragment.init(position, notes_data.get(7));
			case 8:
				return NotesFragment.init(position, notes_data.get(8));
			case 9:
				return NotesFragment.init(position, notes_data.get(9));
			default:// Fragment # 2-9 - Will show list
				return NotesFragment.init(position, notes_data.get(0));
			}
		}
	}


	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
	}

}