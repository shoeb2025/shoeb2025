package com.alamaanah.nisaarnadiadwala;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;


public class About extends Activity {


	Button follow;
	TextView header,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(new ColorDrawable(Color.parseColor("#1a237e")))
				.headerLayout(R.layout.about_header)
				.contentLayout(R.layout.about)
				.headerOverlayLayout(R.layout.about_header_overlay);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		
		setTitle("About"); 
		android.app.ActionBar ab= getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setIcon(android.R.color.transparent);
		
		header=(TextView)findViewById(R.id.tvAboutHeader);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoregular.ttf");
		header.setTypeface(typeface);
		
		content=(TextView)findViewById(R.id.tvAboutContent);
		Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		content.setTypeface(typeface2);
		
		follow=(Button)findViewById(R.id.bFollow);
		follow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100001221181311"));
				    startActivity(intent);
				} catch(Exception e) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NisaarYNadiadwala")));
				}
			}
		});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
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
