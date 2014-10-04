package com.alamaanah.nisaarnadiadwala;

import java.io.File;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.joanzapata.pdfview.PDFView;

public class ViewPDF extends ActionBarActivity{

	PDFView pdfView;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#22A7F0")));
		ab.setIcon(android.R.color.transparent);
		
		
		setContentView(R.layout.viewpdf);
		context=this;
		int bookid= Integer.parseInt(getIntent().getStringExtra("bookid"));
		
		File target = new File(context.getDir("nisaar", Context.MODE_PRIVATE) + "/"+bookid+".pdf");
		
		pdfView=(PDFView)findViewById(R.id.pdfview);
		pdfView.fromFile(target)
	    .load();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}*/
	

	
}
